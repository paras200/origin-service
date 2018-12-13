package com.ilab.origin.certificates.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.certificates.model.CertificateTrack;
import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.certificates.repo.CertificatesRepo;
import com.ilab.origin.certificates.to.BulkCertificatesTO;
import com.ilab.origin.certificates.to.CertificatesTO;
import com.ilab.origin.certificates.to.Student;
import com.ilab.origin.common.model.QueryParamTO;
import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.common.utils.DateUtils;
import com.ilab.origin.common.utils.ValidationUtils;
import com.ilab.origin.feedback.mode.FeedBackData;
import com.ilab.origin.feedback.service.FeedbackService;
import com.ilab.origin.serial.SerialNumberGenerator;
import com.ilab.origin.serial.UUIDSerialization;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.OriginStatus;
import com.ilab.origin.validator.model.OriginTrack;
import com.ilab.origin.validator.model.Result;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/certificates")
public class CertificatesValidationService {
	
	private static Log log = LogFactory.getLog(CertificatesValidationService.class.getName());
	
	@Autowired
	private CertificatesRepo certRepo;
	
	private SerialNumberGenerator slgenerator = UUIDSerialization.getInstance();
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	@Autowired
	private CertificateTrackRecorder certificatesRecorder;
	
	@Autowired
	private CertificatesAnalyticsService certAnalyticService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping("/generate-qrcode")	
	public List<Certificates> saveAllQRCode(@RequestBody CertificatesTO inputData) throws OriginException{		
		
		if(inputData.getStudents() == null || inputData.getStudents().size() == 0){
			throw new OriginException("Students data is missing");
		}
		List<Certificates> certList = new ArrayList<>();
		List<Student> students = inputData.getStudents();
		for (Student student : students) {
			Certificates certificates = new Certificates();
			certificates.setCourseName(inputData.getCourseName());
			certificates.setInstituteName(inputData.getInstituteName());
			certificates.setUniversityName(inputData.getUniversityName());
			certificates.setMerchantId(inputData.getMerchantId());
			certificates.setMerchantKey(inputData.getMerchantKey());
			
			certificates.setStudentName(student.getStudentName());
			certificates.setDateOfBirth(student.getDateOfBirth());
			certificates.setCertificateId(student.getCertificateId());
			certificates.setCertIssueDate(student.getCertIssueDate());
			
			certificates.setQrCode(Certificates.CERTIFICATES_QR_PREFIX + slgenerator.getSequenceNumber());
			System.out.println("qr code : " + certificates.getQrCode());
			
			certList.add(certificates);
		}
		List<Certificates> cList = certRepo.save(certList);
		return cList;
	}

	@PostMapping("/generate-bulk-qrcode")	
	public List<Certificates> saveAllQRCode(@RequestBody BulkCertificatesTO bulkCertificatesTO) throws OriginException{		
		
		ValidationUtils.validateInputParam(bulkCertificatesTO.getMerchantId());
		List<Student> dataList = bulkCertificatesTO.getData();
		//List<String> headerList = bulkCertificatesTO.getHeader();//Arrays.asList(bulkCertificatesTO.getHeader().split(","));
		
		List<Certificates> certList = new ArrayList<>();
		
		for (Student student : dataList) {
			//List<String> studentData = Arrays.asList(text.split(","));
			Certificates certificates = new Certificates();
			certificates.setMerchantId(bulkCertificatesTO.getMerchantId());
			certificates.setMerchantKey(bulkCertificatesTO.getMerchantKey());
			certificates.setUniversityName(bulkCertificatesTO.getUniversityName());
			certificates.setFileId(bulkCertificatesTO.getFileId());
			
			certificates.setStudentName(student.getStudentName());
			certificates.setDateOfBirth(student.getDateOfBirth());
			certificates.setCertificateId(student.getCertificateId());
			certificates.setInstituteName(student.getInstituteName());
			certificates.setCertIssueDate(student.getCertIssueDate());
			certificates.setCourseName(student.getCourseName());
			
			certificates.setQrCode(Certificates.CERTIFICATES_QR_PREFIX + slgenerator.getSequenceNumber());
			certList.add(certificates);
		}
		
		
		List<Certificates> cList = certRepo.save(certList);
		return cList;
	}
	
	public Certificates validateCertificates(OriginTrack originTrack) {
		String qrcode = originTrack.getQrcode();
		log.info("Certificates Validation is in progress...");
		Certificates certificate =  certRepo.findByQrCode(qrcode);
		if(certificate != null) {
			certificate.setStatusCode(OriginStatus.GREEN);
			StringBuilder sb = new StringBuilder("The Certificate is issued by " + certificate.getInstituteName() +". ");
			sb.append("Please validate the Certificates details as below.");
			certificate.setMessage(sb.toString());
			
			CertificateTrack certificateTrack = populateCertificatesTrack(originTrack , certificate);
			certificatesRecorder.asyncSave(certificateTrack);
			
			if(certificate.getLatestScanStatus() == OriginStatus.NO_SCAN) {
				certificate.setLatestScanStatus(OriginStatus.GREEN);
				certAnalyticService.saveAsync(certificate);
			}
			
		}else {
			certificate = new Certificates();
			certificate.setStatusCode(OriginStatus.RED);
			StringBuilder sb = new StringBuilder("The Certificate is not listed with us");
			certificate.setMessage(sb.toString());
		}
		return certificate;
	}
	
	private CertificateTrack populateCertificatesTrack(OriginTrack originTrack, Certificates certificate) {
		CertificateTrack cTrack = new CertificateTrack();
		cTrack.setComment(originTrack.getComment());
		cTrack.setQrcode(originTrack.getQrcode());
		cTrack.setLocation(originTrack.getLocation());
		cTrack.setScanTime(new Date());
		cTrack.setUserId(originTrack.getUserId());
		
		cTrack.setCourseName(certificate.getCourseName());
		cTrack.setInstitutesName(certificate.getInstituteName());
		cTrack.setUniversityName(certificate.getUniversityName());
		cTrack.setMerchantId(certificate.getMerchantId());
		
		if(!(StringUtils.isEmpty(originTrack.getUserType()))) {
			cTrack.setUserType(originTrack.getUserType());
		}
		return cTrack;
	}

	@RequestMapping(value="/generic-query" , method = { RequestMethod.POST })
	public List<Certificates>  getValidationData(@RequestBody QueryParamTO paramTO) throws OriginException{
		Map<String,String> queryMap = paramTO.getQueryMap();
		String merchantId = queryMap.get("merchantId");
		
		ValidationUtils.validateInputParam(merchantId);
		Criteria latestScanCritera =  handelLatestScanStatus(queryMap);
		Criteria qrDateCriteria = handleQRGenDateCriteria(queryMap);
		List<Criteria> criteriaList = mongoQueryMgr.createCriteriaList(queryMap);
		if(criteriaList != null && qrDateCriteria != null) {
			criteriaList.add(qrDateCriteria);
		}
		
		if(latestScanCritera != null) {
			criteriaList.add(latestScanCritera);
		}
		
		
		Query query = mongoQueryMgr.createQuery(criteriaList);
		
		List<?> results =  mongoQueryMgr.executeQuery(query, Certificates.class,"timeinmilli", paramTO.getPageNum(), paramTO.getPageSize());	
		log.info("result size retruned : " + results.size());
		
		List<Certificates> updatedResult = new ArrayList<>();
		for (Object resObj : results) {
			Certificates od = (Certificates) resObj;
			updatedResult.add(od);
		}
		updateScanStatus(updatedResult);
		updateUserFeedbackInfo(updatedResult);
		return updatedResult;
	}
	

	@PostMapping("/update-cert-url")	
	public Result updateCertificatesUrl(@RequestBody Map<String, String> qrCodeMap) throws OriginException{	
		if(qrCodeMap != null && qrCodeMap.size() > 0) {
			Set<String> qrCodeSet = qrCodeMap.keySet();
			Criteria criteria =  mongoQueryMgr.addToInQuery("qrCode", new ArrayList<>(qrCodeSet));
			Query query = mongoQueryMgr.createQuery(criteria);
			
			@SuppressWarnings("unchecked")
			List<Certificates> certList = (List<Certificates>) mongoQueryMgr.executeQuery(Certificates.class, query);
			certList.stream().forEach(x -> {
				x.setCertUrl(qrCodeMap.get(x.getQrCode()));
			});
			certRepo.save(certList);
		}
		return new Result();
	}
	
	@GetMapping("/get-scan-hist")	
	public List<CertificateTrack> getCertificatesScanHistory(@RequestParam(value="userid") String userId) throws OriginException{	
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("userId", userId);
		
		@SuppressWarnings("unchecked")
		List<CertificateTrack> results = (List<CertificateTrack>) mongoQueryMgr.executeQuery(queryMap, CertificateTrack.class);
		return results;
	}
	
	@GetMapping("/has-user-scanned-before")	
	public Boolean hasUserAlreadyScannedit(@RequestParam(value="userid") String userId, @RequestParam(value="qrcode") String qrcode) throws OriginException{	
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("userId", userId);
		queryMap.put("qrcode", qrcode);
		
		@SuppressWarnings("unchecked")
		List<CertificateTrack> results = (List<CertificateTrack>) mongoQueryMgr.executeQuery(queryMap, CertificateTrack.class);
		if(results.size() > 0) return new Boolean(true);
		return new Boolean(false);
	}
	
	private Criteria handelLatestScanStatus(Map<String, String> queryMap) {
		String value = queryMap.get("latestScanStatus");
		if(StringUtils.isEmpty(value)) return null;
		Criteria cc = Criteria.where("latestScanStatus").is(Integer.parseInt(value));
		queryMap.remove("latestScanStatus");
		return cc;
	}

	private void updateUserFeedbackInfo(List<Certificates> result) {
		List<String> qrList = result.stream().map(Certificates::getQrCode).collect(Collectors.toList());
		Map<String, FeedBackData> datamap = feedbackService.getFeedbackDataByQrcodes(qrList);
		for (Certificates od : result) {
			if(datamap.get(od.getQrCode()) != null){
				od.setUserFeeback(true);
			}
		}
	}
	
	private void updateScanStatus(List<Certificates> updatedResult) {
		// TODO Auto-generated method stub
		List<String> qrList = updatedResult.stream().map(Certificates::getQrCode).collect(Collectors.toList());
		Set<String> qrTrackList = certAnalyticService.getCertificatesTrackQRList(qrList);
		List<Certificates> filteredList =  updatedResult.stream().filter(c -> qrTrackList.contains(c.getQrCode())).collect(Collectors.toList());
		for (Certificates certificates : filteredList) {
			certificates.setLatestScanStatus(OriginStatus.GREEN);
		}
	}

	private Criteria handleQRGenDateCriteria(Map<String, String> queryMap) throws OriginException {
		String startDate = queryMap.get("startDate");
		String endDate = queryMap.get("endDate");		
		queryMap.remove("startDate");
		queryMap.remove("endDate");
		
		if(StringUtils.isEmpty(endDate)) {
			endDate = startDate;
		}
		if(!StringUtils.isEmpty(startDate)) {
			try {
				return Criteria.where("timeinmilli").gte(DateUtils.convertStartDate(startDate)).lt(DateUtils.convertEndDate(endDate));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new OriginException("date can't parsed , please chcek the format dd/MM/yyyy");
			}
		}
		return null;
	}
	
}

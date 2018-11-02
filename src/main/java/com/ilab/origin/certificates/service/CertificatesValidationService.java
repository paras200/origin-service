package com.ilab.origin.certificates.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.ilab.origin.serial.SerialNumberGenerator;
import com.ilab.origin.serial.UUIDSerialization;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.OriginStatus;
import com.ilab.origin.validator.model.OriginTrack;

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
	
	private CertificateTrackRecorder certificatesRecorder;
	
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
			
			certificates.setQrCode(Certificates.CERTIFICATES_QR_PREFIX + slgenerator.getSequenceNumber());
			
			certList.add(certificates);
		}
		List<Certificates> cList = certRepo.save(certList);
		return cList;
	}

	@PostMapping("/generate-bulk-qrcode")	
	public List<Certificates> saveAllQRCode(@RequestBody BulkCertificatesTO bulkCertificatesTO) throws OriginException{		
		
		ValidationUtils.validateInputParam(bulkCertificatesTO.getMerchantId());
		List<Student> dataList = bulkCertificatesTO.getData();
		List<String> headerList = bulkCertificatesTO.getHeader();//Arrays.asList(bulkCertificatesTO.getHeader().split(","));
		
		List<Certificates> certList = new ArrayList<>();
		
		for (Student student : dataList) {
			//List<String> studentData = Arrays.asList(text.split(","));
			Certificates certificates = new Certificates();
			certificates.setMerchantId(bulkCertificatesTO.getMerchantId());
			certificates.setMerchantKey(bulkCertificatesTO.getMerchantKey());
			certificates.setUniversityName(bulkCertificatesTO.getUniversityName());
			
			certificates.setStudentName(student.getStudentName());
			certificates.setDateOfBirth(student.getDateOfBirth());
			certificates.setCertificateId(student.getCertificateId());
			certificates.setInstituteName(student.getInstituteName());
			//int index = 0;
			/*for (int index =0; index < headerList.size() ; index++) {
				if("CourseName".equalsIgnoreCase(headerList.get(index))) {
					certificates.setCourseName(studentData.get(index));
				}else if("InstituteName".equalsIgnoreCase(headerList.get(index))) {
					certificates.setInstituteName(studentData.get(index));
				}else if("StudentName".equalsIgnoreCase(headerList.get(index))) {
					certificates.setStudentName(studentData.get(index));
				}else if("DateOfBirth".equalsIgnoreCase(headerList.get(index))) {
					certificates.setDateOfBirth(studentData.get(index));
				}else if("CertificateId".equalsIgnoreCase(headerList.get(index))) {
					certificates.setCertificateId(studentData.get(index));
				}
			}*/
			
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
		
		return cTrack;
	}

	@RequestMapping(value="/generic-query" , method = { RequestMethod.POST })
	public List<Certificates>  getValidationData(@RequestBody QueryParamTO paramTO) throws OriginException{
		Map<String,String> queryMap = paramTO.getQueryMap();
		String merchantId = queryMap.get("merchantId");
		
		ValidationUtils.validateInputParam(merchantId);
		
		Criteria qrDateCriteria = handleQRGenDateCriteria(queryMap);
		List<Criteria> criteriaList = mongoQueryMgr.createCriteriaList(queryMap);
		if(criteriaList != null && qrDateCriteria != null) {
			criteriaList.add(qrDateCriteria);
		}
		
		Query query = mongoQueryMgr.createQuery(criteriaList);
		
		List<?> results =  mongoQueryMgr.executeQuery(query, Certificates.class,"timeinmilli", paramTO.getPageNum(), paramTO.getPageSize());	
		log.info("result size retruned : " + results.size());
		
		List<Certificates> updatedResult = new ArrayList<>();
		for (Object resObj : results) {
			Certificates od = (Certificates) resObj;
			updatedResult.add(od);
		}
		
		return updatedResult;
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

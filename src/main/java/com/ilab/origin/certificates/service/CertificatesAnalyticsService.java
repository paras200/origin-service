package com.ilab.origin.certificates.service;

import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.certificates.model.CertificateTrack;
import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.certificates.to.ReportResult;
import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.common.utils.DateUtils;
import com.ilab.origin.common.utils.NumberUtil;
import com.ilab.origin.common.utils.ValidationUtils;
import com.ilab.origin.mobileapp.model.AppUser;
import com.ilab.origin.tracker.error.OriginException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/certificates-report")
public class CertificatesAnalyticsService {
	
	private static Log log = LogFactory.getLog(CertificatesAnalyticsService.class.getName());
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	

	@RequestMapping(value="/get-scan-hist" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<CertificateTrack> getScanHistory(@RequestParam(value="qrcode") String qrcode){
		Criteria cr = Criteria.where("qrcode").is(qrcode);
		Query query = new Query();
		query.addCriteria(cr);
		
		@SuppressWarnings("unchecked")
		List<CertificateTrack>  trackResult = (List<CertificateTrack> ) mongoQueryMgr.executeQuery(CertificateTrack.class, query);
		return trackResult;
	}
	
	@RequestMapping(value="/qrcode/analytics" , method = { RequestMethod.POST })
	public ReportResult  generateQrAnalytics(@RequestBody Map<String, String> queryMap) throws OriginException{
		String merchantId = queryMap.get("merchantId");
		ValidationUtils.validateInputParam(merchantId);
		
		Criteria qrDateCriteria = handleQRGenDateCriteria(queryMap);
		Criteria latestScanCritera =  handelLatestScanStatus(queryMap);
		
		List<Criteria> criteriaList = mongoQueryMgr.createCriteriaList(queryMap);
		if(criteriaList != null && qrDateCriteria != null) {
			criteriaList.add(qrDateCriteria);
		}
		addCourseNameCritera(criteriaList, queryMap);
		if(latestScanCritera != null) {
			criteriaList.add(latestScanCritera);
		}
		
		Query query = mongoQueryMgr.createQuery(criteriaList);
		query.fields().include("qrCode");
		query.fields().include("courseName");
		query.fields().include("timeinmilli");
		List<?> results =  mongoQueryMgr.executeQuery(Certificates.class , query);	
		log.info("result size retruned : " + results.size());
		
		List<Certificates> updatedResult = new ArrayList<>();
		for (Object resObj : results) {
			Certificates od = (Certificates) resObj;
			System.out.println(od);
			updatedResult.add(od);
		}
		
		int totalCount = updatedResult.size();
		int scanned = getScannedCount(updatedResult);
		int notScanned = totalCount - scanned;
		Map<String, String> resultMap = new HashMap<>();
		if(totalCount > 0) {
			resultMap.put("Not Scanned" +appendCount(notScanned), NumberUtil.floatToString((notScanned*100)/totalCount));
			resultMap.put("Scanned" +appendCount(scanned), NumberUtil.floatToString((scanned*100)/totalCount));	
		}
		
		
		ReportResult rr = new ReportResult();
		rr.setReportName("QR Scan Analytics");
		rr.setResultMap(resultMap);
		return rr;
	}
	
	@RequestMapping(value="/course-wise" , method = { RequestMethod.POST })
	public ReportResult  generateCourseWiseAnalytics(@RequestBody Map<String, String> queryMap) throws OriginException{
		String merchantId = queryMap.get("merchantId");
		ValidationUtils.validateInputParam(merchantId);
		
		Criteria qrDateCriteria = handleQRGenDateCriteria(queryMap);
		
		List<Criteria> criteriaList = mongoQueryMgr.createCriteriaList(queryMap);
		if(criteriaList != null && qrDateCriteria != null) {
			criteriaList.add(qrDateCriteria);
		}
		addCourseNameCritera(criteriaList, queryMap);
		
		Query query = mongoQueryMgr.createQuery(criteriaList);
		query.fields().include("qrCode");
		query.fields().include("courseName");
		query.fields().include("timeinmilli");
		List<?> results =  mongoQueryMgr.executeQuery(Certificates.class , query);	
		log.info("result size retruned : " + results.size());
		
		List<Certificates> updatedResult = new ArrayList<>();
		for (Object resObj : results) {
			Certificates od = (Certificates) resObj;
			System.out.println(od);
			updatedResult.add(od);
		}
		List<String> qrList = updatedResult.stream().map(Certificates::getQrCode).collect(Collectors.toList());
		List<CertificateTrack> scannedList = getCertificatesTrackInfo(qrList);
		Map<String, Long> courseWiseMap = scannedList.stream().collect(Collectors.groupingBy(CertificateTrack::getCourseName , Collectors.counting()));
		
		long totalCount = scannedList.size();
		Map<String, String> pieChartResult = new HashMap<>();
		
		List<String> nameList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		nameList.add("Scan Count");
		dataList.add("Count");
		for (String courseName : courseWiseMap.keySet()) {
			nameList.add(courseName);
			dataList.add(courseWiseMap.get(courseName)+"");
			if(totalCount > 0) {
				pieChartResult.put(courseName, NumberUtil.floatToString((courseWiseMap.get(courseName)*100)/totalCount));	
			}
		}
		
		ReportResult rr = new ReportResult();
		rr.setReportName("Validation Request Analysis - Course Wise");
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(nameList);
		data.add(dataList);
		rr.setData(data);
		rr.setResultMap(pieChartResult);
		return rr;
	}
	
	@RequestMapping(value="/course-and-company-wise" , method = { RequestMethod.POST })
	public ReportResult  generateCourseAndCompanyWiseAnalytics(@RequestBody Map<String, String> queryMap) throws OriginException{
		String merchantId = queryMap.get("merchantId");
		ValidationUtils.validateInputParam(merchantId);
		
		Criteria qrDateCriteria = handleQRGenDateCriteria(queryMap);
		
		List<Criteria> criteriaList = mongoQueryMgr.createCriteriaList(queryMap);
		if(criteriaList != null && qrDateCriteria != null) {
			criteriaList.add(qrDateCriteria);
		}
		addCourseNameCritera(criteriaList, queryMap);
		
		Query query = mongoQueryMgr.createQuery(criteriaList);
		query.fields().include("qrCode");
		query.fields().include("courseName");
		query.fields().include("timeinmilli");
		
		@SuppressWarnings("unchecked")
		List<Certificates> results = (List<Certificates>) mongoQueryMgr.executeQuery(Certificates.class , query);	
		log.info("result size retruned : " + results.size());
		
		List<String> qrList = results.stream().map(Certificates::getQrCode).collect(Collectors.toList());
		List<CertificateTrack> scannedList = getCertificatesTrackInfo(qrList);
		Map<String, List<CertificateTrack>> courseWiseMap = scannedList.stream().collect(Collectors.groupingBy(CertificateTrack::getCourseName));
		
		int indScanCount = 0;
		int businessScanCount = 0;
		List<String> nameList = new ArrayList<>();
		
		nameList.add("Scan Count");
		nameList.add("Individual");
		nameList.add("3rd Party");
		
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(nameList);
		
		for (String courseName : courseWiseMap.keySet()) {
			//nameList.add(courseName);
			List<String> courseData = new ArrayList<>();
			courseData.add(courseName);
			
			List<CertificateTrack> cList = courseWiseMap.get(courseName);
			if(cList == null || cList.size() == 0) {
				courseData.add("0");
				courseData.add("0");
			}else {
				Map<String, Long> userTypeMap = cList.stream().collect(Collectors.groupingBy(CertificateTrack::getUserType , Collectors.counting()));
				Long indTypeNum = userTypeMap.get(AppUser.USER_TYPE_INDIVIDUAL);
				if(indTypeNum == null) {
					indScanCount += cList.size();
					courseData.add(cList.size()+""); // TODO it should be set to 0 , need to fix userType data issue
				}else {
					indScanCount += indTypeNum;
					courseData.add(indTypeNum+"");
				}
				
				//add business
				Long bussTypeNum = userTypeMap.get(AppUser.USER_TYPE_BUSINESS);
				if(bussTypeNum == null) {
					courseData.add("0");
				}else {
					courseData.add(bussTypeNum+"");
					businessScanCount += bussTypeNum;
				}
			}
			data.add(courseData);
		}
		
		
		ReportResult rr = new ReportResult();
		rr.setReportName("Validation Request Analysis - Course Wise");
		rr.setData(data);
		
		// pie chart result
		Map<String, String> pieChartResult = new HashMap<>();
		int totalScan = indScanCount + businessScanCount;
		if(totalScan > 0) {
			pieChartResult.put("Individual", NumberUtil.calculatePercentage(indScanCount, totalScan));
			pieChartResult.put("\"3rd Party\"", NumberUtil.calculatePercentage(businessScanCount, totalScan));
		}
		rr.setResultMap(pieChartResult);
		return rr;
	}
	
	private void addCourseNameCritera(List<Criteria> criteriaList, Map<String, String> queryMap) {
		if(!(queryMap.containsKey("courseName"))) {
			Criteria cr = Criteria.where("courseName").ne(null);
			criteriaList.add(cr);
		}
	}

	private int getScannedCount(List<Certificates> result) {
		List<String> qrList = result.stream().map(Certificates::getQrCode).collect(Collectors.toList());
		List<CertificateTrack> trackResult = getCertificatesTrackInfo(qrList);
		List<String> trackQrList = trackResult.stream().map(CertificateTrack::getQrcode).distinct().collect(Collectors.toList());
		
		return trackQrList.size();
	}

	public List<CertificateTrack> getCertificatesTrackInfo(List<String> qrList) {
		Criteria cr = Criteria.where("qrcode").in(qrList);
		Query query = new Query();
		query.addCriteria(cr);
		query.fields().include("qrcode");
		query.fields().include("courseName");
		query.fields().include("userId");
		query.fields().include("userType");
		List<CertificateTrack>  trackResult = (List<CertificateTrack> ) mongoQueryMgr.executeQuery(CertificateTrack.class, query);
		return trackResult;
	}

	
	public Set<String> getCertificatesTrackQRList(List<String> qrList) {
		Criteria cr = Criteria.where("qrcode").in(qrList);
		Query query = new Query();
		query.addCriteria(cr);
		query.fields().include("qrcode");
		List<CertificateTrack>  trackResult = (List<CertificateTrack> ) mongoQueryMgr.executeQuery(CertificateTrack.class, query);
		Set<String> trackQrList = trackResult.stream().map(CertificateTrack::getQrcode).distinct().collect(Collectors.toSet());
		return trackQrList;
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
	
	private Criteria handelLatestScanStatus(Map<String, String> queryMap) {
		String value = queryMap.get("latestScanStatus");
		if(StringUtils.isEmpty(value)) return null;
		Criteria cc = Criteria.where("latestScanStatus").is(Integer.parseInt(value));
		queryMap.remove("latestScanStatus");
		return cc;
	}
	private String appendCount(float count) {
		return "("+NumberUtil.floatToString(count)+")";
	}
}

package com.ilab.origin.certificates.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.certificates.model.CertificateTrack;
import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.certificates.to.ChartData;
import com.ilab.origin.certificates.to.ReportResult;
import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.common.utils.DateUtils;
import com.ilab.origin.common.utils.NumberUtil;
import com.ilab.origin.common.utils.ValidationUtils;
import com.ilab.origin.tracker.error.OriginException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/certificates-report")
public class CertificatesAnalyticsService {
	
	private static Log log = LogFactory.getLog(CertificatesAnalyticsService.class.getName());
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	

	@RequestMapping(value="/qrcode/analytics" , method = { RequestMethod.POST })
	public ReportResult  generateQrAnalytics(@RequestBody Map<String, String> queryMap) throws OriginException{
		String merchantId = queryMap.get("merchantId");
		ValidationUtils.validateInputParam(merchantId);
		
		Criteria qrDateCriteria = handleQRGenDateCriteria(queryMap);
		
		List<Criteria> criteriaList = mongoQueryMgr.createCriteriaList(queryMap);
		if(criteriaList != null && qrDateCriteria != null) {
			criteriaList.add(qrDateCriteria);
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
		resultMap.put("Not Scanned" +appendCount(notScanned), NumberUtil.floatToString((notScanned*100)/totalCount));
		resultMap.put("Scanned" +appendCount(scanned), NumberUtil.floatToString((scanned*100)/totalCount));
		
		ReportResult rr = new ReportResult();
		rr.setReportName("QR Analytics");
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
		
		List<String> nameList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		for (String courseName : courseWiseMap.keySet()) {
			nameList.add(courseName);
			dataList.add(courseWiseMap.get(courseName)+"");
		}
		
		ReportResult rr = new ReportResult();
		rr.setReportName("Validation Request Analysis - Course Wise");
		rr.setLabels(nameList);
		rr.setChartData(new ChartData("count", dataList));
		return rr;
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
		List<CertificateTrack>  trackResult = (List<CertificateTrack> ) mongoQueryMgr.executeQuery(CertificateTrack.class, query);
		return trackResult;
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
	
	private String appendCount(float count) {
		return "("+NumberUtil.floatToString(count)+")";
	}
}

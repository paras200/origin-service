package com.ilab.origin.tracker.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.model.TransactionInfo;
import com.ilab.origin.tracker.to.TraceReportTo;

@Service
public class ReportingRepo {

	private static Log log = LogFactory.getLog(ReportingRepo.class.getName());
	
	@Autowired
	private MongoOperations operations;
	
	@Autowired
	private TrackingDataRepository trackingDataRepo;
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	public TraceReportTo detailedReport(String qrcode, String lotNumber) throws OriginException {
		Criteria criteria = null;
		criteria = mongoQueryMgr.addToQuery("qrcode",qrcode, criteria);
		criteria = mongoQueryMgr.addToQuery("lotNumber",lotNumber, criteria);
		
		Query query = new Query();
		query.addCriteria(criteria);
		List<TransactionInfo>  result = operations.find(query, TransactionInfo.class);
		log.info("result size retruned : " + result.size());
		
		if(result.size() > 1) throw new OriginException("data consistency check breaks");
		
		List<TrackingData> tracList = null;
		TransactionInfo ti = null;
		if(result.size() == 1) {
			ti = result.get(0);
			tracList = trackingDataRepo.findByQrcode(ti.getQrcode());
		}
		
		TraceReportTo reportTo = new TraceReportTo();
		reportTo.setTransactionInfo(ti);
		reportTo.setTrackingDataList(tracList);
		return reportTo;
	}
	
	public List<TraceReportTo> detailedReport(List<String> qrcode, List<String> lotNumber) throws OriginException {
		Criteria criteria = null;
		criteria = mongoQueryMgr.addToInQuery("qrcode",qrcode, criteria);
		criteria = mongoQueryMgr.addToInQuery("lotNumber",lotNumber, criteria);
		
		List<TraceReportTo> reportTo = getDataByCriteria(criteria);
		return reportTo;
	}

	public List<TraceReportTo> detailedReport(String qrcode, String merchantId, String productName, String lotNumber) {
		Criteria criteria = null;
		criteria = mongoQueryMgr.addToQuery("qrcode",qrcode, criteria);
		criteria = mongoQueryMgr.addToQuery("merchantId",merchantId, criteria);
		criteria = mongoQueryMgr.addToQuery("productName",productName, criteria);
		criteria = mongoQueryMgr.addToQuery("lotNumber",lotNumber, criteria);
	
		List<TraceReportTo> result = getDataByCriteria(criteria);
		log.info("result size retruned : " + result.size());
		return result;
	}
	
	private List<TraceReportTo> getDataByCriteria(Criteria criteria) {
		Query query = new Query();
		query.addCriteria(criteria);
		List<TransactionInfo>  results = operations.find(query, TransactionInfo.class);
		log.info("result size retruned : " + results.size());
				
		List<TrackingData> tracList = null;
		List<String> qrcodes = new ArrayList<>();
		for (TransactionInfo ti : results) {
			qrcodes.add(ti.getQrcode());
		}
		
		Criteria criteria2 = null;
		criteria2 = mongoQueryMgr.addToInQuery("qrcode",qrcodes, criteria2);
		Query query2 = new Query();
		query2.addCriteria(criteria2);
		List<TrackingData>  traceList = operations.find(query2, TrackingData.class);
		Map<String, List<TrackingData>> trackMap = convertToMap(traceList);
		
		List<TraceReportTo> reportList = new ArrayList<>();
		for (TransactionInfo ti : results) {
			TraceReportTo reportTo = new TraceReportTo();
			List<TrackingData> tdList = trackMap.get(ti.getQrcode());
			Collections.sort(tdList);
			
			reportTo.setTransactionInfo(ti);
			reportTo.setTrackingDataList(tdList);
			reportList.add(reportTo);
		}
		
		return reportList;
	}
	
	private Map<String, List<TrackingData>> convertToMap(List<TrackingData> traceList) {
		Map<String, List<TrackingData>> trackMap = new HashMap<>();
		for (TrackingData trackingData : traceList) {
			List<TrackingData> tdList =  trackMap.get(trackingData.getQrcode());
			if(tdList == null) {
				tdList = new ArrayList<>();
				trackMap.put(trackingData.getQrcode(), tdList);
			}
			tdList.add(trackingData);
		}
		
		return trackMap;
	}

	public List<TransactionInfo> summaryView(String qrcode, String merchantId, String productName, String lotNumber) {
		Criteria criteria = null;
		criteria = mongoQueryMgr.addToQuery("qrcode",qrcode, criteria);
		criteria = mongoQueryMgr.addToQuery("merchantId",merchantId, criteria);
		criteria = mongoQueryMgr.addToQuery("productName",productName, criteria);
		criteria = mongoQueryMgr.addToQuery("lotNumber",lotNumber, criteria);
		
		
		Query query = new Query();
		query.addCriteria(criteria);
		List<TransactionInfo>  result = operations.find(query, TransactionInfo.class);
		log.info("result size retruned : " + result.size());
		return result;
	}

	/**
	private Criteria addToQuery(String fieldName, String value, Criteria criteria) {
		if(StringUtils.isEmpty(value)) return criteria;
		
		if(criteria == null) {
			criteria = Criteria.where(fieldName).is(value);
		}else {
			criteria = criteria.andOperator(Criteria.where(fieldName).is(value));
		}
		return criteria;
	}
	
	private Criteria addToInQuery(String fieldName, List<String> value, Criteria criteria) {
		if(StringUtils.isEmpty(value)) return criteria;
		
		if(criteria == null) {
			criteria = Criteria.where(fieldName).in(value);
		}else {
			criteria = criteria.andOperator(Criteria.where(fieldName).in(value));
		}
		return criteria;
	}
	**/
}

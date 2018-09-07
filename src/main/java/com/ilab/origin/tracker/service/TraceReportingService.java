package com.ilab.origin.tracker.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.tracker.model.TransactionInfo;
import com.ilab.origin.tracker.repo.ReportingRepo;
import com.ilab.origin.tracker.to.TraceReportTo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/trace-reporting")
public class TraceReportingService {

	private static Log log = LogFactory.getLog(TraceReportingService.class.getName());
	
	@Autowired
	private ReportingRepo reportingRepo;
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	@RequestMapping(value="/summary-report" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<TransactionInfo>  getTransactionInfoList(@RequestParam(value="qrcode", required=false) String qrcode , @RequestParam(value="merchantId") String merchantId,
				@RequestParam(value="productName" , required=false) String productName , @RequestParam(value="lotNumber" , required=false) String lotNumber) throws OriginException{
		
		//BasicQuery query = new BasicQuery("{ qrCode :\""+ qrcode +"\" , merchantId : \" "+ qrcode +"\" }");// merchantId : { $gt : 1000.00 }
		//List<OriginData> result = operations.find(query, OriginData.class);
		if(StringUtils.isEmpty(merchantId)) {
			throw new OriginException("merchantId is mandatory, please provide the same");
		}
		List<TransactionInfo> result = reportingRepo.summaryView(qrcode, merchantId, productName, lotNumber);
		return result;
	}

	@RequestMapping(value="/generic_summary-report" , method = {RequestMethod.POST })
	public List<?>  getTransactionInfoList(@RequestBody Map<String, String> queryMap) throws OriginException{
		
		//BasicQuery query = new BasicQuery("{ qrCode :\""+ qrcode +"\" , merchantId : \" "+ qrcode +"\" }");// merchantId : { $gt : 1000.00 }
		//List<OriginData> result = operations.find(query, OriginData.class);
		if(StringUtils.isEmpty(queryMap.get("merchantId"))) {
			throw new OriginException("merchantId is mandatory, please provide the same");
		}
		List<?> result = mongoQueryMgr.executeQuery(queryMap, TransactionInfo.class);
		return result;
	}
	
	@RequestMapping(value="/detailed-report" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<TraceReportTo>  getDetailedTxReport(@RequestParam(value="qrcodes", required=false) List<String> qrcodes, @RequestParam(value="lotNumbers" , required=false) List<String> lotNumbers) throws OriginException{
		
		if(StringUtils.isEmpty(qrcodes) && StringUtils.isEmpty(lotNumbers)) {
			throw new OriginException("atleast one of the parameter qrcode or lotNumber must be supplied");
		}
		return reportingRepo.detailedReport(qrcodes, lotNumbers);
	}

	@RequestMapping(value="/detailed-report-query" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<TraceReportTo>  getDetailedTxReport(@RequestParam(value="qrcode", required=false) String qrcode , @RequestParam(value="merchantId") String merchantId,
				@RequestParam(value="productName" , required=false) String productName , @RequestParam(value="lotNumber" , required=false) String lotNumber) throws OriginException{
		
		//BasicQuery query = new BasicQuery("{ qrCode :\""+ qrcode +"\" , merchantId : \" "+ qrcode +"\" }");// merchantId : { $gt : 1000.00 }
		//List<OriginData> result = operations.find(query, OriginData.class);
		if(StringUtils.isEmpty(merchantId)) {
			throw new OriginException("merchantId is mandatory, please provide the same");
		}
		List<TraceReportTo> result = reportingRepo.detailedReport(qrcode, merchantId, productName, lotNumber);
		return result;
	}
	
}

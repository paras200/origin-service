package com.ilab.origin.validator.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.common.utils.DateUtils;
import com.ilab.origin.serial.SerialNumberGenerator;
import com.ilab.origin.serial.TimestampSerialization;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.OriginData;
import com.ilab.origin.validator.model.OriginStatus;
import com.ilab.origin.validator.model.OriginTrack;
import com.ilab.origin.validator.model.QRGenInputData;
import com.ilab.origin.validator.model.Result;
import com.ilab.origin.validator.repo.ValidatorRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/validator")
public class ValidationService {

	@Autowired
	private ValidatorRepo repository;
	
	@Autowired
	private OriginTrackRecorder originTrackRecorder;
	
	private SerialNumberGenerator slgenerator = TimestampSerialization.getInstance();
	
	@Autowired 
	private MongoOperations operations;
	
	private static Log log = LogFactory.getLog(ValidationService.class.getName());
	
	@PostMapping("/save-qrcode")	
	public OriginData saveQRCode(@RequestBody OriginData vData){		
		log.info(" saving QR code :" + vData);
		vData.setTimeinmilli(Calendar.getInstance().getTimeInMillis());
		return repository.save(vData);
	}
	
	@PostMapping("/save-all-codes")	
	public Result saveAllQRCode(@RequestBody List<OriginData> vDataList){		
		log.info(" saving QR code :" + vDataList);
		long timeinmilli = Calendar.getInstance().getTimeInMillis();
		for (OriginData originData : vDataList) {
			originData.setSold(false);
			originData.setTimeinmilli(timeinmilli);
		}
		repository.save(vDataList);
		return new Result();
	}
	
	@PostMapping("/generate-qrcode")	
	public List<OriginData> saveAllQRCode(@RequestBody QRGenInputData inputData) throws OriginException{		
		if(inputData.getOriginData()== null || inputData.getOriginData().getMerchantId() == null){
			throw new OriginException("Merchant Id can't bull");
		}
		
		List<OriginData> vDataList = new ArrayList<>();
		int count = inputData.getCount();
		List<String> serialNumberList = slgenerator.getSequenceNumber(count);
		for (int i =0 ; i < count; i++) {
			OriginData vd = new OriginData();
			
			String qrcode = generateQrcode(vd, serialNumberList.get(i));
			vd.setQrCode(qrcode);
			vDataList.add(vd);
		}
		log.info(" saving QR code :" + vDataList);
		vDataList = repository.save(vDataList);
		log.info("QR codes saved for : " + vDataList);
		return vDataList;
	}
	  
	private String generateQrcode(OriginData vd, String serialNum) {
		return vd.getGstn() +"-" + serialNum + "-" + vd.getLotNumber()+"-"+vd.getExpiryDate();
	}

	@RequestMapping(value="/get-by-qrcode" , method = { RequestMethod.GET, RequestMethod.POST })
	public OriginData getValidationData(@RequestParam(value="qrcode") String qrcode){
		return repository.findByQrCode(qrcode);
	}
	
	@RequestMapping(value="/query" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<OriginData>  getValidationData(@RequestParam(value="qrcode", required=false) String qrcode , @RequestParam(value="merchantId") String merchantId,
				@RequestParam(value="startDate" , required=false) String startDate , @RequestParam(value="endDate" , required=false) String endDate) throws OriginException{
		
		//BasicQuery query = new BasicQuery("{ qrCode :\""+ qrcode +"\" , merchantId : \" "+ qrcode +"\" }");// merchantId : { $gt : 1000.00 }
		//List<OriginData> result = operations.find(query, OriginData.class);
		if(StringUtils.isEmpty(merchantId)) {
			throw new OriginException("merchantId is mandatory, please provide the same");
		}
		Criteria criteria = null;
		if(!StringUtils.isEmpty(qrcode)) {
			criteria = Criteria.where("qrCode").is(qrcode);
		}
		if(criteria == null) {
			criteria = Criteria.where("merchantId").is(merchantId);
		}else {
			criteria = criteria.andOperator(Criteria.where("merchantId").is(merchantId));
		}
		if(StringUtils.isEmpty(endDate)) {
			endDate = startDate;
		}
		if(!StringUtils.isEmpty(startDate)) {
			try {
				criteria.andOperator(Criteria.where("timeinmilli").lt(DateUtils.convertEndDate(endDate)).gt(DateUtils.convertStartDate(startDate)));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new OriginException("date can't parsed , please chcek the format dd/MM/yyyy");
			}
		}
		
		Query query = new Query();
		query.addCriteria(criteria);
		List<OriginData>  result = operations.find(query, OriginData.class);
		log.info("result size retruned : " + result.size());
		return result;
	}
	
	@RequestMapping(value="/get-by-merchant" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<OriginData> getByMerchant(@RequestParam(value="merchantId") String merchantId){
		return repository.findByMerchantId(merchantId);
	}
	

	
	@RequestMapping(value="/mark-sold" , method = { RequestMethod.GET, RequestMethod.POST })
	public OriginData markSold(@RequestBody OriginTrack oTrack){
		OriginData vd = repository.findByQrCode(oTrack.getQrcode());
		if(vd == null) {
			// invalid product
			vd = new OriginData();
			vd.setStatusCode(OriginStatus.RED);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.RED));
			updateOrginTrack(oTrack , vd);
			originTrackRecorder.asyncSave(oTrack);
		}else if(!vd.isSold()) {
			// Valid product
			vd.setSold(true);
			vd.setLocation(oTrack.getLocation());
			vd.setFirstScanTime(new Date());
			vd.setLatestScanTime(vd.getFirstScanTime());
			vd.setLatestScanStatus(OriginStatus.GREEN);
			vd = repository.save(vd);
			vd.setStatusCode(OriginStatus.GREEN);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.GREEN));
			updateOrginTrack(oTrack , vd);
			originTrackRecorder.asyncSave(oTrack);
		}else {
			// already sold
			vd.setLatestScanTime(new Date());
			vd.setLatestScanStatus(OriginStatus.AMBER);
			vd = repository.save(vd);
			vd.setStatusCode(OriginStatus.AMBER);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.AMBER));
			updateOrginTrack(oTrack , vd);
			originTrackRecorder.asyncSave(oTrack);
		}
		return vd;
	}

	@RequestMapping(value="/get-scan-hist" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<OriginTrack> getScanHistory(@RequestParam(value="qrcode") String qrcode){
		return originTrackRecorder.getTrackHistory(qrcode);
	}
	
	private OriginTrack updateOrginTrack(OriginTrack oTrack, OriginData vd) {
		if(vd.getLatestScanTime() != null) {
			oTrack.setScanTime(vd.getLatestScanTime());
		}
		else {
			oTrack.setScanTime(new Date());
		}
		oTrack.setStatusCode(vd.getStatusCode());
		return oTrack;
	}
}
 
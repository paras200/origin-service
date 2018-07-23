package com.ilab.origin.validator.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.validator.model.OriginData;
import com.ilab.origin.validator.model.OriginStatus;
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
	private MerchantRepository merchantRepo;
	
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
	public List<OriginData> saveAllQRCode(@RequestBody QRGenInputData inputData){		
		if(inputData.getMerchantId() == null){
			//throw error
		}
		if(inputData.getMerchantKey() == null){
			Merchant merchant = merchantRepo.findById(inputData.getMerchantId());
			if(merchant != null){
				inputData.setMerchantKey(merchant.getMerchantKey());
			}
		}
		List<OriginData> vDataList = new ArrayList<>();
		UUID uniqueId = UUID.randomUUID();
		int cunt = inputData.getCount();
		for (int i =0 ; i < cunt; i++) {
			OriginData vd = new OriginData();
			String qrcode = inputData.getMerchantKey()+ "_" + uniqueId + "_" + i;
			vd.setQrCode(qrcode);
			vd.setProductName(inputData.getProductName());
			vd.setMerchantId(inputData.getMerchantId());
			vDataList.add(vd);
		}
		log.info(" saving QR code :" + vDataList);
		repository.save(vDataList);
		log.info("QR codes saved for : " + vDataList);
		return vDataList;
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
	public OriginData markSold(@RequestBody OriginData vData){
		OriginData vd = repository.findByQrCode(vData.getQrCode());
		if(vd == null) {
			// invalid product
			vd = new OriginData();
			vd.setStatusCode(OriginStatus.RED);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.RED));
		}else if(!vd.isSold()) {
			// Valid product
			vd.setSold(true);
			vd.setLocation(vData.getLocation());
			vd = repository.save(vd);
			vd.setStatusCode(OriginStatus.GREEN);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.GREEN));
		}else {
			// already sold
			vd.setStatusCode(OriginStatus.AMBER);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.AMBER));
		}
		
		return vd;
	}
}
 
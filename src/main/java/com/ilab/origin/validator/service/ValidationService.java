package com.ilab.origin.validator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/save-qrcode")	
	public OriginData saveQRCode(@RequestBody OriginData vData){		
		System.out.println(" saving QR code :" + vData);
		return repository.save(vData);
	}
	
	@PostMapping("/save-all-codes")	
	public Result saveAllQRCode(@RequestBody List<OriginData> vDataList){		
		System.out.println(" saving QR code :" + vDataList);
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
		System.out.println(" saving QR code :" + vDataList);
		repository.save(vDataList);
		System.out.println("QR codes saved for : " + vDataList);
		return vDataList;
	}
	  
	@RequestMapping(value="/get-by-qrcode" , method = { RequestMethod.GET, RequestMethod.POST })
	public OriginData getValidationData(@RequestParam(value="qrcode") String qrcode){
		return repository.findByQrCode(qrcode);
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
			vd.setStatus(OriginStatus.RED);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.RED));
		}else if(!vd.isSold()) {
			// Valid product
			vd.setSold(true);
			vd.setLocation(vData.getLocation());
			vd = repository.save(vd);
			vd.setStatus(OriginStatus.GREEN);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.GREEN));
		}else {
			// already sold
			vd.setStatus(OriginStatus.AMBER);
			vd.setMessage(OriginStatus.getStatusMessage(OriginStatus.AMBER));
		}
		
		return vd;
	}
}
 
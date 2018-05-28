package com.ilab.origin.validator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.validator.model.QRGenInputData;
import com.ilab.origin.validator.model.Result;
import com.ilab.origin.validator.model.ValidationData;
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
	public ValidationData saveQRCode(@RequestBody ValidationData vData){		
		System.out.println(" saving QR code :" + vData);
		return repository.save(vData);
	}
	
	@PostMapping("/save-all-codes")	
	public Result saveAllQRCode(@RequestBody List<ValidationData> vDataList){		
		System.out.println(" saving QR code :" + vDataList);
		repository.save(vDataList);
		return new Result();
	}
	
	@PostMapping("/generate-qrcode")	
	public List<ValidationData> saveAllQRCode(@RequestBody QRGenInputData inputData){		
		if(inputData.getMerchantId() == null){
			//throw error
		}
		if(inputData.getMerchantKey() == null){
			Merchant merchant = merchantRepo.findById(inputData.getMerchantId());
			if(merchant != null){
				inputData.setMerchantKey(merchant.getMerchantKey());
			}
		}
		List<ValidationData> vDataList = new ArrayList<>();
		UUID uniqueId = UUID.randomUUID();
		int cunt = inputData.getCount();
		for (int i =0 ; i < cunt; i++) {
			ValidationData vd = new ValidationData();
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
	  
	@RequestMapping("/get-by-qrcode")
	public ValidationData getValidationData(@RequestParam(value="qrcode") String qrcode){
		return repository.findByQrCode(qrcode);
	}
	
	@RequestMapping("/get-by-merchant")
	public List<ValidationData> getByMerchant(@RequestParam(value="merchantId") String merchantId){
		return repository.findByMerchantId(merchantId);
	}
	
	@RequestMapping("/mark-sold")
	public ValidationData markSold(@RequestParam(value="qrcode") String qrcode){
		ValidationData vd = repository.findByQrCode(qrcode);
		vd.setSold(ValidationData.SOLD);
		return repository.save(vd);
	}
}
 
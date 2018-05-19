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
import com.ilab.origin.validator.model.ValidationData;
import com.ilab.origin.validator.repo.ValidatorRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/validator")
public class ValidationService {

	@Autowired
	private ValidatorRepo repository;
	
	private MerchantRepository merchantRepo;
	
	@PostMapping("/saveQRCode")	
	public ValidationData saveQRCode(@RequestBody ValidationData vData){		
		System.out.println(" saving QR code :" + vData);
		return repository.save(vData);
	}
	
	@PostMapping("/saveAllCodes")	
	public void saveAllQRCode(@RequestBody List<ValidationData> vDataList){		
		System.out.println(" saving QR code :" + vDataList);
		repository.save(vDataList);
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
			vd.setQrcode(qrcode);
			vd.setProductName(inputData.getProductName());
			vd.setMerchantId(inputData.getMerchantId());
			vDataList.add(vd);
		}
		System.out.println(" saving QR code :" + vDataList);
		repository.save(vDataList);
		System.out.println("QR codes saved for : " + vDataList);
		return vDataList;
	}
	  
	@RequestMapping("/getByQrCode")
	public ValidationData getValidationData(@RequestParam(value="qrcode") String qrcode){
		return repository.findByQrcode(qrcode);
	}
	
	@RequestMapping("/markSold")
	public ValidationData markSold(@RequestParam(value="qrcode") String qrcode){
		ValidationData vd = repository.findByQrcode(qrcode);
		vd.setSold(ValidationData.SOLD);
		return repository.save(vd);
	}
}
 
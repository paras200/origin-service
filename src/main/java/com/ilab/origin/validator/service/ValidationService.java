package com.ilab.origin.validator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.validator.model.ValidationData;
import com.ilab.origin.validator.repo.ValidatorRepo;

@RestController
@RequestMapping(path="/validator")
public class ValidationService {

	@Autowired
	private ValidatorRepo repository;
	
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
	
	@RequestMapping("/getByQrCode")
	public ValidationData getValidationData(@RequestParam(value="qrcode") String qrcode){
		return repository.findByQrKey(qrcode);
	}
	
	@RequestMapping("/markSold")
	public ValidationData markSold(@RequestParam(value="qrcode") String qrcode){
		ValidationData vd = repository.findByQrKey(qrcode);
		vd.setSold(ValidationData.SOLD);
		return repository.save(vd);
	}
}

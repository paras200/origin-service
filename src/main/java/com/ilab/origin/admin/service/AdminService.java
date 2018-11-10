package com.ilab.origin.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.admin.processor.CertificatesTestDataGenerator;
import com.ilab.origin.admin.processor.TestDataGenerator;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.Result;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/admin")
public class AdminService {
	
	@Autowired
	private TestDataGenerator testDataGen;
	
	@Autowired
	private CertificatesTestDataGenerator certDataGenerator;


	@RequestMapping(value="/gen-test-data" , method = { RequestMethod.GET, RequestMethod.POST })
	public Result   getValidationData(@RequestParam(value="lotNumber") String lotNumber , @RequestParam(value="scanPercentage", required=false) Integer scanPercentage,
			@RequestParam(value="feedbackPercentage", required=false) Integer feedPercentage) throws OriginException{
		
		if(feedPercentage == null) feedPercentage = 10;
		if(scanPercentage == null) scanPercentage = 80;
		
		testDataGen.generateTestData(lotNumber, scanPercentage, feedPercentage);
		return new Result();
	}
	
	@RequestMapping(value="/gen-certificates-data" , method = { RequestMethod.GET, RequestMethod.POST })
	public Result   getValidationData(@RequestParam(value="institutesName") String institutesName , @RequestParam(value="userScanPercentage", required=false) Integer userScanPercentage,
			@RequestParam(value="businessScanPercentage", required=false) Integer businessScanPercentage,
			@RequestParam(value="generateQR", required=false , defaultValue="false") boolean generateQR,
			@RequestParam(value="merchantId") String merchantId) throws OriginException{
		
		if(userScanPercentage == null) userScanPercentage = 150;
		if(businessScanPercentage == null) businessScanPercentage = 80;
		
		certDataGenerator.generateTestData(institutesName, userScanPercentage, businessScanPercentage, generateQR, merchantId);
		return new Result();
	}
}

package com.ilab.origin.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.admin.processor.CertificatesTestDataGenerator;
import com.ilab.origin.admin.processor.TestDataGenerator;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.tracker.model.TransactionInfo;
import com.ilab.origin.tracker.service.TrackingService;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.service.MerchantService;
import com.ilab.origin.usermgt.service.UserService;
import com.ilab.origin.validator.model.Result;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/admin")
public class AdminService {
	
	@Autowired
	private TestDataGenerator testDataGen;
	
	@Autowired
	private CertificatesTestDataGenerator certDataGenerator;
	
	@Autowired
	private UserService userService;
	@Autowired
	private TrackingService trackingService;
	
	@Autowired
	private MerchantService merchantService;


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
	
	@RequestMapping(value="/add-test-users" , method = { RequestMethod.GET, RequestMethod.POST })
	public Result   addTestUsers() throws OriginException{
		
		certDataGenerator.addTestUser();
		return new Result();
	}
	
	//@RequestBody User user
	@RequestMapping(value = "/users" , method = { RequestMethod.GET})
	public List<User>   getAllUsers() throws OriginException{
		return userService.getAllUsers();
	}
	
	@RequestMapping(value = "/qrcodes" , method = { RequestMethod.GET})
	public List<TransactionInfo> getQRCodeDetails(@RequestParam(value="merchantId", required=true) String merchantId) throws OriginException{
		return trackingService.getallQRData(merchantId);
	}
	
	@RequestMapping(value = "/merchants" , method = { RequestMethod.GET})
	public List<Merchant>   getAllMerchant() throws OriginException{
		return merchantService.findAllMerchants();
	}
}

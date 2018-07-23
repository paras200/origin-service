package com.ilab.origin.feedback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.feedback.mode.FeedBackData;
import com.ilab.origin.feedback.repo.FeedbackRepo;
import com.ilab.origin.validator.model.Result;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/feedback")
public class FeedbackService {

	@Autowired
	private FeedbackRepo fdRepo;
	
	@PostMapping("/save")	
	public Result saveUserFeedback(@RequestBody FeedBackData fData){		
		System.out.println(" saving user feedback :" + fData);
		fData = fdRepo.save(fData);
		Result rs = new Result();
		rs.setIsSuccess(true);
		rs.setMessage("Thanks for your feedback!!!");
		return rs;
	}
	
	@RequestMapping(value="/get-by-qrcode" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<FeedBackData> getFeedbackData(@RequestParam(value="qrcode") String qrcode){
		return fdRepo.findByQrCode(qrcode);
	}
	
	@RequestMapping(value="/get-by-merchant" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<FeedBackData> getFeedbackByMerchant(@RequestParam(value="merchantId") String merchantId){
		return fdRepo.findByMerchantId(merchantId);
	}
	@RequestMapping(value="/get-by-user" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<FeedBackData> getFeedbackByUserId(@RequestParam(value="userId") String userId){
		return fdRepo.findByUserId(userId);
	}
}

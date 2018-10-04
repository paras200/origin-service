package com.ilab.origin.feedback.service;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.feedback.mode.ContactInfo;
import com.ilab.origin.feedback.repo.ContactInfoRepo;
import com.ilab.origin.validator.model.Result;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/contact-us")
public class ContactInfoService {
	
	private ContactInfoRepo contactInfoRepo;
	
	@PostMapping("/save")	
	public Result saveContactInfo(@RequestBody ContactInfo info){		
		System.out.println(" saving user contact info :" + info);
		info = contactInfoRepo.save(info);
		Result rs = new Result();
		rs.setIsSuccess(true);
		rs.setMessage("Thanks for your enquiry, we will get back to you !!!");
		return rs;
	}
}

package com.ilab.origin.usermgt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.repo.MerchantRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/merchant")
public class MerchantService {

	@Autowired
	private MerchantRepository repository;
	
	@PostMapping("/save")	
	public Merchant saveUser(@RequestBody Merchant user){		
		System.out.println(" saving user :" + user);
		return repository.save(user);
	}
	
	@GetMapping("/get-merchant")	
	public Merchant findMerchantByKey(@RequestParam(value="merchantKey") String merchantKey){		
		System.out.println(" retreive merchant detail for :" + merchantKey);
		return repository.findByMerchantKey(merchantKey);
	}
	
	@GetMapping("/get-merchantById")	
	public Merchant findMerchantById(@RequestParam(value="merchantId") String merchantId){		
		System.out.println(" retreive merchant detail for :" + merchantId);
		return repository.findById(merchantId);
	}
}

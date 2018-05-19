package com.ilab.origin.usermgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.QRTemplates;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.usermgt.repo.TemplateRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/merchant")
public class MerchantService {

	@Autowired
	private MerchantRepository repository;
	
	@Autowired
	private TemplateRepository templateRepo;
	
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
	
	@PostMapping("/save-templates")	
	public QRTemplates saveTemplates(@RequestBody QRTemplates templates){		
		System.out.println(" saving template :" + templates);
		return templateRepo.save(templates);
	}
	
	@GetMapping("/get-templates")	
	public List<QRTemplates> findTemplatesByMerchantId(@RequestParam(value="merchantId") String merchantId){		
		System.out.println(" retreive template detail for :" + merchantId);
		return templateRepo.findByMerchantId(merchantId);
	}
}

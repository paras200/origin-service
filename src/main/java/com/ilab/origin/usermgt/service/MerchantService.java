package com.ilab.origin.usermgt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private static Log log = LogFactory.getLog(MerchantService.class.getName());
	
	@Autowired
	private MerchantRepository repository;
	
	@Autowired
	private TemplateRepository templateRepo;
	
	@PostMapping("/save")	
	public Merchant saveMerchant(@RequestBody Merchant user){		
		log.info(" saving merchant :" + user);
		return repository.save(user);
	}
	
	@GetMapping("/get-merchant")	
	public Merchant findMerchantByKey(@RequestParam(value="merchantKey") String merchantKey){		
		log.info(" retreive merchant detail for :" + merchantKey);
		return repository.findByMerchantKey(merchantKey);
	}
	
	@GetMapping("/get-merchant-by-id")	
	public Merchant findMerchantById(@RequestParam(value="merchantId") String merchantId){		
		log.info(" retreive merchant detail for :" + merchantId);
		return repository.findById(merchantId);
	}
	
	@GetMapping("/get-all-merchant-keys")	
	public List<String> findAllMerchantKeys(){		

		List<Merchant> mrList = repository.findAll();  // TODO move the querey to selected fields
		List<String> mkeyList = mrList.stream().map(Merchant::getMerchantKey).collect(Collectors.toList());

		return mkeyList;
	}
	
	@GetMapping("/is-merchant-key-availble")	
	public boolean checkIfExist(@RequestParam(value="merchantKey") String merchantKey){		
		log.info(" retreive merchant detail for :" + merchantKey);
		Merchant mr = repository.findByMerchantKey(merchantKey);
		if(mr == null) return true;
		return false;
	}
	
	@PostMapping("/save-templates")	
	public QRTemplates saveTemplates(@RequestBody QRTemplates templates){		
		log.info(" saving template :" + templates);
		return templateRepo.save(templates);
	}
	
	@GetMapping("/get-templates")	
	public List<QRTemplates> findTemplatesByMerchantId(@RequestParam(value="merchantId") String merchantId){		
		log.info(" retreive template detail for :" + merchantId);
		return templateRepo.findByMerchantId(merchantId);
	}

}

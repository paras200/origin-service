package com.ilab.origin.usermgt.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.UserAudit;
import com.ilab.origin.usermgt.repo.AuditRepository;

@RestController
@CrossOrigin(origins = "*")
public class AuditService {

	private static Log log = LogFactory.getLog(AuditService.class.getName());
	
	@Autowired
    AuditRepository repository;
	
	@GetMapping("/audit/userid")
	public List<UserAudit> getUserAuditInfo(@RequestParam(value="userId") String userId){
		return repository.findByUserId(userId);
	}
	
	@PostMapping("/audit/save")	
	public UserAudit saveUser(@RequestBody UserAudit auditLog){		
		log.info(" saving auditLog : " + auditLog);
		return repository.save(auditLog);
	}
}

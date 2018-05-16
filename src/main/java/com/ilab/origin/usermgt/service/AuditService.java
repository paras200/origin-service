package com.ilab.origin.usermgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.UserAudit;
import com.ilab.origin.usermgt.repo.AuditRepository;

@RestController
@CrossOrigin(origins = "*")
public class AuditService {

	@Autowired
    AuditRepository repository;
	
	@RequestMapping("/audit/userid")
	public List<UserAudit> getUserAuditInfo(@RequestParam(value="userId") String userId){
		return repository.findByUserId(userId);
	}
	
	@PostMapping("/audit/save")	
	public UserAudit saveUser(@RequestBody UserAudit auditLog){		
		System.out.println(" saving auditLog : " + auditLog);
		return repository.save(auditLog);
	}
}

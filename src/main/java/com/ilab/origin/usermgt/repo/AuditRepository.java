package com.ilab.origin.usermgt.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.usermgt.model.UserAudit;

public interface AuditRepository extends MongoRepository<UserAudit, String> {

	List<UserAudit> findByUserId(String userId);

}

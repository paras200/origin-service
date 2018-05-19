package com.ilab.origin.usermgt.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.usermgt.model.QRTemplates;

public interface TemplateRepository extends MongoRepository<QRTemplates, String> {
	public List<QRTemplates> findByMerchantId(String merchantId);
}

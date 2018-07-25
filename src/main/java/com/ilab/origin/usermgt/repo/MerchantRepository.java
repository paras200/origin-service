package com.ilab.origin.usermgt.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.usermgt.model.Merchant;

public interface MerchantRepository extends MongoRepository<Merchant, String> {

	public Merchant findByMerchantKey(String merchantKey);
	
	public Merchant findById(String id);
	
}

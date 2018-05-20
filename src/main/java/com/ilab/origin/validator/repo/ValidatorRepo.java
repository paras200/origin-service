package com.ilab.origin.validator.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.validator.model.ValidationData;

public interface ValidatorRepo extends MongoRepository<ValidationData, String> {

	public ValidationData findByQrCode(String qrcode);
	
	public List<ValidationData> findByMerchantId(String qrcode);
		
}

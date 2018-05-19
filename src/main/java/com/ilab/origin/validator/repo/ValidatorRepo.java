package com.ilab.origin.validator.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.validator.model.ValidationData;

public interface ValidatorRepo extends MongoRepository<ValidationData, String> {

	public ValidationData findByQrcode(String qrcode);
		
}

package com.ilab.origin.validator.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.validator.model.OriginData;

public interface ValidatorRepo extends MongoRepository<OriginData, String> {

	public OriginData findByQrCode(String qrcode);
	
	public List<OriginData> findByMerchantId(String merchantId);
		
}

package com.ilab.origin.feedback.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.feedback.mode.FeedBackData;

public interface FeedbackRepo extends MongoRepository<FeedBackData, String> {

    public List<FeedBackData> findByQrCode(String qrcode);
	
	public List<FeedBackData> findByMerchantId(String merchantId);
	
	public List<FeedBackData> findByUserId(String userId);
}

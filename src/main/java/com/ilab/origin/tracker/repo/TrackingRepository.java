package com.ilab.origin.tracker.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.tracker.model.TrackingData;

public interface TrackingRepository extends MongoRepository<TrackingData, String> {

	public TrackingData findByQrCode(String qrcode);
	
	public List<TrackingData> findByMerchantId(String merchantId);
	
	public List<TrackingData> findBytrackingCode(String trackingCode);
}

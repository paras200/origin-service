package com.ilab.origin.tracker.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.tracker.model.TrackingData;

public interface TrackingDataRepository extends MongoRepository<TrackingData, String> {

	List<TrackingData> findByQrcode(String qrcode);

}

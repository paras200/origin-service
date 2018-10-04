package com.ilab.origin.validator.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.validator.model.OriginTrack;

public interface OriginTrackRepository extends MongoRepository<OriginTrack, String> {

	public List<OriginTrack> findByQrcode(String qrcode);
	
	public List<OriginTrack> findByUserIdOrderByScanTimeDesc(String userId);

}

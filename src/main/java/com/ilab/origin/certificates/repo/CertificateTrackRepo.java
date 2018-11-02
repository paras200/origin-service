package com.ilab.origin.certificates.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.certificates.model.CertificateTrack;

public interface CertificateTrackRepo extends MongoRepository<CertificateTrack, String> {

	List<CertificateTrack> findByQrcode(String qrcode);
	
}

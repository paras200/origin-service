package com.ilab.origin.certificates.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.certificates.model.Certificates;

public interface CertificatesRepo extends MongoRepository<Certificates, String> {

	public Certificates findByQrCode(String qrcode);
}

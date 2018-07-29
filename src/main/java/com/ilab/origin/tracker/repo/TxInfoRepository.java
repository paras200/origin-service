package com.ilab.origin.tracker.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.tracker.model.TransactionInfo;

public interface TxInfoRepository extends MongoRepository<TransactionInfo, String> {

	public TransactionInfo findByQrcode(String qrcode);
	
	public List<TransactionInfo> findByMerchantId(String merchantId);
	
	public List<TransactionInfo> findBytrackingCode(String trackingCode);
	
	public List<TransactionInfo> findByLotNumber(String lotNumber);
}

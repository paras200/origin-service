package com.ilab.origin.certificates.service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilab.origin.certificates.model.CertificateTrack;
import com.ilab.origin.certificates.repo.CertificateTrackRepo;
import com.ilab.origin.validator.model.OriginTrack;

@Service
@Scope(scopeName="singleton")
public class CertificateTrackRecorder {

	private static Log log = LogFactory.getLog(CertificateTrackRecorder.class.getName());
	private BlockingQueue<CertificateTrack> queue = new ArrayBlockingQueue<>(100);
	
	@Autowired
	private CertificateTrackRepo certTrackRepo;
	
	@PostConstruct
	public void init(){
		for(int i=1 ; i<=2 ; i++){
			CertificatesScanRecorderThread oTrackThread = new CertificatesScanRecorderThread(queue,certTrackRepo);
			oTrackThread.start();
		}			
	}
	
	public void asyncSave(CertificateTrack certificateTrack) {
		try {
			queue.put(certificateTrack);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			log.error("Error saving certificates tracking records...");
			// TODO send notifications
		}
	}
	
	public void asyncSave(OriginTrack oTrack) {
		CertificateTrack certificateTrack = getCertificatesTrackingInfo(oTrack);
		asyncSave(certificateTrack);
	}

	private CertificateTrack getCertificatesTrackingInfo(OriginTrack oTrack) {
		// TODO Auto-generated method stub
		return null;
	}

	public CertificateTrack save(CertificateTrack originTrack) {
		return certTrackRepo.save(originTrack);
	}
	
	public List<CertificateTrack> getTrackHistory(String qrcode){
		return certTrackRepo.findByQrcode(qrcode);
	}

}

package com.ilab.origin.certificates.service;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ilab.origin.certificates.model.CertificateTrack;
import com.ilab.origin.certificates.repo.CertificateTrackRepo;

public class CertificatesScanRecorderThread  extends Thread{


	private static Log log = LogFactory.getLog(CertificatesScanRecorderThread.class.getName());
	
	private BlockingQueue<CertificateTrack> queue;
	private CertificateTrackRepo certTrackRepo;
	
	public CertificatesScanRecorderThread(BlockingQueue<CertificateTrack> queue,CertificateTrackRepo certTrackRepo) {
		this.queue = queue;
		this.certTrackRepo = certTrackRepo;
	}
	
	@Override
	public void run() {
		log.info("CertificatesScanRecorderThread ... started : " + currentThread().getName());
		while(true){
			CertificateTrack oTrack = null;
			try {
				 oTrack = queue.take();
				log.info("Saving " + oTrack);
				certTrackRepo.save(oTrack);
				log.info("waitig for next scan...");
			}catch(Exception ex) {
				log.error("error saving origin track : " + oTrack, ex);
				// TODO send email notification 
			}
		}
	}


}

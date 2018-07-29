package com.ilab.origin.validator.service;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ilab.origin.validator.model.OriginTrack;
import com.ilab.origin.validator.repo.OriginTrackRepository;

public class OriginScanRecorderThread  extends Thread{

	private static Log log = LogFactory.getLog(OriginScanRecorderThread.class.getName());
	
	private BlockingQueue<OriginTrack> queue;
	private OriginTrackRepository originTrackRepo;
	
	public OriginScanRecorderThread(BlockingQueue<OriginTrack> queue,OriginTrackRepository originTrackRepo) {
		this.queue = queue;
		this.originTrackRepo = originTrackRepo;
	}
	
	@Override
	public void run() {
		log.info("OriginScanRecorderThread ... started : " + currentThread().getName());
		while(true){
			OriginTrack oTrack = null;
			try {
				 oTrack = queue.take();
				log.info("Saving " + oTrack);
				originTrackRepo.save(oTrack);
				log.info("waitig for next scan...");
			}catch(Exception ex) {
				log.error("error saving origin track : " + oTrack, ex);
				// TODO send email notification 
			}
		}
	}
}

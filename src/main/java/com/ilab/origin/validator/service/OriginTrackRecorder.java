package com.ilab.origin.validator.service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilab.origin.validator.model.OriginTrack;
import com.ilab.origin.validator.repo.OriginTrackRepository;

@Service
@Scope(scopeName="singleton")
public class OriginTrackRecorder {
	
	private static Log log = LogFactory.getLog(OriginTrackRecorder.class.getName());
	private BlockingQueue<OriginTrack> queue = new ArrayBlockingQueue<>(100);
	
	@Autowired
	private OriginTrackRepository originTrackRepo;
	
	@PostConstruct
	public void init(){
		for(int i=1 ; i<=10 ; i++){
			OriginScanRecorderThread oTrackThread = new OriginScanRecorderThread(queue,originTrackRepo);
			oTrackThread.start();
		}			
	}
	
	public void asyncSave(OriginTrack originTrack) {
		try {
			queue.put(originTrack);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			log.error("Error saving tracking records...");
			// TODO send notifications
		}
	}

	public OriginTrack save(OriginTrack originTrack) {
		return originTrackRepo.save(originTrack);
	}
	
	public List<OriginTrack> getTrackHistory(String qrcode){
		return originTrackRepo.findByQrcode(qrcode);
	}
}

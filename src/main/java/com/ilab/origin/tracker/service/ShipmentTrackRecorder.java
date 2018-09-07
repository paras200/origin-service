package com.ilab.origin.tracker.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.repo.TrackingDataRepository;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.usermgt.repo.UserRepository;

@Service
@Scope(scopeName="singleton")
public class ShipmentTrackRecorder {
	
	private static Log log = LogFactory.getLog(ShipmentTrackRecorder.class.getName());
	private BlockingQueue<TrackingData> trackerQueue = new ArrayBlockingQueue<>(100);
	
	@Autowired
	private TrackingDataRepository trackingDataRepo;
	
	@Autowired
    private UserRepository userRepo;
	
	@Autowired
	private MerchantRepository merchantRepo;
	
	@PostConstruct
	public void init(){
		for(int i=1 ; i<=5 ; i++){
			ShipmentTrackRecorderThread oTrackThread = new ShipmentTrackRecorderThread(trackerQueue,trackingDataRepo, userRepo,merchantRepo);
			oTrackThread.start();
		}			
	}
	
	public void asyncUpdate(TrackingData trackingData) {
		try {
			trackerQueue.put(trackingData);
			
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			log.error("Error saving tracking records...");
			// TODO send notifications
		}
	}

}

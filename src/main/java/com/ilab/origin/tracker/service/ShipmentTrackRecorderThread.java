package com.ilab.origin.tracker.service;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ilab.origin.notification.fcm.NotificationService;
import com.ilab.origin.tracker.model.ProductOwner;
import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.repo.TrackingDataRepository;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.usermgt.repo.UserRepository;

public class ShipmentTrackRecorderThread extends Thread{


	private static Log log = LogFactory.getLog(ShipmentTrackRecorderThread.class.getName());
	
	private BlockingQueue<TrackingData> queue;
	private TrackingDataRepository trackingDataRepo;
	
    private UserRepository userRepo;
    private MerchantRepository merchantRepo;
    private NotificationService notificationService;
	
	public ShipmentTrackRecorderThread(BlockingQueue<TrackingData> queue,TrackingDataRepository trackingDataRepo , UserRepository userRepo,  MerchantRepository merchantRepo, NotificationService notificationService) {
		this.queue = queue;
		this.trackingDataRepo = trackingDataRepo;
		this.userRepo = userRepo;
		this.merchantRepo = merchantRepo;
		this.notificationService = notificationService;
	}
	
	@Override
	public void run() {
		log.info("ShipmentTrackRecorderThread ... started : " + currentThread().getName());
		while(true){
			TrackingData shipTrack = null;
			try {
				 shipTrack = queue.take();
				log.info("Saving " + shipTrack);
				if(shipTrack.getUserId() != null) {
					User user = userRepo.findByUserId(shipTrack.getUserId());
					if(user != null) {
						ProductOwner owner = new ProductOwner();
						owner.setPersonName(user.getUserName());
						shipTrack.setMerchantId(user.getMerchantId());
						if(user.getMerchantId() != null) {
							Merchant merchant =  merchantRepo.findById(user.getMerchantId());
							owner.setBusinessName(merchant.getName());
						}
						shipTrack.setOwner(owner);
						// update
						trackingDataRepo.save(shipTrack);
						notificationService.sendTrackingUpdate(shipTrack);
					}
				}
				
				log.info("waitig for next scan...");
			}catch(Exception ex) {
				log.error("error saving origin track : " + shipTrack, ex);
				// TODO send email notification 
			}
		}
	}


}

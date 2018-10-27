package com.ilab.origin.notification.fcm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ilab.origin.tracker.model.TrackingData;

@Service
@Scope(scopeName="singleton")
public class FirebaseNotification implements NotificationService {

	private static Log log = LogFactory.getLog(FirebaseNotification.class.getName());
	
	@Value("${firebase.security.config}")
	private String firebaseSecurityConfig;
	
	
	public String getFirebaseSecurityConfig() {
		return firebaseSecurityConfig;
	}

	public void setFirebaseSecurityConfig(String firebaseSecurityConfig) {
		this.firebaseSecurityConfig = firebaseSecurityConfig;
	}

	@PostConstruct
	public void init() {
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream(firebaseSecurityConfig);

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://originscan-messaging.firebaseio.com/")
					.build();

			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			log.error("Firebase security validation failed",e);
		} catch (IOException e) {
			log.error("Firebase security validation failed",e);
		}
	}
	
	public void sendTrackingUpdate(TrackingData trackingData) {
		// The topic name can be optionally prefixed with "/topics/".
		String topic = "shipment-" + trackingData.getMerchantId();

		Notification notification = new Notification("Shipment Update", "Shipment with LotNumber : " + trackingData.getLotNumber() + "  is received by " + trackingData.getOwner().getPersonName());
		// See documentation on defining a message payload.
		Message message = Message.builder()
		    .putData("LotNumber", trackingData.getLotNumber())
		    .putData("Owner Name", trackingData.getOwner().getPersonName())
		    .putData("Merchant Name", trackingData.getMerchantName())
		    .setNotification(notification)
		    .setTopic(topic)
		    .build();
//		 Send a message in the dry run mode.lp/yu	1qy2trg  mb/
		boolean dryRun = true;


		// Send a message to the devices subscribed to the provided topic.
		String response;
		try {
			//response = FirebaseMessaging.getInstance().send(message, dryRun);
			response = FirebaseMessaging.getInstance().send(message);
			// Response is a message ID string.
			log.info("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			log.error("firebase message sending failed for " + trackingData , e);
		}
		
	}
}

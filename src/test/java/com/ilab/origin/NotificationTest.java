package com.ilab.origin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.notification.fcm.FirebaseNotification;
import com.ilab.origin.tracker.model.ProductOwner;
import com.ilab.origin.tracker.model.TrackingData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationTest {

	@Autowired
	private FirebaseNotification notification;
	
	@Test
	public void testNotification()  {
		TrackingData trackingData = new TrackingData();
		trackingData.setMerchantId("10000");
		trackingData.setMerchantName("Durga Ghee");
		trackingData.setLotNumber("TestLot100");
		ProductOwner po = new ProductOwner();
		po.setPersonName("Anil Sinha");
		trackingData.setOwner(po);
		notification.sendTrackingUpdate(trackingData);
	}
}

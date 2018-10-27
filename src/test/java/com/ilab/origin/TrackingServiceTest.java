package com.ilab.origin;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.cloud.firestore.Transaction;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.model.TransactionInfo;
import com.ilab.origin.tracker.service.TrackingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackingServiceTest {

	@Autowired
	private TrackingService trackingService;
	
	
	@Test
	public void testShipmentTrack() throws OriginException {
		String merchantId = "10000";
		TrackingData td = new TrackingData();
		td.setQrcode("LOT1");
		td.setMerchantId(merchantId);
		
		TransactionInfo ti =  trackingService.saveTrackingData(td);
		System.out.println("ti : " + ti);
		
	}
	
	@Test
	public void testScanHistory() throws OriginException {
		String merchantId = "10000";
		List<TrackingData> results =  trackingService.getLatestShipmentScanData(merchantId);
		System.out.println("Size : " + results.size());
		for (TrackingData trackingData : results) {
			System.out.println(trackingData);
		}
	}
}

package com.ilab.origin.admin.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.feedback.mode.FeedBackData;
import com.ilab.origin.feedback.service.FeedbackService;
import com.ilab.origin.usermgt.model.Location;
import com.ilab.origin.validator.model.OriginData;
import com.ilab.origin.validator.model.OriginTrack;
import com.ilab.origin.validator.service.ValidationService;

@Component
public class TestDataGenerator {

	private static Log log = LogFactory.getLog(TestDataGenerator.class.getName());
	
	private List<String> locationList = new ArrayList<>();
	private List<String> userList = new ArrayList<>();
	
	@Autowired
	private ValidationService validationSrvc; 
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	@Autowired
	private FeedbackService feedBkService;
	
	@PostConstruct
	public void init() {
		locationList.add("Patna");
		locationList.add("Hyde");
		locationList.add("Banglore");
		locationList.add("Pune");
		locationList.add("Delhi");
		locationList.add("Punjab");
		locationList.add("Kolkatta");
		locationList.add("Mumbai");
		
		
		////////// userList
		userList.add("sinhanil@gmail.com, Anil Sinha");
		userList.add("pradeep@gmail.com, Pradeep Sambha");
		userList.add("bhushan@gmail.com, Bhushan Pawar");
		userList.add("paras@gmail.com, Paras Agni");
		userList.add("ayan@gmail.com, Ayan Sinha");
	}
	
	public void generateTestData(String lotNum, Integer scanPercentage , Integer feedback) {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("lotNumber", lotNum);
		
		Query query = mongoQueryMgr.createQuery(queryMap);
		query.fields().include("qrCode");
		query.fields().include("latestScanStatus");
		List<?> results =  mongoQueryMgr.executeQuery(queryMap, OriginData.class);	
		log.info("result size retruned : " + results.size());
		
		List<OriginData> updatedResult = new ArrayList<>();
		for (Object resObj : results) {
			OriginData od = (OriginData) resObj;
			updatedResult.add(od);
		}
		
		int total = updatedResult.size();
		int scancount = (total * scanPercentage)/100 ;
		int feedbackCount = (feedback * scanPercentage)/100 ;
		
		// add scan
		addScan( scancount, updatedResult, feedbackCount);

		log.info("Test data generated for lot : " + lotNum);
	}

	private void addScan(int scancount, List<OriginData> updatedResult, int feedbackCount) {
		for (int i = 0; i < scancount; i++) {
			OriginTrack oTrack = new OriginTrack();
			oTrack.setQrcode(updatedResult.get(i).getQrCode());
			oTrack.setLocation(getRandomLocation());
			validationSrvc.markSold(oTrack);
			
			// add feedback
			if(i < feedbackCount) {
				addFeedback(updatedResult.get(i).getQrCode());
			}
		}		
	}

	private void addFeedback(String qrCode) {
		FeedBackData fd = new FeedBackData();
		fd.setQrCode(qrCode);
		setRandomUser(fd);
		feedBkService.saveUserFeedback(fd);
	}

	private Location getRandomLocation() {
		int index = getRandomNumberInRange(0, locationList.size()-1);	
		Location loc = new Location();
		loc.setAddress(locationList.get(index));
		return loc;
	}
	
	private void setRandomUser(FeedBackData fd) {
		String userDetails = userList.get(getRandomNumberInRange(0, userList.size()-1));
		String[] userDArr = userDetails.split(",");
		fd.setUserId(userDArr[0]);
		fd.setUserName(userDArr[1]);
	}
	
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}

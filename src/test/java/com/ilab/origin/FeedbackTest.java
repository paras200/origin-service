package com.ilab.origin;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.feedback.mode.FeedBackData;
import com.ilab.origin.feedback.repo.FeedbackRepo;
import com.ilab.origin.validator.model.OriginData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackTest {
	
	@Autowired
	private FeedbackRepo feedRepo;
	
	@Autowired 
	private MongoOperations operations;
	
	@Test
    public void testFeedBack() {
		FeedBackData fd = new FeedBackData();
		fd.setQrCode("key-1");
		fd.setMessage("test msg 1");
		fd.setUserId("aks");
		
		feedRepo.save(fd);
		
		fd = new FeedBackData();
		fd.setQrCode("key-2");
		fd.setMessage("test msg 2");
		fd.setUserId("nish");
		feedRepo.save(fd);
		
		//Criteria cr = Criteria.where("qrCode").in(qrcode);
		List<String> qList = new ArrayList<String>();
		qList.add("key-1");
		qList.add("key-2");
		//List<FeedBackData> fdDataList =  feedRepo.findByQrCode(qList);
	//	System.out.println(fdDataList);
		
		Criteria cr = Criteria.where("qrCode").in(qList);

		Query query = new Query();
		query.addCriteria(cr);
		List<FeedBackData>  result = operations.find(query, FeedBackData.class);
		System.out.println(result);
    	
    }

}

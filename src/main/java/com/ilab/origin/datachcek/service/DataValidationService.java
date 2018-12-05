package com.ilab.origin.datachcek.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.jws.soap.SOAPBinding.Use;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.User;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/data-validator")
public class DataValidationService {
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	private static Log log = LogFactory.getLog(DataValidationService.class.getName());

	@GetMapping("/merchant-unique-data")	
	public Map<String, List<String>> getMerchantUniqueDataList(){
		
		Query query = new Query();
		query.fields().include("merchantKey");
		query.fields().include("name");
		query.fields().include("companyPANNumber");
		List<Merchant> results = (List<Merchant>) mongoQueryMgr.executeQuery(Merchant.class , query);	
		log.info("result size retruned : " + results.size());
		
		List<String> mKeyList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();
		List<String> panList =new ArrayList<>();
		
		for (Merchant merchant : results) {
			mKeyList.add(merchant.getMerchantKey());
			nameList.add(merchant.getName());
			panList.add(merchant.getCompanyPANNumber());
		}
		
		Map<String, List<String>> resultMap = new HashMap<>();
		resultMap.put("merchantKey", mKeyList);
		resultMap.put("name", nameList);
		resultMap.put("companyPANNumber", panList);
		
		return resultMap;
	}
	
	@GetMapping("/unique-user-id")	
	public List<String> uniqueUserList(){
		
		Query query = new Query();
		query.fields().include("userId");
		List<User> results = (List<User>) mongoQueryMgr.executeQuery(User.class , query);	
		return results.stream().map(User::getUserId).collect(Collectors.toList());
	}
}

package com.ilab.origin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.common.model.QueryParamTO;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.OriginData;
import com.ilab.origin.validator.service.ValidationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {
	
	@Autowired
	private ValidationService validationService;

	@Test
	public void qrReportTest() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "1000");
		queryMap.put("productName", "Ghee");
		QueryParamTO paramTo = new QueryParamTO();
		paramTo.setQueryMap(queryMap);
		paramTo.setPageNum(1);
		paramTo.setPageSize(2);
		List<OriginData> orList = validationService.getValidationData(paramTo);
		System.out.println("size : " + orList.size());
		for (OriginData originData : orList) {
			System.out.println(originData.getQrCode() + "- " + originData.getTimeinmilli());
		}
	}
	
	@Test
	public void qrAnalyticTest() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "1000");
		Map<String,String> resMap  = validationService.generateQrAnalytics(queryMap);
		System.out.println("size : " + resMap);
		
	}
	
	@Test
	public void locationAnalyticTest() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "1000");
		//queryMap.put("productName", "Ghee");
		Map<String,String> resMap  = validationService.locationAnalytics(queryMap);
		System.out.println("locationAnalyticTest : " + resMap);
		
	}
}

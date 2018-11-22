package com.ilab.origin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.certificates.service.CertificatesAnalyticsService;
import com.ilab.origin.certificates.service.CertificatesValidationService;
import com.ilab.origin.certificates.to.CertificatesTO;
import com.ilab.origin.certificates.to.ReportResult;
import com.ilab.origin.certificates.to.Student;
import com.ilab.origin.common.model.QueryParamTO;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.OriginTrack;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificatesReportTest {

	@Autowired
	private CertificatesAnalyticsService analyticsService;
	
	@Autowired
	private CertificatesValidationService certValidationService;
	
	private static List<String> qrlist = new ArrayList<>();
	
	@Test
	public void saveCertificates() throws OriginException {
		CertificatesTO bulkCertificatesTO = new CertificatesTO();
		bulkCertificatesTO.setCourseName("Electrical");
		bulkCertificatesTO.setInstituteName("IIT Kgp");
		bulkCertificatesTO.setMerchantId("2000");
		bulkCertificatesTO.setUniversityName("IIT");
		List<Student> students = new ArrayList<>();
		Student s1 = new Student();
		s1.setStudentName("Raj Singh");
		Calendar cal = Calendar.getInstance();
		cal.set(2001, 10, 01);
		s1.setDateOfBirth("01/10/2001");
		students.add(s1);
		Student s2 = new Student();
		s2.setStudentName("Aks Singh");
		s2.setDateOfBirth("01/05/2005");
		students.add(s2);
		bulkCertificatesTO.setStudents(students);
		List<Certificates> cList = certValidationService.saveAllQRCode(bulkCertificatesTO);
		System.out.println(cList);
		for (Certificates certificates : cList) {
			qrlist.add(certificates.getQrCode());
		}
	}
	
	@Test
	public void testQRSearch() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "2000");
		queryMap.put("latestScanStatus", "-1");
		QueryParamTO paramTO = new QueryParamTO();
		paramTO.setPageNum(0);
		paramTO.setPageSize(100);
		paramTO.setQueryMap(queryMap);
		List<Certificates> rr = certValidationService.getValidationData(paramTO);
		System.out.println("*** result size :" + rr.size());
		System.out.println(rr);
	}
	
	@Test
	public void validateCertificates(){
		for (String qrCode : qrlist) {
			OriginTrack originTrack = new OriginTrack();
			originTrack.setQrcode(qrCode);
			originTrack.setUserId("anil@a.com");
			certValidationService.validateCertificates(originTrack);	
		}
		
	}
	
	@Test
	public void testQRAnalytics() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "2000");
		ReportResult rr = analyticsService.generateQrAnalytics(queryMap);
		System.out.println(rr);
	}
	
	@Test
	public void testCourseWiseAnalytics() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "2000");
		ReportResult rr = analyticsService.generateCourseWiseAnalytics(queryMap);
		System.out.println(rr);
	}
	
	@Test
	public void testUserTypeWiseAnalytics() throws OriginException {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("merchantId", "2000");
		ReportResult rr = analyticsService.generateCourseAndCompanyWiseAnalytics(queryMap);
		System.out.println(rr);
	}
}

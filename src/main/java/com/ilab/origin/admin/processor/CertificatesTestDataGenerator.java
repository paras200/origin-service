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
import org.springframework.util.StringUtils;

import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.certificates.service.CertificatesValidationService;
import com.ilab.origin.certificates.to.CertificatesTO;
import com.ilab.origin.certificates.to.Student;
import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.mobileapp.model.AppUser;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.usermgt.model.Location;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.service.MerchantService;
import com.ilab.origin.validator.model.OriginTrack;

@Component
public class CertificatesTestDataGenerator {

	private static Log log = LogFactory.getLog(CertificatesTestDataGenerator.class.getName());
	
	private List<String> locationList = new ArrayList<>();
	private List<String> userList = new ArrayList<>();
	private List<String> buList = new ArrayList<>();
	private List<String> courseList = new ArrayList<>();
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private CertificatesValidationService certService;
	
	@PostConstruct
	public void init() {
		locationList.add("Hyde");
		locationList.add("Banglore");
		locationList.add("Pune");
		locationList.add("Delhi");
		locationList.add("Mumbai");
		
		courseList.add("Computer Science");
		courseList.add("Electrical Engg ");
		courseList.add("Data Science");
		courseList.add("Mechnical Engg");
		
		
		////////// userList
		userList.add("sinhanil@gmail.com, Anil Sinha");
		userList.add("pradeep@gmail.com, Pradeep Sambha");
		userList.add("bhushan@gmail.com, Bhushan Pawar");
		userList.add("paras@gmail.com, Paras Agni");
		userList.add("ayan@gmail.com, Ayan Sinha");
		
		buList.add("tcs@tcs.com, TCS");
		buList.add("wipro@wipro.com, WIPRO");
		buList.add("infy@infosys.com, INFOSYS");
	}
	
	@SuppressWarnings("unchecked")
	public void generateTestData(String institutesName, Integer userScanPercentage , Integer businessScanPercentage, boolean generateQR , String merchantId) throws OriginException {
		List<Certificates> cList = null;
		if(generateQR) {
			if(StringUtils.isEmpty(merchantId)) {
				throw new OriginException("for QR generation merchant Id is mandatory");
			}
			
			Merchant merchant =  merchantService.findMerchantById(merchantId);
			
			if(merchant == null) {
				throw new OriginException("No data for the merchant Id provided");
			}
			cList = new ArrayList<>();
			for (String courseName : courseList) {
				CertificatesTO bulkCertificatesTO = new CertificatesTO();
				bulkCertificatesTO.setCourseName(courseName);
				bulkCertificatesTO.setInstituteName(institutesName);
				bulkCertificatesTO.setMerchantId(merchantId);
				bulkCertificatesTO.setUniversityName(merchant.getName());
				List<Student> students = new ArrayList<>();
				for (int i = 0; i < 5; i++) {
					Student s1 = new Student();
					s1.setStudentName(getRandomUser());
					s1.setDateOfBirth("10/01/" +2001+i);
					students.add(s1);
				}
				bulkCertificatesTO.setStudents(students);
				cList.addAll(certService.saveAllQRCode(bulkCertificatesTO));
			}
			
		}else {

			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("instituteName", institutesName);
			
			Query query = mongoQueryMgr.createQuery(queryMap);
			query.fields().include("qrCode");
			query.fields().include("courseName");
			query.fields().include("timeinmilli");
			cList = (List<Certificates>) mongoQueryMgr.executeQuery(Certificates.class , query);	
		}
		
		
		
		int total = cList.size();
		int userScanCount = (total * userScanPercentage)/100 ;
		int businessScanCount = (total * businessScanPercentage)/100 ;
		
		// add scan
		addScan( userScanCount, cList );
		addBusinessScan( businessScanCount, cList);

		log.info("Test data generated for isntitute : " + institutesName  +" & merchant Id " + merchantId);
	}

	private void addScan(int userScanCount, List<Certificates> updatedResult ) {
		for (int i = 0; i < userScanCount; i++) {
			OriginTrack oTrack = new OriginTrack();
			oTrack.setQrcode(updatedResult.get(i).getQrCode());
			oTrack.setLocation(getRandomLocation());
			oTrack.setUserType(AppUser.USER_TYPE_INDIVIDUAL);
			setRandomUser(oTrack);
			certService.validateCertificates(oTrack);
		}		
	}
	
	private void addBusinessScan(int businessScanCount, List<Certificates> updatedResult ) {
		for (int i = 0; i < businessScanCount; i++) {
			OriginTrack oTrack = new OriginTrack();
			oTrack.setQrcode(updatedResult.get(getRandomIndex(updatedResult)).getQrCode());
			oTrack.setLocation(getRandomLocation());
			oTrack.setUserType(AppUser.USER_TYPE_BUSINESS);
			setRandomBussUser(oTrack);
			certService.validateCertificates(oTrack);
		}		
	}

	private void setRandomUser(OriginTrack oTrack) {
		String userDetails = userList.get(getRandomNumberInRange(0, userList.size()-1));
		String[] userDArr = userDetails.split(",");
		oTrack.setUserId(userDArr[0]);
	}
	
	private int getRandomIndex(List<?> dataList) {
		return getRandomNumberInRange(0, dataList.size()-1);
	}
	
	private String getRandomUser() {
		String userDetails = userList.get(getRandomNumberInRange(0, userList.size()-1));
		String[] userDArr = userDetails.split(",");
		//oTrack.setUserId(userDArr[0]);
		return userDArr[1];
	}
	private void setRandomBussUser(OriginTrack oTrack) {
		String userDetails = buList.get(getRandomNumberInRange(0, buList.size()-1));
		String[] userDArr = userDetails.split(",");
		oTrack.setUserId(userDArr[0]);
	}
	
	private Location getRandomLocation() {
		int index = getRandomNumberInRange(0, locationList.size()-1);	
		Location loc = new Location();
		loc.setAddress(locationList.get(index));
		return loc;
	}
	
	
	
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}

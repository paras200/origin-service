package com.ilab.origin.certificates.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.certificates.repo.CertificatesRepo;
import com.ilab.origin.certificates.to.CertificatesTO;
import com.ilab.origin.certificates.to.Student;
import com.ilab.origin.serial.SerialNumberGenerator;
import com.ilab.origin.serial.UUIDSerialization;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.OriginStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/certificates")
public class CertificatesValidationService {
	
	private static Log log = LogFactory.getLog(CertificatesValidationService.class.getName());
	
	@Autowired
	private CertificatesRepo certRepo;
	
	private SerialNumberGenerator slgenerator = UUIDSerialization.getInstance();
	
	@PostMapping("/generate-qrcode")	
	public List<Certificates> saveAllQRCode(@RequestBody CertificatesTO inputData) throws OriginException{		
		
		if(inputData.getStudents() == null || inputData.getStudents().size() == 0){
			throw new OriginException("Students data is missing");
		}
		List<Certificates> certList = new ArrayList<>();
		List<Student> students = inputData.getStudents();
		for (Student student : students) {
			Certificates certificates = new Certificates();
			certificates.setCourseName(inputData.getCourseName());
			certificates.setInstituteName(inputData.getInstituteName());
			
			certificates.setStudentName(student.getStudentName());
			certificates.setDateOfBirth(student.getDateOfBirth());
			certificates.setCertificateId(student.getCertificateId());
			
			certificates.setQrCode(Certificates.CERTIFICATES_QR_PREFIX + slgenerator.getSequenceNumber());
			
			certList.add(certificates);
		}
		List<Certificates> cList = certRepo.save(certList);
		return cList;
	}

	public Certificates validateCertificates(String qrcode) {
		log.info("Certificates Validation is in progress...");
		Certificates certificate =  certRepo.findByQrCode(qrcode);
		if(certificate != null) {
			certificate.setStatusCode(OriginStatus.GREEN);
			StringBuilder sb = new StringBuilder("The Certificate is issued by " + certificate.getInstituteName() + "  for the Course " + certificate.getCourseName());
			sb.append(" \n Please validate the Certificates Recipient details as below.");
			certificate.setMessage(sb.toString());
		}else {
			certificate = new Certificates();
			certificate.setStatusCode(OriginStatus.RED);
			StringBuilder sb = new StringBuilder("The Certificate is not listed with us");
			certificate.setMessage(sb.toString());
		}
		return certificate;
	}
}

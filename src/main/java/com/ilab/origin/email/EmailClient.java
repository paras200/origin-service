package com.ilab.origin.email;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ilab.origin.certificates.model.Certificates;

@Component
public class EmailClient {

	private static Log log = LogFactory.getLog(EmailClient.class.getName());

	@Value("${email.server.url}")
	private String emailServerUrl;

	@Value("${origin.base.server.url}")
	private String originBaseServerUrl;

	private String templateBasedUrl;
	private String customEmailUrl;

	@PostConstruct
	public void init() {
		templateBasedUrl = emailServerUrl + "/sendTemplateMail";
		customEmailUrl = emailServerUrl + "/sendMail"; // TODO change
	}

	public void sendEmailByTemplate(EmailBody emailBody) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForEntity(templateBasedUrl, emailBody, String.class);
	}

	public void sendUserRegistrationLink(String emailId, String userCode) {
		String uri = originBaseServerUrl + "/app/#/auth/user-register?userCode=" + userCode;
		try {
			RestTemplate restTemplate = new RestTemplate();
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("link", uri);
			EmailBody eb = new EmailBody();
			eb.setTemplate("invitation");
			eb.getToList().add(emailId);
			eb.setParamMap(paramMap);
			eb.getToList().add("coinxlab@gmail.com");
			log.info("sending email .... " + eb);
			restTemplate.postForEntity(templateBasedUrl, eb, String.class);
		} catch (Exception ex) {
			log.error("Error sending email ", ex);
		}

	}

	public void sendTemporaryPassword(String emailId, String tempPassword) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("password", tempPassword);
			EmailBody eb = new EmailBody();
			eb.setTemplate("temp-pass");
			eb.getToList().add(emailId);
			eb.setParamMap(paramMap);
			eb.getToList().add("coinxlab@gmail.com");
			log.info("sending email .... " + eb);
			restTemplate.postForEntity(templateBasedUrl, eb, String.class);
		} catch (Exception ex) {
			log.error("Error sending email ", ex);
		}

	}
	
	public void sendCertificateDetails(Certificates certificate, String userEmail) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("instituteName", certificate.getInstituteName());
			paramMap.put("studentName", certificate.getStudentName());
			paramMap.put("courseName", certificate.getCourseName());
			paramMap.put("dob", certificate.getDateOfBirth());
			
			EmailBody eb = new EmailBody();
			eb.setTemplate("cert-scan");
			eb.getToList().add(userEmail);
			eb.setParamMap(paramMap);
			eb.getToList().add("originscan.stage.com");
			log.info("sending certificate details email .... " + eb);
			restTemplate.postForEntity(templateBasedUrl, eb, String.class);
		} catch (Exception ex) {
			log.error("Error sending email ", ex);
		}

	}

	public void sendInternalError(String body) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			EmailBody eb = new EmailBody();
			eb.setBody(body);
			eb.setSubject("Alert - Internal payment processing error");
			eb.getToList().add("coinxlab@gmail.com");
			// eb.setToList(AppConstants.SYSTEM_EMAIL);
			log.info("sending email .... " + eb);
			restTemplate.postForEntity(customEmailUrl, eb, String.class);
		} catch (Exception ex) {
			log.error("Error sending email ", ex);
		}

	}

}

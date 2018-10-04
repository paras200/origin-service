package com.ilab.origin.email;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



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
	public void init(){
		templateBasedUrl = emailServerUrl +"/sendTemplateMail";
		customEmailUrl = emailServerUrl + "/sendMail"; // TODO change
	}
	
	public void sendEmailByTemplate(EmailBody emailBody) {
        RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForEntity(templateBasedUrl, emailBody, String.class);
	}

	public void sendUserRegistrationLink(String emailId, String userCode) {
		String uri = originBaseServerUrl+"/#/auth/user-register?userCode=" + userCode;
		String body =" Hi \n   You have been invited to join Origin Scan, please use the link below to Register \n";
		body += uri;
		String subject = "Invitation To Join Origin Scan";
		try {
	        RestTemplate restTemplate = new RestTemplate();
	        EmailBody eb = new EmailBody();
	        eb.setBody(body);
	        eb.setSubject(subject);
	        eb.getToList().add(emailId);
	        eb.getToList().add("coinxlab@gmail.com");
	        log.info("sending email .... "  + eb);
			restTemplate.postForEntity(customEmailUrl, eb, String.class);			
		}catch(Exception ex) {
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
	        log.info("sending email .... "  + eb);
			restTemplate.postForEntity(customEmailUrl, eb, String.class);			
		}catch(Exception ex) {
			log.error("Error sending email ", ex);
		}

	}

	
}

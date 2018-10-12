package com.ilab.origin.password;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class TemporaryPasswordService implements PasswordService{

	@Override
	public String generatePasswordSequence() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*";
		String pwd = RandomStringUtils.random( 5, characters );
		return pwd;
	}

	
}

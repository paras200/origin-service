package com.ilab.origin.mobileapp.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EntityScan
@Document
public class AppUser {
	
	public static String USER_TYPE_INDIVIDUAL ="INDIVIDUAL";
	public static String USER_TYPE_BUSINESS ="BUSINESS";

	@Id
    public String id;

	@Indexed(unique=true)
	private String userId; // email-id as user id
	
	private String password;
	
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String location;
	
	private Boolean isTempPassword = false;
	private String userType = USER_TYPE_INDIVIDUAL;
	private String businessName;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}



	public String getId() {
		return id;
	}	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
		
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		if(firstName == null && lastName == null) {
			return userId;
		}
		if(lastName == null) return firstName;
		
		return firstName + " " + lastName;
	}

	public Boolean getIsTempPassword() {
		return isTempPassword;
	}

	public void setIsTempPassword(Boolean isTempPassword) {
		this.isTempPassword = isTempPassword;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
}

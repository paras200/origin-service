package com.ilab.origin.validator.model;

import java.util.Calendar;

/**
 * The aim of the class is return  success/error, for now its been used for success only
 * @author sinhanil
 *
 */
public class Result {

	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_FAILUER = "FAIL";
	
	private String token;
	private boolean isSuccess;
	private int validTill = 60;
	private long timestamp = Calendar.getInstance().getTimeInMillis();
	private String message;
	private String status;
	
	public Result() {
		
	}
	
	public Result(String status) {
		this.status = status;
	}
	public Result(String status , String message) {
		this.message = message;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getValidTill() {
		return validTill;
	}

	public void setValidTill(int validTill) {
		this.validTill = validTill;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
	
}

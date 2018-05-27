package com.ilab.origin.validator.model;

import java.util.Calendar;

/**
 * The aim of the class is return  success/error, for now its been used for success only
 * @author sinhanil
 *
 */
public class Result {

	private String status = "SUCCESS";
	private String token;
	private boolean isSuccess;
	private int validTill = 60;
	private long timestamp = Calendar.getInstance().getTimeInMillis();

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
		
}

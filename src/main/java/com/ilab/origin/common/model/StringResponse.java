package com.ilab.origin.common.model;

import com.ilab.origin.validator.model.Result;

public class StringResponse {
	
	private String result;
	private String status = Result.STATUS_SUCCESS ;
	
	public StringResponse(String result) {
		this.result = result;
		this.status = Result.STATUS_SUCCESS;
	}
	
	public StringResponse(String result, String status) {
		this.result = result;
		this.status = status;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

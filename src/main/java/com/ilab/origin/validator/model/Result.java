package com.ilab.origin.validator.model;

/**
 * The aim of the class is return  success/error, for now its been used for success only
 * @author sinhanil
 *
 */
public class Result {

	private String status = "SUCCESS";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

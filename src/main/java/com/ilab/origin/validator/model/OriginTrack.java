package com.ilab.origin.validator.model;

import java.util.Date;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ilab.origin.usermgt.model.Location;

@EntityScan
@Document
public class OriginTrack {

	@Id
    public String id;
	
	private String qrcode;
	private String userId;
	private int statusCode;
	private String comment;
	private Location location;
	private Date scanTime;
	private String productName;
	private String manufacturerName;
	private String merchantKey;
	
	
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getId() {
		return id;
	}
	public Date getScanTime() {
		return scanTime;
	}
	public void setScanTime(Date scanTime) {
		this.scanTime = scanTime;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	@Override
	public String toString() {
		return "OriginTrack [id=" + id + ", qrcode=" + qrcode + ", userId=" + userId + ", statusCode=" + statusCode
				+ ", comment=" + comment + ", location=" + location + ", scanTime=" + scanTime + "]";
	}
		
	
}

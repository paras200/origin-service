package com.ilab.origin.feedback.mode;

import java.util.Calendar;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ilab.origin.usermgt.model.Location;

@EntityScan
@Document
public class FeedBackData {

	@Id
    public String id;
	
	@Indexed
	private String userId;
	private String userName;
	private String mobileNum;
	
	@Indexed
	private String qrCode;
	private String qrKey;
	private String merchantId;
	private String message;
	private String productName;
	private long timeinmilli = Calendar.getInstance().getTimeInMillis();
	private Location location;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getQrKey() {
		return qrKey;
	}
	public void setQrKey(String qrKey) {
		this.qrKey = qrKey;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getTimeinmilli() {
		return timeinmilli;
	}
	public void setTimeinmilli(long timeinmilli) {
		this.timeinmilli = timeinmilli;
	}
	public String getId() {
		return id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

}

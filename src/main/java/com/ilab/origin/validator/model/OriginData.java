package com.ilab.origin.validator.model;

import java.util.Calendar;
import java.util.Map;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.ilab.origin.usermgt.model.Location;

@EntityScan
public class OriginData {

	public static boolean SOLD = true;
	public static boolean NOT_SOLD = false;
	
	@Id
    public String id;

	@Indexed(unique=true)
	private String qrCode;
	
	private String qrKey;
	
	private String productName;
	
	@Indexed
	private String merchantId;
	private Map<String, String> dataMap;
	private boolean isSold = NOT_SOLD;
	private int statusCode;
	private String message;
	
	private Location location;
	private long timeinmilli = Calendar.getInstance().getTimeInMillis();
	
	
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSold() {
		return isSold;
	}
	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}
	
	public String getQrKey() {
		return qrKey;
	}
	public void setQrKey(String qrKey) {
		this.qrKey = qrKey;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int status) {
		this.statusCode = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTimeinmilli() {
		return timeinmilli;
	}
	public void setTimeinmilli(long timeinmilli) {
		this.timeinmilli = timeinmilli;
	}	
	
}

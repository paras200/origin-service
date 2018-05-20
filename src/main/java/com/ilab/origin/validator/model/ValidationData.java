package com.ilab.origin.validator.model;

import java.util.Map;

import org.springframework.data.annotation.Id;

public class ValidationData {

	public static boolean SOLD = true;
	public static boolean NOT_SOLD = false;
	
	@Id
    public String id;

	private String qrCode;
	private String productName;
	private String merchantId;
	private Map<String, String> dataMap;
	private boolean isSold = NOT_SOLD;
	
	
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
		
}

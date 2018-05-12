package com.ilab.origin.validator.model;

import org.springframework.data.annotation.Id;

public class ValidationData {

	public static boolean SOLD = true;
	public static boolean NOT_SOLD = false;
	
	@Id
    public String id;

	private String qrKey;
	private String productName;
	private String merchantId;
	private boolean isSold = NOT_SOLD;
	
	public String getQrKey() {
		return qrKey;
	}
	public void setQrKey(String qrKey) {
		this.qrKey = qrKey;
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

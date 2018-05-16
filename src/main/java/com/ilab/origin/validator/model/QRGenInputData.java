package com.ilab.origin.validator.model;

import java.util.Map;

public class QRGenInputData {

	private String merchantId;
	private String merchantKey;
	private int count;
	private String productName;
	private Map<String, String> otherParameters;
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Map<String, String> getOtherParameters() {
		return otherParameters;
	}
	public void setOtherParameters(Map<String, String> otherParameters) {
		this.otherParameters = otherParameters;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
		
	
}

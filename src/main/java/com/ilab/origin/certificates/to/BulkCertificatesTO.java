package com.ilab.origin.certificates.to;

import java.util.List;

public class BulkCertificatesTO {

	private String fileId;
	private String header;
	private List<String> data;
	private String merchantKey;
	private String merchantId;

	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@Override
	public String toString() {
		return "BulkCertificatesTO [fileId=" + fileId + ", header=" + header + ", data=" + data + ", merchantKey="
				+ merchantKey + ", merchantId=" + merchantId + "]";
	}
	
	
}

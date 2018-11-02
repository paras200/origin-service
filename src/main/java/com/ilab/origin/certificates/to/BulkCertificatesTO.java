package com.ilab.origin.certificates.to;

import java.util.List;

public class BulkCertificatesTO {

	private String fileId;
	private List<String> header;
	private List<Student> data;
	private String merchantKey;
	private String merchantId;
	private String universityName;

	
	public List<String> getHeader() {
		return header;
	}
	public void setHeader(List<String> header) {
		this.header = header;
	}
	public List<Student> getData() {
		return data;
	}
	public void setData(List<Student> data) {
		this.data = data;
	}
	public String getUniversityName() {
		return universityName;
	}
	public void setUniversityName(String universityName) {
		this.universityName = universityName;
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

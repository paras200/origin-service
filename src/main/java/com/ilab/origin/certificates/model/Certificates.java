package com.ilab.origin.certificates.model;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.ilab.origin.common.model.QRData;
import com.ilab.origin.validator.model.OriginStatus;

public class Certificates implements QRData{

	public static String CERTIFICATES_QR_PREFIX = "CA-";
	public static String QR_TYPE = "CERTIFICATES";
	
	@Id
    public String id;

	@Indexed(unique=true)
	private String qrCode;
	
	@Indexed
	private String instituteName;
	
    @NotNull(message = "universityName must not be null")
	@Indexed()
	private String universityName;
	
    @NotNull(message = "courseName must not be null")
	@Indexed
	private String courseName;
    
    @NotNull(message = "studentName must not be null")
	private String studentName;
    
	private String dateOfBirth;
	private String certificateId;
	
	private String merchantKey;
	
	@Indexed
	private String merchantId;
	
	private Date certIssueDate;
	
	@Indexed
	private long timeinmilli = Calendar.getInstance().getTimeInMillis();
	
	private String qrType = QR_TYPE;
	private String fileId;	
	private String certUrl;
	
	// Transient field not saved
	private int statusCode;
	private String message;
	private int latestScanStatus = OriginStatus.NO_SCAN;
	private boolean userFeeback = false;
	
	
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getInstituteName() {
		return instituteName;
	}
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}
	public String getId() {
		return id;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getQrType() {
		return qrType;
	}
	public void setQrType(String qrType) {
		this.qrType = qrType;
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
	public long getTimeinmilli() {
		return timeinmilli;
	}
	public void setTimeinmilli(long timeinmilli) {
		this.timeinmilli = timeinmilli;
	}
	public String getUniversityName() {
		return universityName;
	}
	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public Date getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	
	public int getLatestScanStatus() {
		return latestScanStatus;
	}
	public void setLatestScanStatus(int latestScanStatus) {
		this.latestScanStatus = latestScanStatus;
	}
	
	public boolean isUserFeeback() {
		return userFeeback;
	}
	public void setUserFeeback(boolean userFeeback) {
		this.userFeeback = userFeeback;
	}
	
	
	public String getCertUrl() {
		return certUrl;
	}
	public void setCertUrl(String certUrl) {
		this.certUrl = certUrl;
	}
	@Override
	public String toString() {
		return "Certificates [qrCode=" + qrCode + ", universityName=" + universityName + ", studentName=" + studentName
				+ "]";
	}
	
	
	
}

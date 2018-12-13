package com.ilab.origin.certificates.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ilab.origin.mobileapp.model.AppUser;
import com.ilab.origin.usermgt.model.Location;

@EntityScan
@Document
public class CertificateTrack {

	@Id
    public String id;
	
	@Indexed
	private String qrcode;
	
	@Indexed
	private String userId;
	private String userType = AppUser.USER_TYPE_INDIVIDUAL;
	private int statusCode;
	private String comment;
	private Location location;
	private Date scanTime;
	private String institutesName;
	private String universityName;
	
	@NotNull(message = "courseName must not be null")
	private String courseName;
	
	private String merchantId;
	
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
	public Date getScanTime() {
		return scanTime;
	}
	public void setScanTime(Date scanTime) {
		this.scanTime = scanTime;
	}
	public String getInstitutesName() {
		return institutesName;
	}
	public void setInstitutesName(String institutesName) {
		this.institutesName = institutesName;
	}
	public String getUniversityName() {
		return universityName;
	}
	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getId() {
		return id;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
}

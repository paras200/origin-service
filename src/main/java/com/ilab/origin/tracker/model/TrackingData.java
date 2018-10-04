package com.ilab.origin.tracker.model;

import java.util.Date;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ilab.origin.usermgt.model.Location;

@EntityScan
@Document
public class TrackingData implements Comparable<TrackingData>{

	@Id
    public String id;
	
	@Indexed
	private String qrcode;
	
	@Indexed
	private String lotNumber;
	private String productName;
	private String manufacturerId;
	private String manufacturerName;
	private String merchantId;
	private String merchantName;
	
	private String userId;
	private ProductOwner previousOwner;
	private ProductOwner owner;
	private Date creationDate = new Date();
	private String status;
	private String comments;
	private Location location;
	
	
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ProductOwner getPreviousOwner() {
		return previousOwner;
	}
	public void setPreviousOwner(ProductOwner previousOwner) {
		this.previousOwner = previousOwner;
	}
	public ProductOwner getOwner() {
		return owner;
	}
	public void setOwner(ProductOwner owner) {
		this.owner = owner;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	@Override
	public int compareTo(TrackingData o) {
		if(this.getCreationDate() == o.getCreationDate()) return 0;
		else if(this.getCreationDate() != null) {
			return this.getCreationDate().compareTo(o.getCreationDate());
		}else if(o.getCreationDate() != null) {
			return o.getCreationDate().compareTo(this.getCreationDate());
		}
		return 0;
	}
	
	
}

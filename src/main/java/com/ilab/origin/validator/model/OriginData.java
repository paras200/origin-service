package com.ilab.origin.validator.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ilab.origin.usermgt.model.Location;

@EntityScan
@Document
public class OriginData implements Cloneable{

	public static boolean SOLD = true;
	public static boolean NOT_SOLD = false;
	
	@Id
    public String id;

	@Indexed(unique=true)
	private String qrCode;
	
	@Indexed
	private String productName;
	private String productCode;
	
	@Indexed
	private String merchantId;
	
	private String gstn;
	private String expiryDate;
	
	@Indexed
	private String lotNumber;
	private String serialNumber;
	private String otherInfo;
	
	private Map<String, String> dataMap;
	private boolean isSold = NOT_SOLD;
	
	private int statusCode;
	private String message;
	
	private Location location;
	private long timeinmilli = Calendar.getInstance().getTimeInMillis();
	private Date latestScanTime = new Date();
	
	@Indexed
	private int latestScanStatus;
	private Date firstScanTime ;
	private boolean includeOriginCheck = true;
	private boolean userFeeback = false;
	
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
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
	
	public String getGstn() {
		return gstn;
	}
	public void setGstn(String gstn) {
		this.gstn = gstn;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
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
	public Date getLatestScanTime() {
		return latestScanTime;
	}
	public void setLatestScanTime(Date latestScanTime) {
		this.latestScanTime = latestScanTime;
	}
	public int getLatestScanStatus() {
		return latestScanStatus;
	}
	public void setLatestScanStatus(int latestScanStatus) {
		this.latestScanStatus = latestScanStatus;
	}
	public Date getFirstScanTime() {
		return firstScanTime;
	}
	public void setFirstScanTime(Date firstScanTime) {
		this.firstScanTime = firstScanTime;
	}
	
	public boolean isIncludeOriginCheck() {
		return includeOriginCheck;
	}
	public void setIncludeOriginCheck(boolean includeOriginCheck) {
		this.includeOriginCheck = includeOriginCheck;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
	public boolean isUserFeeback() {
		return userFeeback;
	}

	public void setUserFeeback(boolean userFeeback) {
		this.userFeeback = userFeeback;
	}

	public String getDisplayText() {
		StringBuilder sb = new StringBuilder();
		sb.append("Product Name : " + productName).append("\n");
		sb.append("Expiry Date :" + expiryDate).append("\n");
		sb.append("GSTN : " + gstn ).append("\n");
		sb.append("Serial Num : " + serialNumber);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "OriginData [id=" + id + ", qrCode=" + qrCode + ", productName=" + productName + ", merchantId="
				+ merchantId + ", getQrCode()=" + getQrCode() + ", getDataMap()=" + getDataMap() + ", getProductName()="
				+ getProductName() + ", getMerchantId()=" + getMerchantId() + ", isSold()=" + isSold() + ", getGstn()="
				+ getGstn() + ", getExpiryDate()=" + getExpiryDate() + ", getLotNumber()=" + getLotNumber()
				+ ", getSerialNumber()=" + getSerialNumber() + ", getOtherInfo()=" + getOtherInfo() + ", getLocation()="
				+ getLocation() + ", getStatusCode()=" + getStatusCode() + ", getMessage()=" + getMessage()
				+ ", getTimeinmilli()=" + getTimeinmilli() + ", getLatestScanTime()=" + getLatestScanTime()
				+ ", getLatestScanStatus()=" + getLatestScanStatus() + ", getFirstScanTime()=" + getFirstScanTime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}	
	
	
	
}

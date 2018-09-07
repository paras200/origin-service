package com.ilab.origin.tracker.model;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ilab.origin.usermgt.model.Location;

@EntityScan
@Document
public class TransactionInfo {

	@Id
    public String id;

	@Indexed(unique=true)
	private String qrcode;
	
	// Transaction Information
	private String productName;
	private String nationalDrugCode;
	private String productDosage;
	private String lotNumber;
	private int containerSize;
	private int numberOfContainer;
	private String transactionDate;
	private ProductOwner owner;
	private ProductOwner previousOwner;
	
	
	private String trackingCode;
	
	@Indexed
	private String merchantId;
	private Map<String, String> dataMap;
	
	private List<Location> locations ;

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrCode) {
		this.qrcode = qrCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Map<String, String> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}

	public String getId() {
		return id;
	}

	public String getNationalDrugCode() {
		return nationalDrugCode;
	}

	public void setNationalDrugCode(String nationalDrugCode) {
		this.nationalDrugCode = nationalDrugCode;
	}

	public String getProductDosage() {
		return productDosage;
	}

	public void setProductDosage(String productDosage) {
		this.productDosage = productDosage;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public int getContainerSize() {
		return containerSize;
	}

	public void setContainerSize(int containerSize) {
		this.containerSize = containerSize;
	}

	public int getNumberOfContainer() {
		return numberOfContainer;
	}

	public void setNumberOfContainer(int numberOfContainer) {
		this.numberOfContainer = numberOfContainer;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ProductOwner getOwner() {
		return owner;
	}

	public void setOwner(ProductOwner owner) {
		this.owner = owner;
	}

	public ProductOwner getPreviousOwner() {
		return previousOwner;
	}

	public void setPreviousOwner(ProductOwner previousOwner) {
		this.previousOwner = previousOwner;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}	
}

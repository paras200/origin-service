package com.ilab.origin.usermgt.model;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EntityScan
@Document
public class Merchant {

	@Id
    public String id;

	@Indexed(unique=true)
	private String merchantKey;
	
	@Indexed(unique=true)
	private String name;
	
	private String phoneNumber;
	private String mobileNumber;	
	private String emailId;
	private String address;
	private String companyRegNumber;
	
	@Indexed(unique=true)
	private String companyPANNumber;
	
	private List<Product> productList;
	
	
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompanyRegNumber() {
		return companyRegNumber;
	}
	public void setCompanyRegNumber(String companyRegNumber) {
		this.companyRegNumber = companyRegNumber;
	}
	public String getId() {
		return id;
	}
	public String getCompanyPANNumber() {
		return companyPANNumber;
	}
	public void setCompanyPANNumber(String companyPANNumber) {
		this.companyPANNumber = companyPANNumber;
	}
	
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((companyPANNumber == null) ? 0 : companyPANNumber.hashCode());
		result = prime * result + ((companyRegNumber == null) ? 0 : companyRegNumber.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((merchantKey == null) ? 0 : merchantKey.hashCode());
		result = prime * result + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Merchant other = (Merchant) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (companyPANNumber == null) {
			if (other.companyPANNumber != null)
				return false;
		} else if (!companyPANNumber.equals(other.companyPANNumber))
			return false;
		if (companyRegNumber == null) {
			if (other.companyRegNumber != null)
				return false;
		} else if (!companyRegNumber.equals(other.companyRegNumber))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (merchantKey == null) {
			if (other.merchantKey != null)
				return false;
		} else if (!merchantKey.equals(other.merchantKey))
			return false;
		if (mobileNumber == null) {
			if (other.mobileNumber != null)
				return false;
		} else if (!mobileNumber.equals(other.mobileNumber))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Merchant [id=" + id + ", merchantKey=" + merchantKey + ", name=" + name + ", phoneNumber=" + phoneNumber
				+ ", mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", address=" + address
				+ ", companyRegNumber=" + companyRegNumber + ", companyPANNumber=" + companyPANNumber + "]";
	}
	
	
	
}

package com.ilab.origin.usermgt.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class QRTemplates {

	@Id
    public String id;
	
	private String merchantId;
	private String name;
	
	private List<Field> fields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}		
}

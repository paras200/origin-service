package com.ilab.origin.usermgt.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class MerchantConfig {

	@Id
    public String id;
	
	@Indexed
	private String merchantId;
	
	private List<ProductConfig>  productConfigs;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public List<ProductConfig> getProductConfigs() {
		return productConfigs;
	}

	public void setProductConfigs(List<ProductConfig> productConfigs) {
		this.productConfigs = productConfigs;
	}

	public String getId() {
		return id;
	}
	
	
}

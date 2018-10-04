package com.ilab.origin.usermgt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class MerchantConfig {

	@Id
    public String id;
	
	@Indexed
	private String merchantId;
}

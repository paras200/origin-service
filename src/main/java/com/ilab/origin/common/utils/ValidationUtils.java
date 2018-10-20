package com.ilab.origin.common.utils;

import org.springframework.util.StringUtils;

import com.ilab.origin.tracker.error.OriginException;

public class ValidationUtils {

	public static void validateInputParam(String merchantId) throws OriginException {
		if(StringUtils.isEmpty(merchantId)) {
			throw new OriginException("merchantId is mandatory, please provide the same");
		}
	}
}

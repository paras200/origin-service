package com.ilab.origin.serial;

import org.springframework.stereotype.Service;

import com.ilab.origin.crypto.DigitalSignatureUtil;

@Service
public class CryptoBasedQrGenerator implements QRGenerator  {

	@Override
	public String generateQRCode(String text) {
		return DigitalSignatureUtil.applySha256(text);
	}
}

package com.ilab.origin.validator.model;

import com.ilab.origin.common.utils.QRType;

public class QRGenInputData {
	
	private OriginData originData;
	private int count;
	// default is single
	private String qrType = QRType.SINGLE.name();
	
	public OriginData getOriginData() {
		return originData;
	}
	public void setOriginData(OriginData originData) {
		this.originData = originData;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getQrType() {
		return qrType;
	}
	public void setQrType(String qrType) {
		this.qrType = qrType;
	}
	
}

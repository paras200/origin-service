package com.ilab.origin.common.model;

import com.ilab.origin.usermgt.model.Location;

public class LocationTO {
	
	private String qrCode;
	private Location location;
	
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	

}

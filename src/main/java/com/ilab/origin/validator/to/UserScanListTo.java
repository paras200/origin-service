package com.ilab.origin.validator.to;

import java.util.List;

import com.ilab.origin.validator.model.OriginTrack;

public class UserScanListTo {

	private OriginTrack originTrack;
	
	private List<OriginTrack> scanList;

	
	public OriginTrack getOriginTrack() {
		return originTrack;
	}

	public void setOriginTrack(OriginTrack originTrack) {
		this.originTrack = originTrack;
	}

	public List<OriginTrack> getScanList() {
		return scanList;
	}

	public void setScanList(List<OriginTrack> scanList) {
		this.scanList = scanList;
	}

}

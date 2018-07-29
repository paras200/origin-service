package com.ilab.origin.tracker.to;

import java.util.List;

import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.model.TransactionInfo;

public class TraceReportTo {

	private TransactionInfo transactionInfo;
	private List<TrackingData> trackingDataList;
	
	public TransactionInfo getTransactionInfo() {
		return transactionInfo;
	}
	public void setTransactionInfo(TransactionInfo transactionInfo) {
		this.transactionInfo = transactionInfo;
	}
	public List<TrackingData> getTrackingDataList() {
		return trackingDataList;
	}
	public void setTrackingDataList(List<TrackingData> trackingDataList) {
		this.trackingDataList = trackingDataList;
	}
	
	
}

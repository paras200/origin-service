package com.ilab.origin.tracker.to;

import com.ilab.origin.tracker.model.TransactionInfo;

public class TransactionInfoTo extends TransactionInfo {
	
	

	public String getDisplayText() {
		StringBuilder sb = new StringBuilder();
		sb.append("Product Name : " + getProductName()).append("\n");
		sb.append("Lot Number : " + getLotNumber()).append("\n");
		sb.append("Container Size : " + getContainerSize()).append("\n");
		sb.append("Number Of Container : " + getNumberOfContainer()).append("\n");
		if(getOwner() != null) {
			sb.append("Manufacturer : " + getOwner().getBusinessName()).append("\n");
		}
		return sb.toString();
	}
}

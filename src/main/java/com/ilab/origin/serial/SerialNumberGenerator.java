package com.ilab.origin.serial;

import java.util.List;

public interface SerialNumberGenerator {

	public String getSequenceNumber() ;
	
	public List<String> getSequenceNumber(int count) ;
}

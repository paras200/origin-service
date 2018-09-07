package com.ilab.origin.validator.model;

import java.util.HashMap;
import java.util.Map;

public class OriginStatus {
	
	private static Map<Integer, String> statusMap = null;
	
	public static int RED = 2;
	public static int AMBER =  1;
	public static int GREEN = 0;
	public static int NO_SCAN =-1;

	private static String GREEN_MSG ="Genuine Product !!!";
	private static String AMBER_MSG ="Already SOLD - if you are scanning this for the 1st time, please get back to us.";
	private static String RED_MSG ="This is not a valid product, please contact us";
	
	public static String getStatusMessage(int status) {
		if(statusMap == null) {
			statusMap = new HashMap<>();
			statusMap.put(GREEN, GREEN_MSG);
			statusMap.put(AMBER, AMBER_MSG);
			statusMap.put(RED, RED_MSG);
		}
		return statusMap.get(status);
	}
	
}

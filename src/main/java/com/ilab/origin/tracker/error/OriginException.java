package com.ilab.origin.tracker.error;

public class OriginException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8057099827980648286L;

	public OriginException(String msg) {
		super(msg);
		System.err.println(msg);
	}
	
	public OriginException(String msg, Exception ex) {
		super(msg, ex);
		System.err.println(msg);
	}
}

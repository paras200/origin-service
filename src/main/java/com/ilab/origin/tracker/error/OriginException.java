package com.ilab.origin.tracker.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OriginException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8057099827980648286L;
	private static Log log = LogFactory.getLog(OriginException.class.getName());

	public OriginException(String msg) {
		super(msg);
		log.error(msg);
	}
	
	public OriginException(String msg, Exception ex) {
		super(msg, ex);
		log.error(msg,ex);
	}
}

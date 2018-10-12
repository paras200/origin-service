package com.ilab.origin.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static long convertStartDate(String dateStr) throws ParseException {
		String sDate= dateStr + " 00:00:00";//31/12/2018";  
	    Date date =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(sDate);  
	    System.out.println(sDate+"\t"+date);  
	    
	    Calendar  c = Calendar.getInstance();
	    c.setTime(date);
	    long time = c.getTimeInMillis();
	    System.out.println("start date:" + time);
		return time;
	}
	
	public static long convertEndDate(String dateStr) throws ParseException {
		String eDate= dateStr + " 23:59:59";//31/12/2018";  
	    Date date =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(eDate);  
	    System.out.println(eDate+"\t"+date);  
	    
	    Calendar  c = Calendar.getInstance();
	    c.setTime(date);
	    long time = c.getTimeInMillis();
	    System.out.println("end date:" + time);
		return time;
	}
}

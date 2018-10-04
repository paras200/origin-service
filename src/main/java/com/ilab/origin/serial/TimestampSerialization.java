package com.ilab.origin.serial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimestampSerialization implements SerialNumberGenerator {

	private static SerialNumberGenerator serialNumberGenerator = new TimestampSerialization();
	private long lastSerialNumber = 0;
	private TimestampSerialization() {
	}
	
	public static SerialNumberGenerator getInstance() {
		return serialNumberGenerator;
	}
	@Override
	public synchronized String getSequenceNumber() {
		String seqNumber = "";
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		if(currentTime > lastSerialNumber) {
			lastSerialNumber = currentTime;
			seqNumber = lastSerialNumber +"";
		}else {
			seqNumber = ++lastSerialNumber + "";
		}
		return seqNumber;
	}

	@Override
	public synchronized List<String> getSequenceNumber(int count) {
		List<String> seqList = new ArrayList<>();
		String seqNumber = getSequenceNumber();
		for (int i = 1; i <= count; i++) {
			seqList.add(seqNumber + "-" + i);
		}
		return seqList;
	}

	@Override
	public synchronized List<String> getSequenceNumber(int count, String prefix) {
		List<String> seqList = new ArrayList<>();
		String seqNumber = getSequenceNumber();
		for (int i = 1; i <= count; i++) {
			seqList.add(prefix+seqNumber + "-" + i);
		}
		return seqList;
	}
}

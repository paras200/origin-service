package com.ilab.origin.serial;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UUIDSerialization implements SerialNumberGenerator{

	private static SerialNumberGenerator serialNumberGenerator = new UUIDSerialization();
	private UUIDSerialization() {
	}
	
	public static SerialNumberGenerator getInstance() {
		return serialNumberGenerator;
	}
	@Override
	public String getSequenceNumber() {
		return UUID.randomUUID().toString();
	}

	@Override
	public List<String> getSequenceNumber(int count) {
		List<String> seqList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			seqList.add(UUID.randomUUID().toString());
		}
		return seqList;
	}

	@Override
	public List<String> getSequenceNumber(int count, String prefix) {
		List<String> seqList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			seqList.add(prefix+UUID.randomUUID().toString());
		}
		return seqList;
	}

}

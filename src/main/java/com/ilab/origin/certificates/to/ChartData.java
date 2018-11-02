package com.ilab.origin.certificates.to;

import java.util.List;

public class ChartData {

	private String label;
	private List<String> data;
	
	public ChartData(String label,List<String> data) {
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	
	
}

package com.ilab.origin.certificates.to;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportResult {

	private String status;
	private String reportName;
	
	// pie chart
	private Map<String, String> resultMap;
	
	// bar chart
	private List<List<String>> data = new ArrayList<List<String>>();
	/*private List<String> labels;
	private List<ChartData> chartDataList;
	*/
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	public Map<String, String> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, String> resultMap) {
		this.resultMap = resultMap;
	}
	public List<List<String>> getData() {
		return data;
	}
	public void setData(List<List<String>> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ReportResult [status=" + status + ", reportName=" + reportName + ", resultMap=" + resultMap + ", data="
				+ data + "]";
	}
	
	
	
}

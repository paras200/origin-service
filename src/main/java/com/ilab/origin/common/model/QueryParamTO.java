package com.ilab.origin.common.model;

import java.util.Map;

public class QueryParamTO {

	private Map<String, String> queryMap ;
	private Integer pageSize;
	private 	Integer pageNum;
	
	public Map<String, String> getQueryMap() {
		return queryMap;
	}
	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	
	
}

package com.android.weather.model;
/**
 * 用于解析从google map api返回的地址json数据
 */
import java.util.List;

public class Result {
	
	private List<SubResult> results = null;
	private String status = "";
	
	
	public List<SubResult> getResults() {
		return results;
	}
	public void setResults(List<SubResult> results) {
		this.results = results;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

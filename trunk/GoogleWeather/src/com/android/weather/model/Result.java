package com.android.weather.model;
/**
 * ���ڽ�����google map api���صĵ�ַjson����
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

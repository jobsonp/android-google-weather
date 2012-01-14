package com.android.weather.model;

/**
 * 用于解析从google map api返回的地址json数据
 */
public class SubResult {
	
	private String formatted_address = "";

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

}

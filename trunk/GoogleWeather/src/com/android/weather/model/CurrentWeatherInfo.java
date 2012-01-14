package com.android.weather.model;
/**
 * 用于解析从google weather 获取的天气信息--xml数据
 * @author Administrator
 *
 */
public class CurrentWeatherInfo {

	private String condition;//当前天气状况
	private String postalCode;//行政区名称
	private String humidity;//湿度
	private String tempF;//华氏温度
	private String tempC;//摄氏温度
	private String iconName;
	private String windCondition;//风向风速状况
	
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getTempF() {
		return tempF;
	}
	public void setTempF(String tempF) {
		this.tempF = tempF;
	}
	public String getTempC() {
		return tempC;
	}
	public void setTempC(String tempC) {
		this.tempC = tempC;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getWindCondition() {
		return windCondition;
	}
	public void setWindCondition(String windCondition) {
		this.windCondition = windCondition;
	}
	@Override
	public String toString() {
		return "CurrentWeatherInfo [condition=" + condition + ", postalCode="
				+ postalCode + ", humidity=" + humidity + ", tempF=" + tempF
				+ ", tempC=" + tempC + ", iconName=" + iconName
				+ ", windCondition=" + windCondition + "]";
	}
	
	
	
	
	
}

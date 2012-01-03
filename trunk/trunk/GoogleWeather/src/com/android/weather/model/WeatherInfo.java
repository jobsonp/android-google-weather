package com.android.weather.model;

public class WeatherInfo {

	
	private String dayOfWeek;
	private String lowC;
	private String highC;
	private String iconName;
	private String condition;//ÌìÆø×´¿ö
	
	
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getLowC() {
		return lowC;
	}
	public void setLowC(String lowC) {
		this.lowC = lowC;
	}
	public String getHighC() {
		return highC;
	}
	public void setHighC(String highC) {
		this.highC = highC;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		return "WeatherInfo [dayOfWeek=" + dayOfWeek
				+ ", lowC=" + lowC + ", highC=" + highC + ", iconName="
				+ iconName + ", condition=" + condition + "]";
	}
	
	
	
	
}

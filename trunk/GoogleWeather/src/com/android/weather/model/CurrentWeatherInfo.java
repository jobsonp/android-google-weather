package com.android.weather.model;
/**
 * ���ڽ�����google weather ��ȡ��������Ϣ--xml����
 * @author Administrator
 *
 */
public class CurrentWeatherInfo {

	private String condition;//��ǰ����״��
	private String postalCode;//����������
	private String humidity;//ʪ��
	private String tempF;//�����¶�
	private String tempC;//�����¶�
	private String iconName;
	private String windCondition;//�������״��
	
	
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

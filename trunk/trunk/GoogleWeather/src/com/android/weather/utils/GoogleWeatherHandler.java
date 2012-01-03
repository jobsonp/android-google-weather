package com.android.weather.utils;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.android.weather.model.CurrentWeatherInfo;
import com.android.weather.model.WeatherInfo;

public class GoogleWeatherHandler extends DefaultHandler{

	private List<WeatherInfo> weatherInfos = null;
	private WeatherInfo weatherInfo = null;
	private CurrentWeatherInfo currentWeatherInfo= null;
	private String tagName="";
	



	public GoogleWeatherHandler(List<WeatherInfo> weatherInfos,
			CurrentWeatherInfo currentWeatherInfo) {
		super();
		this.weatherInfos = weatherInfos;
		this.currentWeatherInfo = currentWeatherInfo;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
	}
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		String data = attributes.getValue(0);
		tagName = localName;
		if(weatherInfo==null){//获取当天的详细天气信息
			if(localName.equals("postal_code")){
				currentWeatherInfo.setPostalCode(data);
			}else if(localName.equals("condition")){
				currentWeatherInfo.setCondition(data);
			}else if(localName.equals("temp_f")){
				currentWeatherInfo.setTempF(data);
			}else if(localName.equals("temp_c")){
				currentWeatherInfo.setTempC(data);
			}else if(localName.equals("humidity")){
				currentWeatherInfo.setHumidity(data);
			}else if(localName.equals("icon")){
				String[] str = data.split("/");
				String name = str[str.length-1];
				String[] a = name.split("\\.");
				String preName = a[0];
				currentWeatherInfo.setIconName(preName);
			}else if(localName.equals("wind_condition")){
				currentWeatherInfo.setWindCondition(data);
			}
		}
		
		
		if(localName.equals("forecast_conditions")){//开始解析今后四天的天气信息包含当天的信息
			weatherInfo = new WeatherInfo();
		}else if(localName.equals("day_of_week")){
			weatherInfo.setDayOfWeek(data);
		}else if(localName.equals("low")){
			weatherInfo.setLowC(data);
		}else if(localName.equals("high")){
			weatherInfo.setHighC(data);
		}else if(weatherInfo!=null&&localName.equals("icon")){//因为之前没对象的话，也有icon标签 所以要过滤
			String[] str = data.split("/");
			String name = str[str.length-1];
			String[] a = name.split("\\.");
			String preName = a[0];
			weatherInfo.setIconName(preName);
		}else if(weatherInfo!=null&&localName.equals("condition")){
			weatherInfo.setCondition(data);
		}
	}

	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		tagName="";
		if(localName.equals("forecast_conditions")){
			weatherInfos.add(weatherInfo);
		}
	}

	

	
	
}

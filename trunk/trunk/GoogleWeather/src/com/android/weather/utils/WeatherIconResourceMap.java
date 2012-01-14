package com.android.weather.utils;

import java.util.HashMap;

import com.android.weather.R;

public class WeatherIconResourceMap {
	private HashMap<String, Integer> drawableIdHash =null;
	
	
	public WeatherIconResourceMap(HashMap<String, Integer> drawableIdHash) {
		super();
		this.drawableIdHash = drawableIdHash;
		setDrawableIdHash();
	}

	private void setDrawableIdHash(){
		
		drawableIdHash.put("chance_of_rain", R.drawable.chance_of_rain);
		drawableIdHash.put("chance_of_snow", R.drawable.chance_of_snow);
		drawableIdHash.put("chance_of_storm", R.drawable.chance_of_storm);
		drawableIdHash.put("chance_of_tstorm", R.drawable.chance_of_tstorm);
		drawableIdHash.put("cloudy", R.drawable.cloudy);
		drawableIdHash.put("dust", R.drawable.dust);
		drawableIdHash.put("flurries", R.drawable.flurries);
		drawableIdHash.put("fog", R.drawable.fog);
		drawableIdHash.put("haze", R.drawable.haze);
		drawableIdHash.put("icy", R.drawable.icy);
		drawableIdHash.put("mist", R.drawable.mist);
		drawableIdHash.put("mostly_cloudy", R.drawable.mostly_cloudy);
		drawableIdHash.put("mostly_sunny", R.drawable.mostly_sunny);
		drawableIdHash.put("partly_cloudy", R.drawable.partly_cloudy);
		drawableIdHash.put("rain", R.drawable.rain);
		drawableIdHash.put("sleet", R.drawable.sleet);
		drawableIdHash.put("smoke", R.drawable.smoke);
		drawableIdHash.put("snow", R.drawable.snow);
		drawableIdHash.put("storm", R.drawable.storm);
		drawableIdHash.put("sunny", R.drawable.sunny);
		drawableIdHash.put("thunderstorm", R.drawable.thunderstorm);
		drawableIdHash.put("cn_fog", R.drawable.cn_fog);
		drawableIdHash.put("cn_heavyrain", R.drawable.cn_heavyrain);
		drawableIdHash.put("cn_lightrain", R.drawable.cn_lightrain);
		drawableIdHash.put("cn_cloudy", R.drawable.cn_cloudy);
		
		
	}

	public int getIconResourceId(String wStr){
		System.out.println(wStr);
		return drawableIdHash.get(wStr);
		
	}
}

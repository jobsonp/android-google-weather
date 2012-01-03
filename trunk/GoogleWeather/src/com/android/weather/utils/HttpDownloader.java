package com.android.weather.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpDownloader {
	/*
	 * ��ϸ�����XML��ͷ����<?xml version="1.0"?>��
	 * ����׼��XMLͷ�����ȱ��������encoding=UTF-8�����ı������������ǻ�������������һ�㵼��SAX����DOM�������ѱ�����UTF
	 * -8���ַ����뵱��UTF-8���������ǵ�����������쳣������google����֤ʵ��ʹ��hl=zh-cnʱ���ص���GBK�����XML��
	 * ����������õ����API��php���붼����GBK->UTF-8��ת������
	 * 
	 * 
	 * ���⵽��������ʵ�ͺܼ��ˣ���Ȼ��GBK����SAX��DOMĬ�ϵ�UTF-8������
	 * �������ǲ�����ȥ����GOOGLE��Servlet��������һ����XMLͷ����encoding
	 * =GBK��XML����ô����ֻ�������취��Ҫ��Ͱѷ��ص�XML��GBK����ת�뵽UTF
	 * -8��Ҫ�����SAX��DOM��������XML��GBK�������ڶ��ַ��������˰���û�ҵ�SAX��DOM��������ܵĺ������������ִӵ�һ�ְ취�������
	 */
	/**
	 * �����ı��ļ� �����ı��е��ַ��� ���汾�� ��ǰ���Ǹ������ϻ�ȡweather
	 * xml�ļ��е�������ʾ���룬��google��ȡ��xml�ļ���GBK����
	 * 
	 * @param urlStr
	 * @return
	 */
	public String downText(String urlStr) {
		URL url;
		BufferedInputStream bis = null;
		try {
			// url = new URL(urlStr.replace(" ", "%20"));
			// URLConnection urlconn = url.openConnection();
			// urlconn.connect();
			// InputStream is = urlconn.getInputStream();
			bis = new BufferedInputStream(
					getInputStreamFromUrl(urlStr));
			ByteArrayBuffer buf = new ByteArrayBuffer(50);
			int read_data = -1;
			while ((read_data = bis.read()) != -1) {
				buf.append(read_data);
			}
//			 String resps = EncodingUtils.getString(buf.toByteArray(), "GBK");
			
			String resp = EncodingUtils.getString(buf.toByteArray(), "GBK");
			return resp;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ex");
			e.printStackTrace();
			return "";
		} finally{
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}
	
	/**
	 * �������ϻ�ȡͼƬ
	 * @param url
	 * @return
	 */
	public Bitmap getImageByURL(String url) {
		BufferedInputStream bis = null;
		Bitmap bm = null;
        try {  
            URL imgURL = new URL(url);  
            URLConnection conn = imgURL.openConnection();  
            conn.connect();  
            InputStream input = conn.getInputStream();  
            
//            HttpClient client = new DefaultHttpClient();
//    		HttpGet get = new HttpGet(url);
//    		InputStream input = client.execute(get).getEntity().getContent();
    		
            bis = new BufferedInputStream(input);  
            bm = BitmapFactory.decodeStream(bis);  
        
        } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
        return bm;  
    }  
	/**
	 * ����URL�õ�������
	 * 
	 * @param urlStr
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr) throws IOException {
		// URL url = new URL(urlStr);
		// HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// InputStream input = urlConn.getInputStream();

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(urlStr);
		InputStream input = client.execute(get).getEntity().getContent();

		return input;
	}
}

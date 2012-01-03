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
	 * 仔细看这个XML的头部，<?xml version="1.0"?>，
	 * 跟标准的XML头部相比缺少了类似encoding=UTF-8这样的编码声明。于是怀疑正是由于这一点导致SAX或者DOM解析器把本不是UTF
	 * -8的字符编码当作UTF-8来处理，于是导致了乱码和异常。经过google搜索证实当使用hl=zh-cn时返回的是GBK编码的XML，
	 * 并且有许多用到这个API的php代码都做了GBK->UTF-8的转换处理。
	 * 
	 * 
	 * 问题到了这里其实就很简单了，既然是GBK编码SAX和DOM默认当UTF-8来处理，
	 * 并且我们不可能去更改GOOGLE的Servlet让他返回一个在XML头部带encoding
	 * =GBK的XML。那么我们只有两个办法，要嘛就把返回的XML从GBK编码转码到UTF
	 * -8，要嘛就让SAX和DOM解析器把XML当GBK来处理。第二种方法我找了半天没找到SAX和DOM有这个功能的函数，于是着手从第一种办法来解决。
	 */
	/**
	 * 下载文本文件 返回文本中的字符串 换版本了 先前的那个从网上获取weather
	 * xml文件中的中文显示乱码，从google获取的xml文件是GBK编码
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
	 * 从网络上获取图片
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
	 * 根据URL得到输入流
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

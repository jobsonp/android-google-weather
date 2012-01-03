package com.android.weather;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.android.weather.adapter.WeatherInfoItemAdapter;
import com.android.weather.model.CurrentWeatherInfo;
import com.android.weather.model.WeatherInfo;
import com.android.weather.utils.AppConstants;
import com.android.weather.utils.GoogleWeatherHandler;
import com.android.weather.utils.HttpDownloader;
import com.android.weather.utils.WeatherIconResourceMap;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.storage.OnObbStateChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GoogleWeatherActivity extends ListActivity {
	/** Called when the activity is first created. */
	private List<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
	private CurrentWeatherInfo currentWeatherInfo = new CurrentWeatherInfo();
	private ListView listView;
	private WeatherInfoItemAdapter adapter;
	private TextView tView;
	
	//��ʼ������ͼ�����Դ����
	private WeatherIconResourceMap weaObj;
	private HashMap<String, Integer> resMap = new HashMap<String, Integer>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = getListView();
		tView = (TextView)findViewById(R.id.address);
		//��ʼ������ͼ�����Դ������hashMap
		weaObj = new WeatherIconResourceMap(resMap);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		// ��������
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				GoogleWeatherActivity.this,
				android.R.layout.simple_spinner_item, AppConstants.city);

		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(adapter);
		// �����¼�
		spinner.setOnItemSelectedListener(new MyItemSelectedListener());
	}

	class MyItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			String cont = parent.getItemAtPosition(pos).toString();
			Toast.makeText(parent.getContext(),cont,Toast.LENGTH_SHORT).show();
			tView.setText("Ŀǰ��ʾ��ַ: "+cont);
			
			//
			clearListView();
			
			CompileXMLTask xmlTask = new CompileXMLTask(parent.getContext());
			xmlTask.execute(cont);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	class CompileXMLTask extends AsyncTask<String, Integer, String> {
		ProgressDialog proDialog = null;
		
		public CompileXMLTask(Context context){
//			proDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
			proDialog = ProgressDialog.show(context, "�Ե�", "����������...");
		}
		
		protected String doInBackground(String... params) {
			
	    	try {
	    		
//				URL url = new URL("http://www.google.com/ig/api?hl=zh-cn&weather="+ params[0]);
	    		//i = url.openStream();
				HttpClient client = new DefaultHttpClient();
	    		HttpGet get = new HttpGet("http://www.google.com/ig/api?hl=zh-cn&weather="+ params[0]);
				InputStream i = client.execute(get).getEntity().getContent();
				
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLReader reader = factory.newSAXParser().getXMLReader();

				reader.setContentHandler(new GoogleWeatherHandler(weatherInfos,currentWeatherInfo));
				//��ȡ�ļ�����������GBK����
				InputStreamReader input = new InputStreamReader(i, "GBK");
				//����xml�ļ�
				reader.parse(new InputSource(input));
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
			return "j";
		}

		@Override
		protected void onPostExecute(String result) {
			showInListView(weatherInfos);
			proDialog.dismiss();
		}

	}
	
	
	
	// ��������Ϣչʾ��ǰ̨listView
	public void showInListView(List<WeatherInfo> weatherInfos) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (Iterator iterator = weatherInfos.iterator(); iterator.hasNext();) {
			WeatherInfo weatherInfo = (WeatherInfo) iterator.next();
			
			
			HashMap<String, String> hash = new HashMap<String, String>();
			
			hash.put("weaDate", weatherInfo.getDayOfWeek());
			hash.put("weaTempScope",weatherInfo.getLowC() + "/" + weatherInfo.getHighC());
			hash.put("icon", weatherInfo.getIconName());
			hash.put("condition", weatherInfo.getCondition());
			list.add(hash);

		}

		adapter = new WeatherInfoItemAdapter(GoogleWeatherActivity.this,
				list, R.layout.weather_list_item, new String[] { "weaDate",
						"weaTempScope", "icon", "condition" }, new int[] {
						R.id.weatherDate, R.id.weatherTempScope,
						R.id.weatherPic, R.id.weatherCondition },weaObj,resMap);
		
		listView.setAdapter(adapter);
	}

	/**
	 * ���֮ǰ��listView����
	 */
	private void clearListView(){
		if(weatherInfos!=null&&adapter!=null){
			weatherInfos.clear();
			adapter.notifyDataSetChanged();
		}
	}
	
}
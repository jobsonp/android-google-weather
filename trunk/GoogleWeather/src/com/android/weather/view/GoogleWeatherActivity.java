package com.android.weather.view;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.weather.R;
import com.android.weather.adapter.WeatherInfoItemAdapter;
import com.android.weather.db.DatabaseManager;
import com.android.weather.model.CurrentWeatherInfo;
import com.android.weather.model.WeatherInfo;
import com.android.weather.utils.AppConstants;
import com.android.weather.utils.GoogleWeatherHandler;
import com.android.weather.utils.WeatherIconResourceMap;

public class GoogleWeatherActivity extends ListActivity {
	/** Called when the activity is first created. */
	private List<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
	private CurrentWeatherInfo currentWeatherInfo = new CurrentWeatherInfo();
	private ListView listView;
	private WeatherInfoItemAdapter adapter;
	private TextView tView;
	private DatabaseManager dbManager;
	
	//初始化天气图像的资源对象
	private WeatherIconResourceMap weaObj;
	private HashMap<String, Integer> resMap = new HashMap<String, Integer>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = getListView();
		tView = (TextView)findViewById(R.id.address);
		dbManager = new DatabaseManager(getApplicationContext());
		
		//初始化天气图像的资源，生成hashMap
		weaObj = new WeatherIconResourceMap(resMap);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		// 城市数据
		List<String> cityListData = getCityData();
		if(cityListData.size()!=0){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					GoogleWeatherActivity.this,
					android.R.layout.simple_spinner_item, cityListData);
//		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
			spinner.setAdapter(adapter);
		}else{
			showAlertDialog("请添加城市！");
		}

		// 触发事件
		spinner.setOnItemSelectedListener(new MyItemSelectedListener());
	}

	class MyItemSelectedListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			String cont = parent.getItemAtPosition(pos).toString();
			Toast.makeText(parent.getContext(),cont,Toast.LENGTH_SHORT).show();
			tView.setText("目前显示地址: "+cont);
			//
			clearListView();
			
			CompileXMLTask xmlTask = new CompileXMLTask(parent.getContext());
			xmlTask.execute(cont);
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	class CompileXMLTask extends AsyncTask<String, Integer, String> {
		ProgressDialog proDialog = null;
		
		public CompileXMLTask(Context context){
			proDialog = ProgressDialog.show(context, "稍等", "更新数据中...");
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
				//获取文件流，并且以GBK编码
				InputStreamReader input = new InputStreamReader(i, "GBK");
				//解析xml文件
				reader.parse(new InputSource(input));
				
			} catch (Exception e) {
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "添加城市");
		menu.add(0, 2, 0, "关于");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId()==1){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NeedShowWethInfoCityListActivity.class);
			startActivity(intent);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	// 将天气信息展示到前台listView
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
	 * 清空之前的listView数据
	 */
	private void clearListView(){
		if(weatherInfos!=null&&adapter!=null){
			weatherInfos.clear();
			adapter.notifyDataSetChanged();
		}
	}
	/**
	 * 读取数据库中存的要查询天气的城市数据
	 * @return
	 */
	private List<String> getCityData(){
		List<String> list = new ArrayList<String>();
		
		if(!dbManager.isTableExists("storecity.db", "storecity")){
			dbManager.execSQL("storecity.db", "create table storecity(_id integer primary key autoincrement,city varchar(20))");
		}
		Cursor cursor = dbManager.openQuery("storecity.db", "select _id,city from storecity");
		while (cursor.moveToNext()) {
			String cityName = cursor.getString(cursor.getColumnIndex("city"));
			list.add(cityName);
		}
		return list;
		
	}
	
	private void showAlertDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("提示信息")
		.setIcon(R.drawable.ic_podcast)
		.setMessage(msg)
		.setPositiveButton("添加", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(GoogleWeatherActivity.this, BackgroundCitySearchActivity.class);
				startActivity(intent);
			}
		});
		
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
}
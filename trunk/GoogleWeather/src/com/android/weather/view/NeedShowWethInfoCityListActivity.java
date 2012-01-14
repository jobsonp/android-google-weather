package com.android.weather.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.weather.R;
import com.android.weather.adapter.CityListItemSimpleAdapter;
import com.android.weather.db.DatabaseHelper;
import com.android.weather.db.DatabaseManager;

import net.youmi.android.AdManager;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class NeedShowWethInfoCityListActivity extends Activity {
	
//	private DatabaseHelper helper = null;
	private ListView listView;
	public static NeedShowWethInfoCityListActivity dcity;
	public static SQLiteDatabase db=null;
	private Button addCity ,finishCity;
	private DatabaseManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//对广告信息初始化
		AdManager.init(getApplicationContext(), "120511ceb41405ad", "7f2e4c0300e37e06", 30, true);
		setContentView(R.layout.need_show_wethinfo_city_listview);
		//导入已有的数据库
		dbManager = new DatabaseManager(getApplicationContext());
		db = dbManager.importDatabase();
		dbManager.closeDB(db);
		
		
		dcity = this;
//		helper = new DatabaseHelper(NeedShowWethInfoCityListActivity.this, "storecity.db");
		
		addCity = (Button)findViewById(R.id.addcity);
		finishCity = (Button)findViewById(R.id.finishcity);
		
		listView = (ListView)findViewById(R.id.storecitylist);
		
		showCityList();
		addCity.setOnClickListener(new AddCityButtonClickListener());
		finishCity.setOnClickListener(new FinishCityButtonClickListener());
	}
	
	class AddCityButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), BackgroundCitySearchActivity.class);
			startActivity(intent);
		}
		
	}
	class FinishCityButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GoogleWeatherActivity.class);
			startActivity(intent);
		}
	}
	
	
	public void showCityList(){
//		db =dbManager.openOrCreateDatabase("storecity.db");
		if(!dbManager.isTableExists("storecity.db", "storecity")){
			dbManager.execSQL("storecity.db", "create table storecity(_id integer primary key autoincrement,city varchar(20))");
		}
		//获取城市列表，显示到listview中
		Cursor cursor = dbManager.openQuery("storecity.db", "select _id,city from storecity");
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		while(cursor.moveToNext()){
			HashMap<String, Object> hash = new HashMap<String, Object>();
			String cityName = cursor.getString(cursor.getColumnIndex("city"));
			hash.put("cityname", cityName);
			hash.put("btn1","");
			hash.put("delbtn", "删除");
			list.add(hash);
			
		}
		CityListItemSimpleAdapter listadapter = new CityListItemSimpleAdapter(NeedShowWethInfoCityListActivity.this, list, R.layout.need_show_wethinfo_city_listview_item,
				new String[]{"cityname","btn1","delbtn"}, new int[]{R.id.storeListview,R.id.button1,R.id.delbutton,R.id.imageview1});
		
//		SimpleCursorAdapter listadapter = new SimpleCursorAdapter(DBCityList.this, R.layout.storcity_listview_item, cursor,
//				new String[]{"city"}, new int[]{R.id.storeListview});
		
		listView.setAdapter(listadapter);
//		dbManager.closeDB(db);
//		listView.setOnItemClickListener(listener)
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		if(db!=null){
//			db.close();
//		}
//		if(helper!=null){
//			helper.close();

	}
	
		
}

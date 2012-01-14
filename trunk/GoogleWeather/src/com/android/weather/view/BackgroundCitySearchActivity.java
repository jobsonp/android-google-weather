package com.android.weather.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.android.weather.R;
import com.android.weather.custom.widget.EditCancel;
import com.android.weather.db.DatabaseHelper;
import com.android.weather.db.DatabaseManager;
import com.android.weather.model.Result;
import com.android.weather.model.SubResult;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BackgroundCitySearchActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText etext;
	private ImageButton imageBtn;
	private Button gpsBtn,localCitySearch;
	private DatabaseHelper helper,helper1 ;
	private GridView gridView;
	private ListView listView;
	private String queryString ;
	private SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_city_search);
        
//        helper = new DatabaseHelper(TestSqlliteActivity.this, "city.db3"); 
        helper1 = new DatabaseHelper(BackgroundCitySearchActivity.this, "storecity.db");
        
        EditCancel ec = (EditCancel)findViewById(R.id.customedit);
        gpsBtn = (Button)findViewById(R.id.gpsSearch);
        localCitySearch = (Button)findViewById(R.id.localSearch);
        
        gridView = (GridView)findViewById(R.id.gridview);
        listView = (ListView)findViewById(R.id.listview);
        
       
        
        
        //搜索框中的控件对象
        etext = ec.geteText();
        imageBtn = ec.getImageBtn();
        etext.addTextChangedListener(tw);//注册监听文字改变的监听器
        
        imageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideCancelBtn();
				etext.setText("");
			}
		});
        //gps定位事件
        gpsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GetAddressTask task = new GetAddressTask();
				queryString = etext.getText().toString();
				task.execute(queryString);
			}
		});
        //查询本地数据库城市名
        localCitySearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = etext.getText().toString();
				if(s.length()!=0){
//	 				SQLiteDatabase db = helper.getReadableDatabase();
	 				db = SQLiteDatabase.openOrCreateDatabase(DatabaseManager.DB_PATH+File.separator+DatabaseManager.DB_NAME, null);
	 				
	 				Cursor cursor = db.query("city", new String[]{"_id","city","province"}, "city like '"+s+"%' or pinyin like '"+s+"%'", null, null, null, null);
	 				
	 				SimpleCursorAdapter adapter = new SimpleCursorAdapter(BackgroundCitySearchActivity.this, R.layout.database_sel_grid_list_item,
	 						cursor, new String[]{"city","province"}, new int[]{R.id.cityname,R.id.provincename});
	 				
	 				gridView.setVisibility(View.VISIBLE);
	 				listView.setVisibility(View.GONE);
	 				gridView.setAdapter(adapter);
//	 				while(cursor.moveToNext()){
//	 					String name = cursor.getString(cursor.getColumnIndex("city"));
//	 					String proname = cursor.getString(cursor.getColumnIndex("province"));
//	 					System.out.println("name is "+ name+"  province is:"+proname);
//	 				}
	 				if(db!=null){
	 		    		db.close();
	 		    	}
	 			}else{
	 				Toast.makeText(getApplicationContext(), "请输入查询内容！", Toast.LENGTH_SHORT).show();
	 			}
			}
		});
        //listview 点击事件
        listView.setOnItemClickListener(new ListViewItemClickListener());
      //gridview 小项目点击事件
        gridView.setOnItemClickListener(new GridViewItemClickListener());
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if(db!=null){
    		db.close();
    	}
    	if(helper1!=null){
    		helper1.close();
    	}
    }
    
  //listview 点击事件
    class ListViewItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap<String, String> hash = (HashMap<String, String>)listView.getItemAtPosition(position);
			String str  = hash.get("address");
			String[] content = str.split(" - ");
			
			ContentValues values = new ContentValues();
			values.put("city", content[1]);
			
			SQLiteDatabase db = helper1.getWritableDatabase();
			Cursor cursor = db.rawQuery("select _id from storecity where city=?", new String[]{content[1]});
			if(cursor.getCount()==0){
				long i = db.insert("storecity", null, values);
				if(i!=-1){
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), NeedShowWethInfoCityListActivity.class);
					startActivity(intent);
					Toast.makeText(BackgroundCitySearchActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(BackgroundCitySearchActivity.this, "该城市已经添加过，请勿重复添加", Toast.LENGTH_SHORT).show();
			}
		}
    	
    }
    class GridViewItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView tv = (TextView)view.findViewById(R.id.cityname);
			String s = tv.getText().toString();
			
			ContentValues values = new ContentValues();
			values.put("city", s);
			
			SQLiteDatabase db = helper1.getWritableDatabase();
			Cursor cursor = db.rawQuery("select _id from storecity where city=?", new String[]{s});
			if(cursor.getCount()==0){
				long i = db.insert("storecity", null, values);
				if(i!=-1){
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), NeedShowWethInfoCityListActivity.class);
					startActivity(intent);
					Toast.makeText(BackgroundCitySearchActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
					
				}
			}else{
				Toast.makeText(BackgroundCitySearchActivity.this, "该城市已经添加过，请勿重复添加", Toast.LENGTH_SHORT).show();
			}
		}
    	
    }
    
    /**
     * 使用google map定位
     * @author Administrator
     *
     */
    class GetAddressTask extends AsyncTask<String, Integer, String>{
//    	String url = "http://maps.google.com/maps/api/geocode/json?latlng=40.714224,-73.961452&sensor=true";
//    	String url = "http://maps.google.com/maps/api/geocode/json?address=qingdao&sensor=false";
//    	String url = "http://www.baidu.com";
    	String addr = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "http://maps.google.com/maps/api/geocode/json?language=zh-CN&address="+params[0]+"&sensor=false";
			
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			try {
				InputStream input = client.execute(get).getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String line = null;
				while((line=reader.readLine())!=null){
					addr += line;
				}
				System.out.println(addr);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return addr;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			String str = "";
			Gson gson = new Gson();
			Result r = gson.fromJson(result, Result.class);
			List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
			
			for (Iterator iterator = r.getResults().iterator(); iterator.hasNext();) {
				SubResult s = (SubResult) iterator.next();
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("address", queryString+" - "+s.getFormatted_address());
				list.add(hash);
//				str +="地址是："+s.getFormatted_address()+"   ";
			}
			SimpleAdapter listadapter = new SimpleAdapter(BackgroundCitySearchActivity.this, list, R.layout.google_location_list_view_item,
					new String[]{"address"}, new int[]{R.id.textView1});
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			listView.setAdapter(listadapter);
		} 
		
    }
    
 // 当输入框状态改变时，会调用相应的方法
 	TextWatcher tw = new TextWatcher() {
 		
 		@Override
 		public void onTextChanged(CharSequence s, int start, int before, int count) {
 		}
 		
 		@Override
 		public void beforeTextChanged(CharSequence s, int start, int count,
 				int after) {
 		}
 		// 在文字改变后调用
 		@Override
 		public void afterTextChanged(Editable s) {
 			if(s.length()==0){
 				hideCancelBtn();
 			}else{
 				showCancelBtn();
 			}
 		}
 	};
 	
 	
 	//显示按钮
 	public void showCancelBtn(){
 		imageBtn.setVisibility(View.VISIBLE);
 	}
 	//隐藏按钮
 	public void hideCancelBtn(){
 		imageBtn.setVisibility(View.GONE);
 	}
}
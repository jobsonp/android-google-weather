/**
 * 
 */
package com.android.weather.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.weather.R;
import com.android.weather.view.NeedShowWethInfoCityListActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * @author Himi
 * 
 */
public class CityListItemSimpleAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<HashMap<String, Object>> list;
	private int layoutID;
	private String flag[];
	private int ItemIDs[];
	private Context context = null;
	
	public CityListItemSimpleAdapter(Context context, List<HashMap<String, Object>> list,
			int layoutID, String flag[], int ItemIDs[]) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.layoutID = layoutID;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(layoutID, null);
		for (int i = 0; i < flag.length; i++) {//±¸×¢1
			if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {
				ImageView iv = (ImageView) convertView.findViewById(ItemIDs[i]);
				iv.setBackgroundResource((Integer) list.get(position).get(
						flag[i]));
			} else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {
				TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
				tv.setText((String) list.get(position).get(flag[i]));
			}else{
				//...±¸×¢2
			}
		}
		addListener(convertView);
		return convertView;
	}
/**
 * 
 * ±¸×¢3
 */
	public void addListener(View convertView) {
		final Button btn1 = (Button)convertView.findViewById(R.id.button1);//Ðý×ª°´Å¥
		final Button delBtn = (Button)convertView.findViewById(R.id.delbutton);//É¾³ý°´Å¥
		final ImageView imageview = (ImageView)convertView.findViewById(R.id.imageview1);//listÍ¼Æ¬
		final TextView tview = (TextView)convertView.findViewById(R.id.storeListview);
		btn1.setOnClickListener(
				new View.OnClickListener() {
					int count = 0;
					@Override
					public void onClick(View v) {
						Toast.makeText(NeedShowWethInfoCityListActivity.dcity, "click", Toast.LENGTH_SHORT).show();
//						AnimationSet aniSet = new AnimationSet(true);
//						aniSet.setInterpolator(new DecelerateInterpolator());
						if(count == 0){//ÏÔÊ¾É¾³ý°´Å¥
							RotateAnimation rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
							TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f,Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF, 0f);
//							AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
							
							rotate.setDuration(800);
							rotate.setFillAfter(true);
							translate.setDuration(10);
							translate.setFillAfter(true);
//							alpha.setDuration(800);
//							alpha.setFillAfter(true);
							
							btn1.startAnimation(rotate);
							
							delBtn.setVisibility(View.VISIBLE);
							imageview.setVisibility(View.GONE);
							delBtn.startAnimation(translate);
//							delBtn.startAnimation(alpha);
							
							count += 1;
						}else if(count==1){//Òþ²ØÉ¾³ý°´Å¥
							RotateAnimation rotate = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
							TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF, 0f);
//							AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
							AlphaAnimation imagealpha = new AlphaAnimation(0f, 1f);
							
							rotate.setDuration(800);
							rotate.setFillAfter(true);
//							alpha.setDuration(400);
//							alpha.setFillAfter(true);
							translate.setDuration(400);
							translate.setFillAfter(true);
							imagealpha.setDuration(400);
							imagealpha.setFillAfter(true);
							
							
							btn1.startAnimation(rotate);
							
//							delBtn.startAnimation(alpha);
							delBtn.startAnimation(translate);
							delBtn.setVisibility(View.GONE);
							imageview.startAnimation(imagealpha);
							imageview.setVisibility(View.VISIBLE);
							
							count = 0;
						}
					}
				});
		delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int i = NeedShowWethInfoCityListActivity.db.delete("storecity", "city=?", new String[]{tview.getText().toString()});
				if(i!=-1){
					
					Intent intent = new Intent();
					intent.setClass(NeedShowWethInfoCityListActivity.dcity, NeedShowWethInfoCityListActivity.class);
					NeedShowWethInfoCityListActivity.dcity.startActivity(intent);
					
//					DBCityList.dcity.setContentView(R.layout.db_city_list);//²»ÐÐ
					Toast.makeText(NeedShowWethInfoCityListActivity.dcity, "delete success", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
//		((Button)convertView.findViewById(R.id.delbutton)).setOnClickListener(
//				new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
	}
}
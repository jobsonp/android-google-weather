package com.android.weather.custom.widget;


import com.android.weather.R;
import com.android.weather.db.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class EditCancel extends RelativeLayout {

	EditText eText;
	ImageButton imageBtn;
	DatabaseHelper helper;
	
	public EditCancel(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.edit_cancel_symbol, this, true);
		
		eText = (EditText)findViewById(R.id.edit);
		imageBtn = (ImageButton)findViewById(R.id.imagebtn);
//		init();
	}
	
	
	
	public ImageButton getImageBtn() {
		return imageBtn;
	}


	public EditText geteText() {
		return eText;
	}


	
/*	
	
	private void init(){
		eText = (EditText)findViewById(R.id.edit);
		imageBtn = (ImageButton)findViewById(R.id.imagebtn);
		
		helper = new DatabaseHelper(EditCancel.this.getContext(), "city.db3");
		//Ϊ�������Ӽ������ֱ仯�ļ�����
		eText.addTextChangedListener(tw);
		
		imageBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideCancelBtn();
				eText.setText("");
			}
		});
	}
	
	
	public void setEditText(String content){
		eText.setText(content);
	}

	// �������״̬�ı�ʱ���������Ӧ�ķ���
	TextWatcher tw = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if(s.length()!=0){
				SQLiteDatabase db = helper.getReadableDatabase();
				
				Cursor cursor = db.query("city", new String[]{"city","province"}, "city like '"+s+"%' or pinyin like '"+s+"%'", null, null, null, null);
				while(cursor.moveToNext()){
					String name = cursor.getString(cursor.getColumnIndex("city"));
					System.out.println("name is "+ name);
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		// �����ָı�����
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(s.length()==0){
				hideCancelBtn();
			}else{
				showCancelBtn();
			}
		}
	};
	
	
	//��ʾ��ť
	public void showCancelBtn(){
		imageBtn.setVisibility(View.VISIBLE);
	}
	//���ذ�ť
	public void hideCancelBtn(){
		imageBtn.setVisibility(View.GONE);
	}
	
	*/
}

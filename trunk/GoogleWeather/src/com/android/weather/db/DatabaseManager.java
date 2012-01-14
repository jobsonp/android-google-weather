package com.android.weather.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.android.weather.R;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DatabaseManager{
    private final int BUFFER_SIZE = 4*1024;
    public static final String DB_NAME = "city.db3"; //��������ݿ��ļ���
    public static final String PACKAGE_NAME = "com.android.weather";//����
    public static final String DB_PATH = "/data"
               + Environment.getDataDirectory().getAbsolutePath() + File.separator + PACKAGE_NAME+File.separator+"databases";  //���ֻ��������ݿ��λ��

    private SQLiteDatabase database;
    private Context context;

    public DatabaseManager(Context context) {
            this.context = context;
    }

    public void openDatabase() {
            this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
            try {
                    if (!(new File(dbfile).exists())) {//�ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
                            InputStream is = this.context.getResources().openRawResource(R.raw.city); //����������ݿ�
                            FileOutputStream fos = new FileOutputStream(dbfile);
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int count = 0;
                            while ((count = is.read(buffer)) > 0) {
                                    fos.write(buffer, 0, count);
                            }
                            fos.close();
                            is.close();
                    }
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
                    return db;
            } catch (FileNotFoundException e) {
                    Log.e("Database", "File not found");
                    e.printStackTrace();
            } catch (IOException e) {
                    Log.e("Database", "IO exception");
                    e.printStackTrace();
            }
            return null;
    }
    
    public  void close(){
    	if(database!=null){
    		database.close();
    	}
    }
}
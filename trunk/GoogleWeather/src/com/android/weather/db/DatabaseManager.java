package com.android.weather.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.android.weather.R;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
/**
 * 
 * @author tps
 *
 */
public class DatabaseManager{
    private final int BUFFER_SIZE = 4*1024;
    public static final String SQLite_MASTER_TABLE = "sqlite_master";
    public static final String DB_NAME = "city.db3"; //�������е����ݿ��ļ���
    public static final String PACKAGE_NAME = "com.android.weather";//����
    public static final String DB_PATH = "/data"
               + Environment.getDataDirectory().getAbsolutePath() + File.separator + PACKAGE_NAME+File.separator+"databases";  //���ֻ��������ݿ��λ��

    private SQLiteDatabase database;
    private Context context;

    public DatabaseManager(Context context){
            this.context = context;
    }
    /**
     * �򿪻򴴽����ݿ�---�������ļ��еĴ���
     * @param dbName 
     * @return
     */
    public SQLiteDatabase openOrCreateDatabase(String dbName){
//    	return SQLiteDatabase.openDatabase(DB_PATH + "/"+dbName, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    	 String daNameStr = DB_PATH + "/"+dbName;
    	 File file = new File(daNameStr);
         if (file.exists() == true) {
             return SQLiteDatabase.openDatabase(daNameStr, null, SQLiteDatabase.OPEN_READWRITE);
         }else {
             File datafiledir = file.getParentFile();
             if(!datafiledir.exists()){
                 datafiledir.mkdirs();
             }
             return SQLiteDatabase.openOrCreateDatabase(daNameStr, null);
         }
    }
    /**
     * ���ض����ݿ�ִ��sql���
     * @param dbName
     * @param sql
     */
    public void execSQL(String dbName, String sql) {
    	SQLiteDatabase db = openOrCreateDatabase(dbName);
    	db.execSQL(sql);
    	closeDB(db);
    } 
    
    
    /**
     * �����е����ݿ⵼�뵽ָ��λ�� --�������ݿ�
     */
    public SQLiteDatabase importDatabase() {
           return this.database = this.importDatabase(DB_PATH + "/" + DB_NAME);
    }
    

    private SQLiteDatabase importDatabase(String dbfile) {
            try {
                if (!(new File(dbfile).exists())) {//�ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
            		//�������ݿ��ļ���һ����ļ���
            		File file = new File(dbfile);
            		File datafiledir = file.getParentFile();
                    if(!datafiledir.exists()){
                         datafiledir.mkdirs();
                    }
                    //����������ݿ�
                    InputStream is = this.context.getResources().openRawResource(R.raw.city); 
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
    
    public  void closeDB(SQLiteDatabase db){
    	if(db!=null){
    		db.close();
    	}
    }
    /**
     * ��ѯ����
     * @param dbName   ���ݿ�����
     * @param sql   ԭ����ѯ���
     * @return
     */
    public Cursor openQuery(String dbName, String sql) {
        SQLiteDatabase db = openOrCreateDatabase(dbName);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();//
        closeDB(db);
        return cursor;
    }
    /**
     * ��ѯ����---������
     * @param dbName    ���ݿ�����
     * @param tableName  Ҫ�����ı���
     * @param condStr    ��ѯ������ʽ(�ֶ�=����)  ʵ����(id=1) �� (name = 'tps')
     * @return
     */
    public Cursor openQuery(String dbName, String tableName, String condStr) {
        SQLiteDatabase db = openOrCreateDatabase(dbName);
        Cursor cursor = db.query(tableName, null, condStr, null, null, null, null);
        cursor.moveToFirst();
        closeDB(db);
        return (cursor);
    }
    /**
     * �ж��Ƿ���ڴ���ı�
     * @param dbName   ���ݿ�����
     * @param tableName  Ҫ�жϵı���  true--��ʾ����
     * @return
     */
    public boolean isTableExists(String dbName, String tableName) {
        Cursor cursor = openQuery(dbName, SQLite_MASTER_TABLE, "(tbl_name='" + tableName + "')");
        int recordCount = cursor.getCount();
        cursor.close();
        return (recordCount > 0);
    }
    /**
     * �ж����ݿ��Ƿ����
     * @param dbName
     * @return
     */
    public boolean isDatabaseExists(String dbName){
        return new File(dbName).exists();
    }
    /**
     * ��������
     * @param dbName
     * @param tableName
     * @param values
     * @return
     */
    public long insert(String dbName,String tableName,ContentValues values){
    	SQLiteDatabase db = openOrCreateDatabase(dbName);
    	long l = db.insert(tableName, null, values);
    	closeDB(db);
		return l;
    	
    }
    /**
     * ɾ������
     * @param dbName
     * @param tableName
     * @param whereClause     �����Ӿ�--- ��ʽ��_id=?
     * @param whereArgs    where�Ӿ�  ����        
     * @return
     */
    public long delete(String dbName,String tableName,String whereClause,String whereArgs ){
    	SQLiteDatabase db = openOrCreateDatabase(dbName);
    	long l = db.delete(tableName, whereClause, new String[]{whereArgs});
    	closeDB(db);
    	return l;
    }
    
    
}
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
    public static final String DB_NAME = "city.db3"; //保存已有的数据库文件名
    public static final String PACKAGE_NAME = "com.android.weather";//包名
    public static final String DB_PATH = "/data"
               + Environment.getDataDirectory().getAbsolutePath() + File.separator + PACKAGE_NAME+File.separator+"databases";  //在手机里存放数据库的位置

    private SQLiteDatabase database;
    private Context context;

    public DatabaseManager(Context context){
            this.context = context;
    }
    /**
     * 打开或创建数据库---屏蔽了文件夹的创建
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
     * 对特定数据库执行sql语句
     * @param dbName
     * @param sql
     */
    public void execSQL(String dbName, String sql) {
    	SQLiteDatabase db = openOrCreateDatabase(dbName);
    	db.execSQL(sql);
    	closeDB(db);
    } 
    
    
    /**
     * 将已有的数据库导入到指定位置 --拷贝数据库
     */
    public SQLiteDatabase importDatabase() {
           return this.database = this.importDatabase(DB_PATH + "/" + DB_NAME);
    }
    

    private SQLiteDatabase importDatabase(String dbfile) {
            try {
                if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            		//生成数据库文件上一层的文件夹
            		File file = new File(dbfile);
            		File datafiledir = file.getParentFile();
                    if(!datafiledir.exists()){
                         datafiledir.mkdirs();
                    }
                    //欲导入的数据库
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
     * 查询函数
     * @param dbName   数据库名字
     * @param sql   原生查询语句
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
     * 查询函数---传参数
     * @param dbName    数据库名字
     * @param tableName  要操作的表名
     * @param condStr    查询条件格式(字段=条件)  实例：(id=1) 或 (name = 'tps')
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
     * 判断是否存在传入的表
     * @param dbName   数据库名称
     * @param tableName  要判断的表名  true--表示存在
     * @return
     */
    public boolean isTableExists(String dbName, String tableName) {
        Cursor cursor = openQuery(dbName, SQLite_MASTER_TABLE, "(tbl_name='" + tableName + "')");
        int recordCount = cursor.getCount();
        cursor.close();
        return (recordCount > 0);
    }
    /**
     * 判断数据库是否存在
     * @param dbName
     * @return
     */
    public boolean isDatabaseExists(String dbName){
        return new File(dbName).exists();
    }
    /**
     * 插入数据
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
     * 删除数据
     * @param dbName
     * @param tableName
     * @param whereClause     条件子句--- 格式：_id=?
     * @param whereArgs    where子句  参数        
     * @return
     */
    public long delete(String dbName,String tableName,String whereClause,String whereArgs ){
    	SQLiteDatabase db = openOrCreateDatabase(dbName);
    	long l = db.delete(tableName, whereClause, new String[]{whereArgs});
    	closeDB(db);
    	return l;
    }
    
    
}
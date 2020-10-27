package com.rom471.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class RecordDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "apptime";

    private String db_name;
    public static final int DB_VERSION=1;
    //内部Context引用
    private Context context;
    //内部数据库引用
    private SQLiteDatabase db;

    public RecordDBHelper(Context context, String db_name){
        super(context,db_name,null,DB_VERSION);
        this.context=context;
        this.db_name=db_name;
        db=super.getWritableDatabase();
        createTable();
    }

    public void createTable(){
        String sql="create table if not exists "+TABLE_NAME+" ("
                + "id integer primary key autoincrement,"
                + "appname text ,"
                + "time DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertRecord(String app_name){
        ContentValues contentValues=new ContentValues();
        //contentValues.put("id",user.getId());
        contentValues.put("appname",app_name);

        db.insert(TABLE_NAME,null,contentValues);
    }
    public void clearRecords(){
        String drop_sql="drop table "+TABLE_NAME;
        db.execSQL(drop_sql);
        createTable();

    }
    public List<Record> queryAll(){
        List<Record> records=new ArrayList<>();
        String sql = "SELECT appname,time FROM "+TABLE_NAME +" ";
        Cursor cursor=db.rawQuery(sql,null);

        while(cursor.moveToNext()){
            String appname=cursor.getString(cursor.getColumnIndex("appname"));
            String datatime=cursor.getString(cursor.getColumnIndex("time"));

            Record record=new Record();
            record.setAppname(appname);
            record.setDatatime(datatime);

            records.add(record);


        }
        cursor.close();
        return records;
    }
}

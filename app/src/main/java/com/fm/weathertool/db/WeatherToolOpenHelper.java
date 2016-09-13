package com.fm.weathertool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FM on 2016/9/12.
 */
public class WeatherToolOpenHelper extends SQLiteOpenHelper{
    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVICE = "create table Province ("
            +"id integer primary key autoincrement,"
            +"province_name text,"
            +"province_code text)";
    /**
     * City表建表语句
     */
    public static final String CREATE_CITY = "create table City("
            +"id integer primary autoincrement,"
            +"city_name text,"
            +"city_code text,"
            +"province_id integer)";

    /**
     *County表建表语句
     */
    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id integer)";


    public WeatherToolOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVICE); // 创建Province表
        db.execSQL(CREATE_CITY);    //创建City表
        db.execSQL(CREATE_COUNTY);  //创建County表

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.coolweather.app.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	/*
	 * Province表建表语句
	 */
	public static final String CREAT_PROVINCE = "Create table Province("
			+"id integer primary key autoincrement,"
			+"province_name text,"
			+"province_code text)";
		
	/*
	 * City表建表语句
	 */
	public static final String CREAT_CITY = "Create table City("
			+"id integer primary key autoincrement,"
			+"city_name text,"
			+"city_code text,"
			+"province_id integer)";
	
	/*
	 * Country表建表语句
	 */
	public static final String CREAT_COUNTRY = "Create table Country("
			+"id integer primary key autoincrement,"
			+"country_name text,"
			+"country_code text,"
			+"city_id integer)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAT_PROVINCE);//创建province表
		db.execSQL(CREAT_CITY);//创建city表
		db.execSQL(CREAT_COUNTRY);//创建country表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

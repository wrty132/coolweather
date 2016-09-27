package com.coolweather.app.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	/*
	 * Province�������
	 */
	public static final String CREAT_PROVINCE = "Create table Province("
			+"id integer primary key autoincrement,"
			+"province_name text,"
			+"province_code text)";
		
	/*
	 * City�������
	 */
	public static final String CREAT_CITY = "Create table City("
			+"id integer primary key autoincrement,"
			+"city_name text,"
			+"city_code text,"
			+"province_id integer)";
	
	/*
	 * Country�������
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
		db.execSQL(CREAT_PROVINCE);//����province��
		db.execSQL(CREAT_CITY);//����city��
		db.execSQL(CREAT_COUNTRY);//����country��
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

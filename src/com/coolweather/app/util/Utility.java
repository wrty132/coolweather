package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

public class Utility {
	/*
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvinceResponse(
			CoolWeatherDB coolWeatherDB, String response) {

		if (!TextUtils.isEmpty(response)) {
			String[] allProvince = response.split(",");
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// �����ݴ洢��Province����
					coolWeatherDB.saveProvince(province);
				}
			}
		}
		return false;
	}

	/*
	 * �����ʹ�����������صĳ�������
	 */
	public synchronized static boolean handlerCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					// �����ݴ洢��City����
					coolWeatherDB.saveCity(city);
				}

			}
		}
		return false;

	}

	/*
	 * �����ʹ�����������صĳ�������
	 */
	public synchronized static boolean handlerCountriesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountries = response.split(",");
			if (allCountries != null && allCountries.length > 0) {
				for (String c : allCountries) {
					String[] array = c.split("\\|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					// �����ݴ洢��Country����
					coolWeatherDB.saveCountry(country);
				}

			}
		}

		return false;

	}

	/*
	 * �������������ص�JSON���ݣ��������ݴ洢������
	 */
	public static void handlerWeatherResponse(Context context, String response) {

		try {

			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");

			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");

			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/*
	 * �����������ص��������ݴ洢��sp�ļ���
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_data", sdf.format(new Date()));
		editor.commit();

	}

}

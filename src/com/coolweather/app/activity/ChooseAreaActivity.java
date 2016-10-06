package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.R.anim;
import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	private static final int LEVEL_PROVINCE = 0;
	private static final int LEVEL_CITY = 1;
	private static final int LEVEL_COUNTRY = 2;
	private ListView listView;
	private TextView titleText;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	private ProgressDialog progressDialog;

	private List<Province> provinceList;// ʡ�б�
	private List<City> cityList;// �����б�
	private List<Country> countryList;// ���б�

	private Province selectedProvince;// ��ѡ�е�ʡ��
	private City selectedCity;// ��ѡ�е�ʡ��
	private Country selectedCountry;// ��ѡ�е�ʡ��

	private int currentLevel;// ��ǰѡ�еļ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ز���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		// �ҵ��ؼ�
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		// ����listView������
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		coolWeatherDB = CoolWeatherDB.getInstance(this);
		// ΪlistViewItem���õ���¼�
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCountries();
				}

			}

		});
		queryProvinces();
	}

	// ��ѯȫ��ʡ�ݣ����ȴ����ݿ��ѯ�����û����ȥ��������ѯ
	private void queryProvinces() {

		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {

			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}

	}

	// ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û����ȥ��������ѯ
	private void queryCountries() {

		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}

	}

	// ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û����ȥ��������ѯ
	private void queryCities() {

		countryList = coolWeatherDB.loadCountries(selectedCity.getId());
		if (countryList.size() > 0) {
			dataList.clear();
			for (Country country : countryList) {
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY;

		} else {
			queryFromServer(selectedCity.getCityCode(), "country");
		}
	}

	// ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯ����
	private void queryFromServer(final String code, final String type) {

		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}

		showProgressDialog();

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(coolWeatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handlerCitiesResponse(coolWeatherDB,
							response, selectedProvince.getId());
				} else if ("country".equals(type)) {
					result = Utility.handlerCountriesResponse(coolWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("country".equals(type))
								queryCountries();

						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", 0).show();
					}
				});
			}
		});

	}

	// ��ʾ�������Ի���
	private void showProgressDialog() {
		if (progressDialog == null) {
			
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ�����....");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	// �رս������Ի���
	private void closeProgressDialog() {
		if (progressDialog != null) {
			
			progressDialog.dismiss();
			
		}
	}

	// ����back�������ݵ�ǰ�����жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳�
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTRY) {
			queryCities();
		}else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		}else {
			finish();
		}
	}
	
	
}

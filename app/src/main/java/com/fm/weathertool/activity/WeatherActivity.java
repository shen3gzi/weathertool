package com.fm.weathertool.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fm.weathertool.R;
import com.fm.weathertool.service.AutoUpdateService;
import com.fm.weathertool.util.HttpCallbackListener;
import com.fm.weathertool.util.HttpUtil;
import com.fm.weathertool.util.Utility;

/**
 * Created by FM on 2016/9/28.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    //API个人认证Key
    public static final String Key = "e6e6d3429669495980a1142ffe5379d1";
    private LinearLayout weatherInfoLayout;
    /**
     * 用于显示城市名
     */
    private TextView cityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView publishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView temp1Text;
    /**
     * 用于显示气温2
     */
    private TextView temp2Text;
    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private ImageButton switchCity;
    /**
     * 更新天气按钮
     */
    private ImageButton refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        // 初始化各控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        switchCity = (ImageButton) findViewById(R.id.chooseBtn);
        refreshWeather = (ImageButton) findViewById(R.id.refreshBtn);

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);


        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
    // 有县级代号时就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
    // 没有县级代号时就直接显示本地天气
            showWeather();
        }
    }


    /**
     * 查询县级代号所对应的天气代号。
     */
    private void queryWeatherCode(String countyCode) {
        String address = "https://api.heweather.com/x3/weather?cityid="+countyCode+"&key="+Key;
        queryFromServer(address);
    }
    /**
     * 根据传入的地址去向服务器查询天气信息
     */
    private void queryFromServer(final String address) {

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                // 处理服务器返回的天气信息
                Utility.handleWeatherResponse(WeatherActivity.this,
                        response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });

            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        cityNameText.setText( prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }


    //刷新天气情况按钮和选择城市按钮点击事件
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.chooseBtn:
                    Intent intent = new Intent(this,ChooseAreaActivity.class);
                    intent.putExtra("from_weatherActivity",true);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.refreshBtn:
                    publishText.setText("同步中...");
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                    String weatherCode = pref.getString("weather_code","");
                    if (!TextUtils.isEmpty(weatherCode)) {
                        queryWeatherCode(weatherCode);
                    }
                    break;
                default:
                    break;

            }
    }
}

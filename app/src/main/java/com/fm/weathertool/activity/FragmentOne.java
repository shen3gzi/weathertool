package com.fm.weathertool.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fm.weathertool.R;
import com.fm.weathertool.util.Utility;

import java.util.ArrayList;

/**
 * Created by FM on 2017/3/20.
 */
public class FragmentOne extends Fragment{
    private TextView timeText;
    private TextView nowTemp;
    private TextView nowWeather;
    private ImageView nowWeatherImg;
    private TextView feelTemp;
    private TextView humidity;
    private TextView visibility;
    private ImageView todayWeatherImg;
    private TextView todayWeatherTemp;
    private TextView todayDate;
    private ImageView tomorrowWeatherImg;
    private TextView tomorrowWeatherTemp;
    private TextView tomorrowDate;
    private ArrayList<TextView> tvList = new ArrayList<>();
    private ArrayList<ImageView> imgList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.table1,container,false);
        nowWeatherImg = (ImageView) v.findViewById(R.id.table1_nowWeatherImg);
        todayWeatherImg = (ImageView) v.findViewById(R.id.table1_TodayWeatherImg);
        tomorrowWeatherImg = (ImageView) v.findViewById(R.id.table1_TomorrowWeatherImg);
        timeText = (TextView) v.findViewById(R.id.table1_timeTxt);
        nowTemp = (TextView) v.findViewById(R.id.table1_nowTemp);
        nowWeather = (TextView) v.findViewById(R.id.table1_nowWeather);
        feelTemp = (TextView) v.findViewById(R.id.table1_feelTemp);
        humidity = (TextView) v.findViewById(R.id.table1_humidity);
        visibility = (TextView) v.findViewById(R.id.table_visibility);
        todayWeatherTemp = (TextView) v.findViewById(R.id.table1_TodayWeatherTemp);
        todayDate = (TextView) v.findViewById(R.id.table1_TodayDate);
        tomorrowWeatherTemp = (TextView) v.findViewById(R.id.table1_TomorrowTemp);
        tomorrowDate = (TextView) v.findViewById(R.id.table1_TomorrowDate);

        tvList.add(timeText);
        tvList.add(nowTemp);
        tvList.add(nowWeather);
        tvList.add(feelTemp);
        tvList.add(humidity);
        tvList.add(visibility);
        tvList.add(todayWeatherTemp);
        tvList.add(tomorrowWeatherTemp);

        imgList.add(nowWeatherImg);
        imgList.add(todayWeatherImg);
        imgList.add(tomorrowWeatherImg);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        refreshView(preferences);
        return v;
    }


    public void refreshView(SharedPreferences prefs){
        ArrayList<String> tableList = new ArrayList<>();
        tableList.add(prefs.getString("currentdate",""));
        tableList.add(prefs.getString("nowtmp",""));
        tableList.add(prefs.getString("nowweathertext",""));
        tableList.add(prefs.getString("fltmp",""));
        tableList.add(prefs.getString("hum",""));
        tableList.add(prefs.getString("vis",""));
        tableList.add(prefs.getString("todayTemp1","")+"~"+prefs.getString("todayTemp2",""));
        tableList.add(prefs.getString("nextDayTemp1","")+"~"+prefs.getString("nextDayTemp2",""));
        for(int i=0;i<tvList.size();i++){
            tvList.get(i).setText(tableList.get(i));
        }
        tableList.clear();
        tableList.add(prefs.getString("nowweathercode",""));
        tableList.add(prefs.getString("day1code_d",""));
        tableList.add(prefs.getString("day2code_d",""));
        for(int i=0;i<imgList.size();i++){
        imgList.get(i).setImageResource(Utility.handlePic(tableList.get(i)));
        }

    }


}

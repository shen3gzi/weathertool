package com.fm.weathertool.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fm.weathertool.R;
import com.fm.weathertool.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class FragmentTwo extends Fragment implements View.OnClickListener{

    private ImageButton cwImgBtn;
    private ImageButton drsgImgBtn;
    private ImageButton fluImgBtn;
    private ImageButton sportImgBtn;
    private ImageButton travImgBtn;
    private ImageButton uvImgBtn;
    private TextView   cwTv;
    private TextView   drsgTv;
    private TextView   fluTv;
    private TextView   sportTv;
    private TextView   travTv;
    private TextView   uvTv;
    private TextView   sugTv;
    private ArrayList<TextView> tvList = new ArrayList<>();
    private ArrayList<ImageView> imgList = new ArrayList<>();

    private ImageView   forecast1Img;
    private ImageView   forecast2Img;
    private ImageView   forecast3Img;
    private ImageView   forecast4Img;
    private ImageView   forecast5Img;
    private ImageView   forecast6Img;
    private ImageView   forecast7Img;

    private TextView   forecast1Tv;
    private TextView   forecast2Tv;
    private TextView   forecast3Tv;
    private TextView   forecast4Tv;
    private TextView   forecast5Tv;
    private TextView   forecast6Tv;
    private TextView   forecast7Tv;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.table2, container, false);
        initView(v);
        initEvent();
        tvList.add(cwTv);
        tvList.add(drsgTv);
        tvList.add(fluTv);
        tvList.add(sportTv);
        tvList.add(travTv);
        tvList.add(uvTv);
        tvList.add(sugTv);

        imgList.add(forecast1Img);
        imgList.add(forecast2Img);
        imgList.add(forecast3Img);
        imgList.add(forecast4Img);
        imgList.add(forecast5Img);
        imgList.add(forecast6Img);
        imgList.add(forecast7Img);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        refreshView(preferences);

        return v;
    }

    public void refreshView(SharedPreferences prefs){
        ArrayList<String> tableList = new ArrayList<>();
        tableList.add(prefs.getString("cwbrf",""));
        tableList.add(prefs.getString("drsgfbrf",""));
        tableList.add(prefs.getString("flubrf",""));
        tableList.add(prefs.getString("sportbrf",""));
        tableList.add(prefs.getString("travbrf",""));
        tableList.add(prefs.getString("uvbrf",""));
        tableList.add(prefs.getString("comfbrf","")+"\n"+prefs.getString("comftxt",""));
        for(int i=0;i<tvList.size();i++){
            tvList.get(i).setText(tableList.get(i));
        }
        tableList.clear();
        tableList.add(prefs.getString("day1code_d",""));
        tableList.add(prefs.getString("day2code_d",""));
        tableList.add(prefs.getString("day3code_d",""));
        tableList.add(prefs.getString("day4code_d",""));
        tableList.add(prefs.getString("day5code_d",""));
        tableList.add(prefs.getString("day6code_d",""));
        tableList.add(prefs.getString("day7code_d",""));

        for(int i=0;i<imgList.size();i++) {
            imgList.get(i).setImageResource(Utility.handlePic(tableList.get(i)));
        }

    }
    private void initView(View v){
        cwImgBtn = (ImageButton) v.findViewById   (R.id.table2_cwBtn);
        drsgImgBtn = (ImageButton) v.findViewById (R.id.table2_drsgBtn);
        fluImgBtn = (ImageButton) v.findViewById  (R.id.table2_fluBtn);
        sportImgBtn = (ImageButton) v.findViewById(R.id.table2_sportBtn);
        travImgBtn = (ImageButton) v.findViewById (R.id.table2_travBtn);
        uvImgBtn = (ImageButton) v.findViewById   (R.id.table2_uvBtn);

        cwTv = (TextView) v.findViewById(R.id.table2_cwText);
        drsgTv = (TextView) v.findViewById(R.id.table2_drsgText);
        fluTv = (TextView) v.findViewById(R.id.table2_fluText);
        sportTv = (TextView) v.findViewById(R.id.table2_sportText);
        travTv = (TextView) v.findViewById(R.id.table2_travText);
        uvTv = (TextView) v.findViewById(R.id.table2_uvText);
        sugTv = (TextView) v.findViewById(R.id.table2_suggestionText);

        forecast1Img = (ImageView) v.findViewById(R.id.table2_forecast1Img);
        forecast2Img = (ImageView) v.findViewById(R.id.table2_forecast2Img);
        forecast3Img = (ImageView) v.findViewById(R.id.table2_forecast3Img);
        forecast4Img = (ImageView) v.findViewById(R.id.table2_forecast4Img);
        forecast5Img = (ImageView) v.findViewById(R.id.table2_forecast5Img);
        forecast6Img = (ImageView) v.findViewById(R.id.table2_forecast6Img);
        forecast7Img = (ImageView) v.findViewById(R.id.table2_forecast7Img);

        forecast1Tv = (TextView) v.findViewById(R.id.table2_forecast1Text);
        forecast2Tv = (TextView) v.findViewById(R.id.table2_forecast2Text);
        forecast3Tv = (TextView) v.findViewById(R.id.table2_forecast3Text);
        forecast4Tv = (TextView) v.findViewById(R.id.table2_forecast4Text);
        forecast5Tv = (TextView) v.findViewById(R.id.table2_forecast5Text);
        forecast6Tv = (TextView) v.findViewById(R.id.table2_forecast6Text);
        forecast7Tv = (TextView) v.findViewById(R.id.table2_forecast7Text);
        //之后七天的日期显示
        SimpleDateFormat sFormat = new SimpleDateFormat("M-d", Locale.CHINA);
        Date date =  new Date();
        Calendar calendar   =   new GregorianCalendar();
        calendar.setTime(date);
        forecast1Tv.setText(sFormat.format(date));
        calendar.add(calendar.DATE,1);  //把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime();        //这个时间就是日期往后推一天的结果
        forecast2Tv.setText(sFormat.format(date));
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        forecast3Tv.setText(sFormat.format(date));
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        forecast4Tv.setText(sFormat.format(date));
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        forecast5Tv.setText(sFormat.format(date));
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        forecast6Tv.setText(sFormat.format(date));
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        forecast7Tv.setText(sFormat.format(date));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        refreshView(preferences);

    }

    private void initEvent(){
          cwImgBtn   .setOnClickListener(this);
          drsgImgBtn .setOnClickListener(this);
          fluImgBtn  .setOnClickListener(this);
          sportImgBtn.setOnClickListener(this);
          travImgBtn .setOnClickListener(this);
          uvImgBtn   .setOnClickListener(this);
    }

    public void onClick(View v) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switch (v.getId()){
            case  R.id.table2_cwBtn   :
                sugTv.setText("  "+preferences.getString("cwtxt",""));
                break;
            case  R.id.table2_drsgBtn :
                sugTv.setText("  "+preferences.getString("drsgtxt",""));
                break;
            case  R.id.table2_fluBtn  :
                sugTv.setText("  "+preferences.getString("flutxt",""));
                break;
            case  R.id.table2_sportBtn:
                sugTv.setText("  "+preferences.getString("sporttxt",""));
                break;
            case  R.id.table2_travBtn :
                sugTv.setText("  "+preferences.getString("travtxt",""));
                break;
            case  R.id.table2_uvBtn   :
                sugTv.setText("  "+preferences.getString("uvtxt",""));
                break;
        }

    }
}

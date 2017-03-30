package com.fm.weathertool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fm.weathertool.R;
import com.fm.weathertool.util.HttpCallbackListener;
import com.fm.weathertool.util.HttpUtil;
import com.fm.weathertool.util.Utility;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    public static final String Key = "e6e6d3429669495980a1142ffe5379d1";
    private TextView topCityTv;
    private ImageButton topAllCityBtu;
    private ImageButton topRefreshBtu;
    private ViewPager mViewPager;
    private ImageButton bottom1;
    private ImageButton bottom2;
    private ImageButton bottom3;
    private ArrayList<Fragment> mFragmentList;
    private FragmentOne fragmentOne ;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    public LocationClient mLocationClient = null; //初始化LocationClient类
    public MyLocationListener myListener = new MyLocationListener();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        topCityTv = (TextView) findViewById(R.id.cityNameText);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener); //注册监听函数
        setLocationOption();
        mLocationClient.start();

         fragmentOne = new FragmentOne();
         fragmentTwo = new FragmentTwo();
         fragmentThree = new FragmentThree();
        initView();
        initEvent();


        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            //设置加载动画
            refreshAnim();
            // 有县级代号时就去查询天气
            queryWeatherCode(countyCode);
        } else {
            // 没有县级代号时就直接显示本地天气
            showWeather();
        }




    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        topCityTv = (TextView) findViewById(R.id.cityNameText);
        topAllCityBtu = (ImageButton) findViewById(R.id.allCityBtn);
        topRefreshBtu = (ImageButton) findViewById(R.id.refreshBtn);
        bottom1 = (ImageButton) findViewById(R.id.bottomImgOne);
        bottom2 = (ImageButton) findViewById(R.id.bottomImgTwo);
        bottom3 = (ImageButton) findViewById(R.id.bottomImgThree);
        bottom1.setImageResource(R.mipmap.bottom_on1);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(fragmentOne);
        mFragmentList.add(fragmentTwo);
        mFragmentList.add(fragmentThree);

        MFragmentPagerAdapter mFragmentPagerAdapter =
                new MFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottom1.setImageResource(R.mipmap.bottom_on1);
                        bottom2.setImageResource(R.mipmap.bottom2);
                        bottom3.setImageResource(R.mipmap.bottom3);
                        break;
                    case 1:
                        bottom1.setImageResource(R.mipmap.bottom1);
                        bottom2.setImageResource(R.mipmap.bottom_on2);
                        bottom3.setImageResource(R.mipmap.bottom3);
                        break;
                    case 2:
                        bottom1.setImageResource(R.mipmap.bottom1);
                        bottom2.setImageResource(R.mipmap.bottom2);
                        bottom3.setImageResource(R.mipmap.bottom_on3);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                

            }
        });


    }

    private void initEvent(){
        bottom1.setOnClickListener(this);
        bottom2.setOnClickListener(this);
        bottom3.setOnClickListener(this);
        topAllCityBtu.setOnClickListener(this);
        topRefreshBtu.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottomImgOne:
                mViewPager.setCurrentItem(0);
                bottom1.setImageResource(R.mipmap.bottom_on1);
                bottom2.setImageResource(R.mipmap.bottom2);
                bottom3.setImageResource(R.mipmap.bottom3);
                break;
           case R.id.bottomImgTwo:
                mViewPager.setCurrentItem(1);
               bottom1.setImageResource(R.mipmap.bottom1);
               bottom2.setImageResource(R.mipmap.bottom_on2);
               bottom3.setImageResource(R.mipmap.bottom3);
               break;
           case R.id.bottomImgThree:
                mViewPager.setCurrentItem(2);
               bottom1.setImageResource(R.mipmap.bottom1);
               bottom2.setImageResource(R.mipmap.bottom2);
               bottom3.setImageResource(R.mipmap.bottom_on3);
               break;
            case R.id.allCityBtn:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_MainActivity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refreshBtn:
                //设置加载动画
                Animation circle_anim = AnimationUtils.loadAnimation(this,R.anim.anim_round_rotate);
                LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
                circle_anim.setInterpolator(interpolator);

                if (circle_anim!=null){
                    topRefreshBtu.startAnimation(circle_anim);
                }
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = pref.getString("weathercode","");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherCode(weatherCode);
                    Toast.makeText(this,"刷新成功",Toast.LENGTH_SHORT).show();
                }
                break;
           default:
               break;
        }
    }

    /**
     * 查询县级代号所对应的天气代号。
     */
    private void queryWeatherCode(String countyCode) {
        String address = "https://api.heweather.com/v5/weather?city="+countyCode+"&key="+Key;
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
                Utility.handleWeatherResponse(MainActivity.this,
                        response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        topCityTv.setText("同步失败");
                        topCityTv.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }
    /*
        将数据信息返回至界面并显示出来
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        topCityTv.setText(prefs.getString("cityname", ""));
        topRefreshBtu.clearAnimation();
        fragmentOne.refreshView(prefs);
        fragmentTwo.refreshView(prefs);
        topAllCityBtu.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        topCityTv.setVisibility(View.VISIBLE);
    }
    /*
        刷新动画
     */
    private void refreshAnim(){
        Animation circle_anim = AnimationUtils.loadAnimation(this,R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);

        if (circle_anim!=null){
            topRefreshBtu.startAnimation(circle_anim);
        }

        topAllCityBtu.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.INVISIBLE);
        topCityTv.setVisibility(View.INVISIBLE);
    }






    //自定义定位监听器

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //将获取的City赋值给txt
            /**
             *1.国家:location.getCountry()
             * 2.城市:location.getCity()
             * 3.区域(例：天河区)：location.getDistrict()
             * 4.地点(例：风信路)：location.getStreet()
             * 5.详细地址：location.getAddrStr()
             */
            topCityTv.setText(location.getAddrStr());
            Toast.makeText(MainActivity.this,"网络定位成功"+
                    location.getAddrStr(),Toast.LENGTH_LONG).show();
        }


        public void onReceivePoi(BDLocation arg0) {
        }
    }

    //执行onDestroy()方法，停止定位
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    //设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);//打开gps
//        option.setCoorType("bd09ll");
//        option.setAddrType("all");
//        option.setIsNeedAddress(true);
//        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
//        option.setPriority(LocationClientOption.GpsFirst); // 设置网络优先
//        option.disableCache(true);//禁止启用缓存定位
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

}

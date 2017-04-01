package com.fm.weathertool.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
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

public class MainActivity extends FragmentActivity implements View.OnClickListener,FragmentThree.Click{
    //天气API开发者Key
    public static final String Key = "e6e6d3429669495980a1142ffe5379d1";
    //标识是否退出
    private static boolean isExit = false;
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
    private LocationClient mLocationClient = null; //初始化LocationClient类
    private MyLocationListener myListener = new MyLocationListener();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
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
        } else if (prfs.getBoolean("loc_selected",false)) {
            //没有县级代号有定位时
            location();

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 没有县级代号没有定位时就直接显示本地天气
                    showWeather();
                }
            });
        }
    }


    //初始化控件
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

            //实现底部按钮点击效果
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

    //初始化点击事件
    private void initEvent(){
        bottom1.setOnClickListener(this);
        bottom2.setOnClickListener(this);
        bottom3.setOnClickListener(this);
        topAllCityBtu.setOnClickListener(this);
        topRefreshBtu.setOnClickListener(this);
    }
    
    //点击事件处理
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
                refreshAnim();
                SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
                if (prfs.getBoolean("loc_selected",false)){
                    location();
                }
                else
                    {
                String weatherCode = prfs.getString("weathercode","");
                        if (!TextUtils.isEmpty(weatherCode)) {
                            queryWeatherCode(weatherCode);
                            Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                break;
           default:
               break;
        }
    }


    //查询县级代号所对应的天气代号。
    private void queryWeatherCode(String countyCode) {
        String address = "https://api.heweather.com/v5/weather?city="+countyCode+"&key="+Key;
        queryFromServer(address);
    }

    // 查询城市名称查询所对应的天气代号。
    private void queryWeatherCode_cityName(String cityName) {
        String address = "https://api.heweather.com/v5/weather?city="+cityName+"&key="+Key;
        queryFromServer(address);
    }

    //根据传入的地址去向服务器查询天气信息
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
    
    //将数据信息返回至界面并显示出来
    private void showWeather() {
        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
        topCityTv.setText(prfs.getString("cityname", ""));
        topRefreshBtu.clearAnimation();
        fragmentOne.refreshView(prfs);
        fragmentTwo.refreshView(prfs);
        topAllCityBtu.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        topCityTv.setVisibility(View.VISIBLE);
    }
    
    //刷新动画
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


    //实现定位功能
    public void location(){

        mLocationClient = new LocationClient(getApplicationContext(), setLocationOption());
        mLocationClient.registerLocationListener(myListener); //注册监听函数
        mLocationClient.start();
    }

    //自定义定位监听器
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Toast.makeText(MainActivity.this,location.getCity()+"定位成功"
                    ,Toast.LENGTH_LONG).show();
            queryWeatherCode("CN101280101");
        }
    }

    //设置定位相关参数
    private LocationClientOption setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setIsNeedAddress(true);
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
        option.setPriority(LocationClientOption.GpsFirst); // 设置网络优先
        option.disableCache(true);//禁止启用缓存定位
        return option;
    }




    //实现设置界面接口通知方法
    public void notifclick(boolean flag) {
        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notifyID = 1;
        if(flag) {
            // 设置通知的ID，以实现更新
            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(prfs.getString("cityname", ""))
                    .setContentText(prfs.getString("nowweathertext", "") + " 当前温度:" + prfs.getString("nowtmp", ""))
                    .setSmallIcon(R.drawable.logo)
                    .setOngoing(true);
            mNotifyMgr.notify(notifyID, mNotifyBuilder.build());
        }
        else {
            mNotifyMgr.cancel(notifyID);
        }

    }
    //实现设置界面接口定位方法
    public void locationclic(boolean flag) {
        if (flag)
            location();
    }

    //实现快速点击两下返回键退出App，否则只提示信息
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }














    //执行onDestroy()方法，停止定位
    public void onDestroy() {
        if (mLocationClient!=null){
        mLocationClient.stop();
        }
        super.onDestroy();
    }


}

package com.fm.weathertool.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.fm.weathertool.activity.WeatherActivity;
import com.fm.weathertool.receiver.AutoUpdateReceiver;
import com.fm.weathertool.util.HttpCallbackListener;
import com.fm.weathertool.util.HttpUtil;
import com.fm.weathertool.util.Utility;

/**
 * Created by FM on 2016/10/25.
 */
public class AutoUpdateService extends Service{

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updataWeather();
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int upHour = 6*60*60*1000;//6小时毫秒数
        long triggerTime = SystemClock.elapsedRealtime()+upHour;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 更新天气信息
     */
    private void updataWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = preferences.getString("weathercode","");
        String address = "https://api.heweather.com/x3/weather?cityid="+countyCode+"&key="+ WeatherActivity.Key;
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }


            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}

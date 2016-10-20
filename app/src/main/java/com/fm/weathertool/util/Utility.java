package com.fm.weathertool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.fm.weathertool.model.City;
import com.fm.weathertool.model.County;
import com.fm.weathertool.model.Province;
import com.fm.weathertool.model.WeatherDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by FM.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级信息
     */
    public synchronized static boolean handleProvincesResponse(WeatherDB db,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvince = response.split(",");
            if(allProvince!=null&&allProvince.length>0){
                for(String p:allProvince){
                    String [] array = p.split(":");
                    Province province = new Province();
                    province.setProvinceCode(array[0].replace("\"","").replace("{",""));
                    province.setProvinceName(array[1].replace("\"","").replace("}",""));
                    //解析回传数据存储Province表
                    db.saveProvince(province);

                }
                return true;
            }
        }
        return false;
    }

    /**
     *解析和处理服务器返回的市级信息
     */
    public synchronized static boolean handleCitiesResponse(WeatherDB db,String response,int provinceId,String provinceCode){
        if (!TextUtils.isEmpty(response)){
            String [] allCity = response.split(",");
            if (allCity!=null&&allCity.length>0){
                for (String c:allCity){
                    String[] array = c.split(":");
                    City city = new City();
                    city.setCityCode(provinceCode+array[0].replace("\"","").replace("{",""));
                    city.setCityName(array[1].replace("\"","").replace("}",""));
                    city.setProvinceId(provinceId);
                    //解析回传数据存储City表
                    db.saveCity(city);
                }
            }
            return true;
        }
        return false;
    }    /**
     *解析和处理服务器返回的县级信息
     */
    public synchronized static boolean handleCountiesResponse(WeatherDB db,String response,int cityId,String cityCode){
        if (!TextUtils.isEmpty(response)){
            String [] allCounty = response.split(",");
            if (allCounty!=null&&allCounty.length>0){
                for (String c:allCounty){
                    String[] array = c.split(":");
                    County county = new County();
                    if("1010100".equals(cityCode)||"1010200".equals(cityCode)||"1010300".equals(cityCode)||"1010400".equals(cityCode)){
                        StringBuffer sb = new StringBuffer(cityCode);
                        sb.insert(5,array[0].replace("\"","").replace("{",""));
                        county.setCountyCode("CN"+sb.toString());
                    }
                    else{
                        county.setCountyCode("CN"+cityCode+array[0].replace("\"", "").replace("{", ""));
                    }

                    county.setCountyName(array[1].replace("\"", "").replace("}", ""));
                    county.setCityId(cityId);
                    //解析回传数据存储County表
                    db.saveCounty(county);
                }
            }
            return true;
        }
        return false;
    }

    /**
     *  解析服务器返回的JSON 数据，并将解析出的数据存储到本地。
     */
    public static void handleWeatherResponse(Context context, String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray HeWeatherData = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject Data = HeWeatherData.getJSONObject(0);
            JSONObject basic = Data.getJSONObject("basic");
            JSONObject update = basic.getJSONObject("update");
            JSONObject now = Data.getJSONObject("now");
            JSONObject cond = now.getJSONObject("cond");
            JSONArray dailyForecast = Data.getJSONArray("daily_forecast");
            JSONObject today = dailyForecast.getJSONObject(0);
            JSONObject todayTmp = today.getJSONObject("tmp");


            String cityName = basic.getString("city");
            String weatherCode = basic.getString("id");
            String temp1 = todayTmp.getString("min");
            String temp2 = todayTmp.getString("max");
            String weatherDesp = cond.getString("txt");
            String publishTime = update.getString("loc");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
                    weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *  将服务器返回的所有天气信息存储到SharedPreferences 文件中。
     */

    public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1+"℃");
        editor.putString("temp2", temp2+"℃");
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }



}

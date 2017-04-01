package com.fm.weathertool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.fm.weathertool.R;
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
            JSONArray HeWeatherData = jsonObject.getJSONArray("HeWeather5");
            JSONObject Data = HeWeatherData.getJSONObject(0);
            JSONObject basic = Data.getJSONObject("basic");
            JSONObject update = basic.getJSONObject("update");
            JSONObject now = Data.getJSONObject("now");
            JSONObject cond = now.getJSONObject("cond");
            JSONArray dailyForecast = Data.getJSONArray("daily_forecast");
            JSONObject day1 = dailyForecast.getJSONObject(0);
            JSONObject todayTmp = day1.getJSONObject("tmp");
            JSONObject day2 = dailyForecast.getJSONObject(1);
            JSONObject nextDayTmp = day2.getJSONObject("tmp");
            JSONObject day3 = dailyForecast.getJSONObject(2);
            JSONObject day4 = dailyForecast.getJSONObject(3);
            JSONObject day5 = dailyForecast.getJSONObject(4);
            JSONObject day6 = dailyForecast.getJSONObject(5);
            JSONObject day7 = dailyForecast.getJSONObject(6);

            JSONObject day1cond = day1.getJSONObject("cond");
            JSONObject day2cond = day2.getJSONObject("cond");
            JSONObject day3cond = day3.getJSONObject("cond");
            JSONObject day4cond = day4.getJSONObject("cond");
            JSONObject day5cond = day5.getJSONObject("cond");
            JSONObject day6cond = day6.getJSONObject("cond");
            JSONObject day7cond = day7.getJSONObject("cond");

            JSONObject sug = Data.getJSONObject("suggestion");
            JSONObject comf = sug.getJSONObject("comf");
            JSONObject cw = sug.getJSONObject("cw");
            JSONObject drsg = sug.getJSONObject("drsg");
            JSONObject flu = sug.getJSONObject("flu");
            JSONObject sport = sug.getJSONObject("sport");
            JSONObject trav = sug.getJSONObject("trav");
            JSONObject uv = sug.getJSONObject("uv");


            String cityName = basic.getString("city");
            String weatherCode = basic.getString("id");

            //第一个fragment界面控件显示相应数据
            String publishTime = update.getString("loc");
            String nowTmp = now.getString("tmp");
            String flTmp = now.getString("fl");
            String hum = now.getString("hum");
            String vis = now.getString("vis");
            String nowWeatherText = cond.getString("txt");
            String nowWeatherCode = cond.getString("code");
            String todayDate = day1.getString("date");
            String todayTemp1 = todayTmp.getString("min");
            String todayTemp2 = todayTmp.getString("max");
            String nextdayDate = day2.getString("date");
            String nextDayTemp1 = nextDayTmp.getString("min");
            String nextDayTemp2 = nextDayTmp.getString("max");
            //第二个Fragment界面数据
            String comfbrf  = comf.getString("brf");
            String cwbrf    = cw.getString("brf");
            String drsgfbrf = drsg.getString("brf");
            String flubrf   = flu.getString("brf");
            String sportbrf = sport.getString("brf");
            String travbrf  = trav.getString("brf");
            String uvbrf    = uv.getString("brf");

            String comftxt  = comf.getString("txt");
            String cwtxt    = cw.getString("txt");
            String drsgftxt = drsg.getString("txt");
            String flutxt   = flu.getString("txt");
            String sporttxt = sport.getString("txt");
            String travtxt  = trav.getString("txt");
            String uvtxt    = uv.getString("txt");

            String day1code_d = day1cond.getString("code_d");
            String day2code_d = day2cond.getString("code_d");
            String day3code_d = day3cond.getString("code_d");
            String day4code_d = day4cond.getString("code_d");
            String day5code_d = day5cond.getString("code_d");
            String day6code_d = day6cond.getString("code_d");
            String day7code_d = day7cond.getString("code_d");



            String[]array = {cityName, weatherCode, publishTime, nowTmp, flTmp,
                    hum, vis, nowWeatherText, nowWeatherCode, todayTemp1, todayTemp2,
                    nextDayTemp1, nextDayTemp2, todayDate, nextdayDate, comfbrf
                    , cwbrf, drsgfbrf, flubrf, sportbrf, travbrf, uvbrf, comftxt,
                    cwtxt, drsgftxt, flutxt, sporttxt, travtxt, uvtxt,
                    day1code_d,day2code_d,day3code_d,day4code_d,day5code_d,day6code_d,day7code_d};

            saveWeatherInfo(context,array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *  将服务器返回的所有天气信息存储到SharedPreferences 文件中。
     */

    public static void saveWeatherInfo(Context context,String[] strings) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("cityname", strings[0]);
        editor.putString("weathercode", strings[1]);
        editor.putString("publishtime", strings[2]);
        editor.putString("nowtmp",strings[3]+"℃");
        editor.putString("fltmp",strings[4]+"℃");
        editor.putString("hum",strings[5]+"％");
        editor.putString("vis",strings[6]+"km");
        editor.putString("nowweathertext",strings[7]);
        editor.putString("nowweathercode",strings[8]);
        editor.putString("todayTemp1",strings[9]+"℃");
        editor.putString("todayTemp2",strings[10]+"℃");
        editor.putString("nextDayTemp1", strings[11]+"℃");
        editor.putString("nextDayTemp2", strings[12]+"℃");
        editor.putString("todaydate", strings[13]);
        editor.putString("nextdaydate", strings[14]);
        editor.putString("currentdate", sdf.format(new Date()));
        String[] array = {"comfbrf","cwbrf","drsgfbrf","flubrf","sportbrf","travbrf","uvbrf",
                "comftxt","cwtxt","drsgtxt","flutxt","sporttxt","travtxt","uvtxt",
                "day1code_d","day2code_d","day3code_d","day4code_d","day5code_d","day6code_d","day7code_d"};
        for(int i=0;i<array.length;i++ ){
            editor.putString(array[i],strings[i+15]);
        }
        editor.commit();
    }

    //根据天气代码匹配天气状态图片
    public static int handlePic(String code){
        int piccode = R.drawable.pic999;
        switch (code){
        case  "100":    piccode = R.drawable.pic100;      break;
        case  "101":    piccode = R.drawable.pic101;      break;
        case  "102":    piccode = R.drawable.pic102;      break;
        case  "103":    piccode = R.drawable.pic103;      break;
        case  "104":    piccode = R.drawable.pic104;      break;
        case  "200":    piccode = R.drawable.pic200;      break;
        case  "201":    piccode = R.drawable.pic201;      break;
        case  "202":    piccode = R.drawable.pic202;      break;
        case  "203":    piccode = R.drawable.pic203;      break;
        case  "204":    piccode = R.drawable.pic204;      break;
        case  "205":    piccode = R.drawable.pic205;      break;
        case  "206":    piccode = R.drawable.pic206;      break;
        case  "207":    piccode = R.drawable.pic207;      break;
        case  "208":    piccode = R.drawable.pic208;      break;
        case  "209":    piccode = R.drawable.pic209;      break;
        case  "210":    piccode = R.drawable.pic210;      break;
        case  "211":    piccode = R.drawable.pic211;      break;
        case  "212":    piccode = R.drawable.pic212;      break;
        case  "213":    piccode = R.drawable.pic213;      break;
        case  "300":    piccode = R.drawable.pic300;      break;
        case  "301":    piccode = R.drawable.pic301;      break;
        case  "302":    piccode = R.drawable.pic302;      break;
        case  "303":    piccode = R.drawable.pic303;      break;
        case  "304":    piccode = R.drawable.pic304;      break;
        case  "305":    piccode = R.drawable.pic305;      break;
        case  "306":    piccode = R.drawable.pic306;      break;
        case  "307":    piccode = R.drawable.pic307;      break;
        case  "308":    piccode = R.drawable.pic308;      break;
        case  "309":    piccode = R.drawable.pic309;      break;
        case  "310":    piccode = R.drawable.pic310;      break;
        case  "311":    piccode = R.drawable.pic311;      break;
        case  "312":    piccode = R.drawable.pic312;      break;
        case  "313":    piccode = R.drawable.pic313;      break;
        case  "400":    piccode = R.drawable.pic400;      break;
        case  "401":    piccode = R.drawable.pic401;      break;
        case  "402":    piccode = R.drawable.pic402;      break;
        case  "403":    piccode = R.drawable.pic403;      break;
        case  "404":    piccode = R.drawable.pic404;      break;
        case  "405":    piccode = R.drawable.pic405;      break;
        case  "406":    piccode = R.drawable.pic406;      break;
        case  "407":    piccode = R.drawable.pic407;      break;
        case  "500":    piccode = R.drawable.pic500;      break;
        case  "501":    piccode = R.drawable.pic501;      break;
        case  "502":    piccode = R.drawable.pic502;      break;
        case  "503":    piccode = R.drawable.pic503;      break;
        case  "504":    piccode = R.drawable.pic504;      break;
        case  "507":    piccode = R.drawable.pic507;      break;
        case  "508":    piccode = R.drawable.pic508;      break;
        case  "900":    piccode = R.drawable.pic900;      break;
        case  "901":    piccode = R.drawable.pic901;      break;
        case  "999":    piccode = R.drawable.pic999;      break;
        default:break;
        }
        return piccode;
    }

    /**
     * 匹配掉错误信息
     */
    public static String replaceCity(String city) {
        city = city.replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
        return city;
    }

}

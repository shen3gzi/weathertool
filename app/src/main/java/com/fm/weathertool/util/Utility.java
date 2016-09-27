package com.fm.weathertool.util;

import android.text.TextUtils;

import com.fm.weathertool.model.City;
import com.fm.weathertool.model.County;
import com.fm.weathertool.model.Province;
import com.fm.weathertool.model.WeatherDB;

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
    public synchronized static boolean handleCitiesResponse(WeatherDB db,String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            String [] allCity = response.split(",");
            if (allCity!=null&&allCity.length>0){
                for (String c:allCity){
                    String[] array = c.split(":");
                    City city = new City();
                    city.setCityCode(array[0].replace("\"","").replace("{",""));
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
    public synchronized static boolean handleCountiesResponse(WeatherDB db,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String [] allCounty = response.split(",");
            if (allCounty!=null&&allCounty.length>0){
                for (String c:allCounty){
                    String[] array = c.split(":");
                    County county = new County();
                    county.setCountyCode(array[0].replace("\"","").replace("{",""));
                    county.setCountyName(array[1].replace("\"","").replace("}",""));
                    county.setCityId(cityId);
                    //解析回传数据存储County表
                    db.saveCounty(county);
                }
            }
            return true;
        }
        return false;
    }

}

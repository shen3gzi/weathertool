package com.fm.weathertool.util;

/**
 * Created by FM.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}

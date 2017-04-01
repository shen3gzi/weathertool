package com.fm.weathertool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.fm.weathertool.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*
    引导页Activity
 */
public class FirstPage extends AppCompatActivity{

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(FirstPage.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    private ImageView firstPageImg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_firstpage);
        firstPageImg = (ImageView) findViewById(R.id.firstpageimg);

        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.CHINA);
        int time = Integer.parseInt(sdf.format(new Date()));//将小时数转化为整型

        //若时间在6点到18点之间，引导页为日间图片，否则为夜间图片
        if(time>5&&time<19){
        firstPageImg.setImageResource(R.drawable.daypage);
        }
        else
        {firstPageImg.setImageResource(R.drawable.nigthpage);}


        new Thread(new Runnable() {
            //延迟引导页两秒跳入主界面
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

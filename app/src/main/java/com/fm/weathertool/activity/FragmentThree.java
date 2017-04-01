package com.fm.weathertool.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.fm.weathertool.R;



public class FragmentThree extends Fragment implements View.OnClickListener{
    private RelativeLayout themeLayout;
    private RelativeLayout infoLayout;
    private Switch locSwitch;
    private Switch notifSwitch;
    private RadioButton autoRBtn;
    private RadioButton manualtoRBtn;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.table3,container,false);
        initView(v);
        initEvent();
        return v;
    }
    private Click click;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       click = (Click) activity;
    }

    private void initView(View view){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        themeLayout = (RelativeLayout) view.findViewById(R.id.table3_themeLayout);
        infoLayout = (RelativeLayout) view.findViewById(R.id.table3_infoLayout);
        locSwitch = (Switch) view.findViewById(R.id.table3_locSwitch);
        notifSwitch = (Switch) view.findViewById(R.id.table3_notifSwitch);
        autoRBtn = (RadioButton) view.findViewById(R.id.table3_autoRadioBtn);
        manualtoRBtn = (RadioButton) view.findViewById(R.id.table3_manualRadioBtn);

        locSwitch.setChecked(preferences.getBoolean("loc_selected",false));
        notifSwitch.setChecked(preferences.getBoolean("notif_selected",false));

    }
    private void initEvent(){
        themeLayout.setOnClickListener(this);
        infoLayout.setOnClickListener(this);
        locSwitch.setOnClickListener(this);
        notifSwitch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        switch (v.getId()){
            case R.id.table3_themeLayout:
                themeLayout.setBackgroundColor(Color.rgb(230,230,230));
                break;
            case R.id.table3_infoLayout:
                infoLayout.setBackgroundColor(Color.rgb(230,230,230));
                break;
            case R.id.table3_locSwitch:
                    editor.putBoolean("loc_selected",locSwitch.isChecked());
                    editor.commit();
                    click.locationclic(locSwitch.isChecked());
                break;
            case R.id.table3_notifSwitch:
                    editor.putBoolean("notif_selected",notifSwitch.isChecked());
                    editor.commit();
                    click.notifclick(notifSwitch.isChecked());
                break;

            default:break;

        }
    }


    public interface Click{
        void notifclick(boolean flag);
        void locationclic(boolean flag);
    }
}

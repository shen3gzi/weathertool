package com.fm.weathertool.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by FM on 2017/3/20.
 */
public class MFragmentPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> mList;
    public MFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public MFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentList) {
        super(fragmentManager);
        this.mList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}

package com.rabbit.application.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Rabbit on 2015/3/16.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"安卓","苹果","微软","数码","智能","探索"};
    List<Fragment> mList;
    public MyFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}

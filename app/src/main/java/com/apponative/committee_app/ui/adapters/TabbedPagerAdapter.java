package com.apponative.committee_app.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apponative.committee_app.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Waqas on 4/26/2017.
 */

public class TabbedPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();

    public TabbedPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragments(List<BaseFragment> mFragmentList, List<String> mFragmentTitleList) {
        this.mFragmentList = mFragmentList;
        this.mFragmentTitleList = mFragmentTitleList;
    }

    public Fragment getFragmentItem(int index){
        return mFragmentList.get(index);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
package com.apps.bandung.bandungcoin;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by Rifqy on 20/04/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    //Membuat variable int mNumOfTabs
    private int mNumOfTabs;

    //Construction
    public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    //Posisi Fragment
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NegeriFragment();
            case 1:
                return new SwastaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

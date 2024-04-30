package com.intuition.ivepos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    String floorname;
    String table_iddd;

    DynamicFragmentAdapter(FragmentManager fm, int NumOfTabs, String floorname, String table_iddd) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.floorname = floorname;
        this.table_iddd = table_iddd;
    }

    // get the current item with position number
    @Override
    public Fragment getItem(int position) {
        System.out.println("DynamicFragmentAdapter1");
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putString("floorname", floorname);
        b.putString("table_iddd", table_iddd);
        Fragment frag = DynamicFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }

    // get total number of tabs
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

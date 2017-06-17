package com.example.tonto.freemusic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tonto.freemusic.fragments.DownloadFragment;
import com.example.tonto.freemusic.fragments.FavoriteFragment;
import com.example.tonto.freemusic.fragments.MusicTypesFragment;

/**
 * Created by tonto on 5/28/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int nTabs;

    public PagerAdapter(FragmentManager fm, int nTabs) {
        super(fm);
        this.nTabs = nTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MusicTypesFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new DownloadFragment();
            default:
                return new MusicTypesFragment();
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}

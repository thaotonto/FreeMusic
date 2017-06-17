package com.example.tonto.freemusic.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.tonto.freemusic.R;

/**
 * Created by tonto on 5/30/2017.
 */

public class ScreenManager {

    public static void openFragment(FragmentManager fragmentManager, Fragment fragment, int layoutID, boolean addToBackStack, boolean haveAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        if (haveAnimation) {
            transaction.setCustomAnimations(R.anim.enter_from_bot, 0, 0, R.anim.exit_to_bot);
        }

        transaction.replace(layoutID, fragment);
        transaction.commit();
    }

    public static void backFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }
}

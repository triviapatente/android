package com.ted_developers.triviapatente.app.views.access;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ted_developers.triviapatente.app.views.access.Login.LoginFragment;
import com.ted_developers.triviapatente.app.views.access.Register.RegisterFragment;

/**
 * Created by Antonio on 22/10/16.
 */
public class FirstAccessAdapter extends FragmentPagerAdapter {

    Fragment[] elements = {
            RegisterFragment.newInstance(),
            // WelcomeFragment.newInstance(),
            LoginFragment.newInstance()
    };

    public FirstAccessAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return elements[position];
    }

    @Override
    public int getCount() {
        return elements.length;
    }
}

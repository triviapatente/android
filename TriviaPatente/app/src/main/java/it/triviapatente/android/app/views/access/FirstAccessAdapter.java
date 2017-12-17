package it.triviapatente.android.app.views.access;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import it.triviapatente.android.app.views.access.login.LoginFragment;
import it.triviapatente.android.app.views.access.register.RegisterFragment;

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

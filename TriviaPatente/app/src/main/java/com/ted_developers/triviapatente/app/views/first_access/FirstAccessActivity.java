package com.ted_developers.triviapatente.app.views.first_access;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;

public class FirstAccessActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FirstAccessAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new FirstAccessAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }
}

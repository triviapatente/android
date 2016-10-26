package com.ted_developers.triviapatente.app.views.first_access;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.mViews.Input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.mViews.LoadingButton.ManageLoading;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstAccessActivity extends AppCompatActivity {

    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.pagerIndicator) CirclePageIndicator mIndicator;
    // error messages
    private FirstAccessAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // View pager
        mAdapter = new FirstAccessAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // provide auto hide of soft keyboard
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
        // ViewPager Indicator
        mIndicator.setViewPager(mViewPager);
        // Start from welcome page
        mViewPager.setCurrentItem(1);
    }

    // hide keyboard
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

package com.ted_developers.triviapatente.app.views.first_access;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstAccessActivity extends AppCompatActivity {

    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.pagerIndicator) CirclePageIndicator mIndicator;
    // error messages
    @BindString(R.string.field_required) public String field_required_error;
    @BindString(R.string.blank_not_allowed) public String blank_not_allowed_error;
    @BindString(R.string.not_matching_pwd) public String not_matching_pwd_error;
    @BindString(R.string.not_valid_email) public String not_valid_email_error;
    // shared preferences
    @BindString(R.string.shared_user) String shared_user;
    @BindString(R.string.shared_token_key) String shared_token_key;
    @BindString(R.string.shared_surname) String shared_surname;
    @BindString(R.string.shared_name) String shared_name;
    @BindString(R.string.shared_username) String shared_username;
    @BindString(R.string.shared_email) String shared_email;

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

    // check input methods

    // is or not a valid email
    public boolean isValidEmail(LabeledInput input) {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(input.toString()).matches()) {
            input.showLabel(not_valid_email_error);
            return false;
        } else {
            input.hideLabel();
            return true;
        }
    }

    // is empty or not
    public boolean checkNotEmptyField(LabeledInput input) {
        if(input.toString().replace(" ", "").equals("")) {
            input.showLabel(field_required_error);
            return false;
        } else {
            input.hideLabel();
            return true;
        }
    }

    // is without blank spaces
    public boolean checkWithoutBlankSpacesField(LabeledInput input) {
        if(input.toString().contains(" ")) {
            input.showLabel(blank_not_allowed_error);
            return false;
        } else {
            input.hideLabel();
            return true;
        }
    }

    // first input equals to the second
    public boolean checkEquals(LabeledInput input1, LabeledInput input2) {
        if(!input1.toString().equals(input2.toString())) {
            input1.showLabel(not_matching_pwd_error);
            return false;
        } else {
            input1.hideLabel();
            return true;
        }
    }

    // go to main page and save data
    public static void openMainPage(FirstAccessActivity a, SuccessUserToken data) {
        // save data
        SharedPreferences sharedPref = a.getSharedPreferences(a.shared_user, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(a.shared_token_key, data.token);
        editor.putString(a.shared_username, data.user.username);
        editor.putString(a.shared_name, data.user.name);
        editor.putString(a.shared_surname, data.user.surname);
        editor.putString(a.shared_email, data.user.email);
        editor.commit();
        // open main page
        Intent myIntent = new Intent(a, MainPageActivity.class);
        a.startActivity(myIntent);
    }
}

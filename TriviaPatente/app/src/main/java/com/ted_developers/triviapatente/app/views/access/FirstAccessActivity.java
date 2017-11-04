package com.ted_developers.triviapatente.app.views.access;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindString;
import butterknife.BindView;

public class FirstAccessActivity extends TPActivity {

    @BindView(R.id.pager) public ViewPager mViewPager;
    @BindView(R.id.pagerIndicator) CirclePageIndicator mIndicator;
    // error messages
    @BindString(R.string.field_required) public String field_required_error;
    @BindString(R.string.blank_not_allowed) public String blank_not_allowed_error;
    @BindString(R.string.not_matching_pwd) public String not_matching_pwd_error;
    @BindString(R.string.not_valid_email) public String not_valid_email_error;
    // intent
    @BindString(R.string.is_old_session) String isOldSession;

    private FirstAccessAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        // if loaded due to old session, show toast
        if(getIntent().getBooleanExtra(isOldSession, false)) {
            Toast.makeText(this, getResources().getString(R.string.old_session),
                    Toast.LENGTH_LONG).show();
        }
        // View pager
        mAdapter = new FirstAccessAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // provide auto hide of soft keyboard
        /*mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });*/
        // ViewPager Indicator
        mIndicator.setViewPager(mViewPager);
        // Start from register page
        mViewPager.setCurrentItem(0);
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
            input.showLabel(not_valid_email_error, Color.WHITE);
            return false;
        } else {
            input.hideLabel();
            return true;
        }
    }

    // is empty or not
    public boolean checkNotEmptyField(LabeledInput input) {
        if(input.toString().replace(" ", "").equals("")) {
            input.showLabel(field_required_error, Color.WHITE);
            return false;
        } else {
            input.hideLabel();
            return true;
        }
    }

    // is without blank spaces
    public boolean checkWithoutBlankSpacesField(LabeledInput input) {
        if(input.toString().contains(" ")) {
            input.showLabel(blank_not_allowed_error, Color.WHITE);
            return false;
        } else {
            input.hideLabel();
            return true;
        }
    }

    // first input equals to the second
    public boolean checkEquals(LabeledInput input1, LabeledInput input2) {
        if(!input1.toString().equals(input2.toString())) {
            input1.showLabel(not_matching_pwd_error, Color.WHITE);
            return false;
        } else {
            input1.hideLabel();
            return true;
        }
    }

    // go to main page and save data
    public static void openMainPage(FirstAccessActivity a, SuccessUserToken data) {
        // save data
        SharedTPPreferences.saveToken(data.token);
        SharedTPPreferences.saveUser(data.user);
        // open main page
        Intent myIntent = new Intent(a, MainPageActivity.class);
        a.startActivity(myIntent);
        a.finish();
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        AuthSocketManager.disconnect();
    }
}

package com.ted_developers.triviapatente.app.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // check if already logged in
        String token = SharedTPPreferences.getToken();
        Intent myIntent;
        if(!token.equals("")) {
            // open main page
             myIntent = new Intent(this, MainPageActivity.class);
        } else {
            // open first access page
            myIntent = new Intent(this, FirstAccessActivity.class);
        }
        startActivity(myIntent);
        finish();
    }
}

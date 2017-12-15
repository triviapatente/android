package it.triviapatente.android.app.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.views.access.FirstAccessActivity;
import it.triviapatente.android.app.views.main_page.MainPageActivity;

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

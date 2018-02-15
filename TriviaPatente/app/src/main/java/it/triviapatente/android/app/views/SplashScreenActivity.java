package it.triviapatente.android.app.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.views.access.FirstAccessActivity;
import it.triviapatente.android.app.views.main_page.MainPageActivity;
import it.triviapatente.android.models.auth.User;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // check if already logged in
        String token = SharedTPPreferences.getToken();
        User user = SharedTPPreferences.currentUser();
        Intent myIntent;
        if(!token.equals("") && user != null) {
            // open main page
            myIntent = new Intent(this, MainPageActivity.class);
            if(getIntent() != null && getIntent().getExtras() != null) {
                String userData = getIntent().getStringExtra(getString(R.string.firebase_message_opponent_key));
                String gameData = getIntent().getStringExtra(getString(R.string.firebase_message_game_key));
                if(userData != null && gameData != null) {
                    myIntent.putExtra(getString(R.string.extra_firebase_opponent_param), userData);
                    myIntent.putExtra(getString(R.string.extra_firebase_game_param), gameData);
                }
            }
        } else {
            // open first access page
            myIntent = new Intent(this, FirstAccessActivity.class);
        }
        startActivity(myIntent);
        finish();
    }
}

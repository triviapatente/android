package com.ted_developers.triviapatente.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 31/10/16.
 */
public class SharedTPPreferences {
    // shared preferences
    public static String shared_TP, shared_token_key, shared_user;
    public static android.content.SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;

    public static void init(Context context) {
        // init shared string
        shared_TP = context.getResources().getString(R.string.shared_preferences);
        shared_token_key = context.getResources().getString(R.string.shared_token_key);
        shared_user = context.getString(R.string.shared_user);

        sharedPref = context.getSharedPreferences(shared_TP, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public static void saveUser(User user) {
        String userString = RetrofitManager.gson.toJson(user, User.class);
        editor.putString(shared_user, userString);
        editor.apply();
    }

    public static User currentUser() {
        String userString = sharedPref.getString(shared_user, "");
        return RetrofitManager.gson.fromJson(userString, User.class);
    }

    public static void saveToken(String token) {
        editor.putString(shared_token_key, token);
        editor.apply();
    }

    public static String getToken() {
        return sharedPref.getString(shared_token_key, "");
    }

    public static void deleteAll() {
        editor.clear();
        editor.apply();
    }
}

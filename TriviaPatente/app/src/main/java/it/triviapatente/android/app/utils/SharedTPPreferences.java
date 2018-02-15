package it.triviapatente.android.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.util.UUID;

import it.triviapatente.android.R;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;

/**
 * Created by Antonio on 31/10/16.
 */
public class SharedTPPreferences {
    // shared preferences
    private static String shared_TP, shared_token_key, shared_user_key;
    private static SharedPreferences sharedPref;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static SharedPreferences.Editor startEditTransaction() {
        return sharedPref.edit();
    }
    public synchronized static String deviceId() {
        if (uniqueID == null) {

            uniqueID = sharedPref.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Editor editor = startEditTransaction();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public static void init(Context context) {
        // init shared string
        shared_TP = context.getResources().getString(R.string.shared_preferences);
        shared_token_key = context.getResources().getString(R.string.shared_token_key);
        shared_user_key = context.getString(R.string.shared_user);

        sharedPref = context.getSharedPreferences(shared_TP, Context.MODE_PRIVATE);
    }

    public static void saveUser(User user) {
        String userString = RetrofitManager.gson.toJson(user, User.class);
        Editor editor = startEditTransaction();
        editor.putString(shared_user_key, userString);
        editor.apply();
    }

    public static User currentUser() {
        String userString = sharedPref.getString(shared_user_key, "");
        return RetrofitManager.gson.fromJson(userString, User.class);
    }

    public static void saveToken(String token) {
        Editor editor = startEditTransaction();
        editor.putString(shared_token_key, token);
        editor.apply();
    }

    public static String getToken() {
        return sharedPref.getString(shared_token_key, "");
    }

    public static void dropSession() {
        Editor editor = startEditTransaction();
        editor.remove(shared_token_key);
        editor.remove(shared_user_key);
        editor.apply();
    }
}

package it.triviapatente.android.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.util.Date;
import java.util.UUID;

import it.triviapatente.android.R;
import it.triviapatente.android.firebase.TokenRequest;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;

/**
 * Created by Antonio on 31/10/16.
 */
public class SharedTPPreferences {
    // shared preferences
    private static String shared_TP, shared_token_key, shared_user_key, shared_last_token_request_key, shared_insta_feed_last_show;
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
        shared_last_token_request_key = context.getString(R.string.shared_last_token_request_key);
        shared_insta_feed_last_show = "last_insta_show";

        sharedPref = context.getSharedPreferences(shared_TP, Context.MODE_PRIVATE);
    }
    public static void saveTokenRequest(String deviceId, String token, User user) {
        TokenRequest request = new TokenRequest(deviceId, token, user.id);
        saveTokenRequest(request);
    }
    public static void saveTokenRequest(TokenRequest request) {
        String value = request.serialize();
        Editor editor = startEditTransaction();
        editor.putString(shared_last_token_request_key, value);
        editor.apply();
    }
    public static TokenRequest getLastTokenRequest() {
        String value = sharedPref.getString(shared_last_token_request_key, null);
        if(value == null) return null;
        return TokenRequest.from(value);
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

    public static void saveInstaLastShow(Date date) {
        long timestamp = date.getTime();
        Editor editor = startEditTransaction();
        editor.putLong(shared_insta_feed_last_show, timestamp);
        editor.apply();
    }

    public static Date getInstaLastShow() {
        long ts = sharedPref.getLong(shared_insta_feed_last_show, 0);
        if (ts != 0) {
            return new Date(ts);
        }
        return null;
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

package com.ted_developers.triviapatente.app.views.first_access.Login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.mViews.Input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.mViews.LoadingButton.ManageLoading;
import com.ted_developers.triviapatente.app.utils.mViews.TPCallback.TPCallback;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Antonio on 23/10/16.
 */
public class LoginPresenter {
    public static void login(String user, String password, final ManageLoading loadingManager) {
        loadingManager.startLoading();
        Call<SuccessUserToken> call = RetrofitManager.getHTTPAuthEndpoint().login(user, password);
        call.enqueue(new Callback<SuccessUserToken>() {
            @Override
            public void onResponse(Call<SuccessUserToken> call, Response<SuccessUserToken> response) {
                Log.i("TEST", response.body().user.username);
                Log.i("TEST", response.body().token);
                loadingManager.stopLoading();
            }

            @Override
            public void onFailure(Call<SuccessUserToken> call, Throwable t) {
                Log.i("TEST", "FAILURE");
                loadingManager.stopLoading();
            }
        });
    }
}

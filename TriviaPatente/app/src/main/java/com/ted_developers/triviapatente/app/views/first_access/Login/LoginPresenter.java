package com.ted_developers.triviapatente.app.views.first_access.Login;

import android.app.Activity;
import android.content.Context;
import android.content.pm.LauncherApps;
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
    public static void login(final LoginFragment lf) {
        FirstAccessActivity a = (FirstAccessActivity) lf.getActivity();
        LabeledInput user = lf.usernameField, password = lf.passwordField;
        final ManageLoading loadingManager = (ManageLoading) lf.loginButton;
        // check input
        boolean valid = a.checkWithoutBlankSpacesField(user) && a.checkNotEmptyField(user);
        valid = a.checkNotEmptyField(password) && valid;
        if(valid) {
            // if no error raised
            // start loading
            loadingManager.startLoading();
            Call<SuccessUserToken> call = RetrofitManager.getHTTPAuthEndpoint().login(
                    user.getText().toString(), password.getText().toString());
            call.enqueue(new TPCallback<SuccessUserToken>() {
                @Override
                public void mOnResponse(Call<SuccessUserToken> call, Response<SuccessUserToken> response) {
                    // response received
                    if (response.code() == 200) {
                        // auth success
                        // success: if shown, hide alert and forgot button
                        // if they aren't shown it is managed from fragment
                        lf.hideAlert();
                        lf.hideForgotButton();
                        // TODO save data
                        // TODO open game page
                        Log.i("TEST", response.body().user.username);
                    } else if (response.code() == 401) {
                        // unauthorized
                        // show alert and forgot button
                        lf.showAlert(lf.forgotUsernamePassword);
                        lf.showForgotButton();
                    } else if (response.code() == 501) {
                        // internal server error
                        lf.showAlert(lf.operationFailed);
                        lf.hideForgotButton();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void mOnFailure(Call<SuccessUserToken> call, Throwable t) {
                    // response not received
                    lf.showAlert(lf.operationFailed);
                }

                // then stop loading
                @Override
                public void then() {
                    loadingManager.stopLoading();
                }
            });
        }
    }
}

package com.ted_developers.triviapatente.app.views.first_access.Register;

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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Antonio on 23/10/16.
 */
public class RegisterPresenter {
    // get from parameters to provide scalability
    public static void register(final RegisterFragment rf) {
        FirstAccessActivity a = (FirstAccessActivity) rf.getActivity();
        final LabeledInput username = rf.usernameField, email = rf.emailField,
                password = rf.passwordField, repeatPassword = rf.repeatPasswordField;
        final ManageLoading loadingManager = rf.registerButton;
        // because of java short circuit condition evaluation
        // check username
        boolean valid = a.checkWithoutBlankSpacesField(username) && a.checkNotEmptyField(username);
        // check email
        valid = a.isValidEmail(email) && valid;
        // check passwords
        if(!a.checkNotEmptyField(password)) {
            valid = false;
            // because i don't need to valid repeat password but i still need to hide the label
            repeatPassword.hideLabel();
        } else {
            // if password is a valid field
            valid = a.checkNotEmptyField(repeatPassword) && a.checkEquals(repeatPassword, password) && valid;
        }
        if (valid) {
            // if no error raised
            // get values
            String username_value = username.toString(),
                    email_value = email.toString(),
                    password_value = password.toString();
            // start loading
            loadingManager.startLoading();
            // register request
            Call<SuccessUserToken> call = RetrofitManager.getHTTPAuthEndpoint().register(
                    username_value, email_value, password_value
            );
            call.enqueue(new TPCallback<SuccessUserToken>() {
                @Override
                public void mOnResponse(Call<SuccessUserToken> call, Response<SuccessUserToken> response) {
                    // response received
                    if (response.code() == 200) {
                        // success: if shown, hide alert
                        // if it isn't shown it is managed from fragment
                        rf.hideAlert();
                        // auth success
                        // TODO save data
                        // TODO open game page
                        Log.i("TEST", response.body().user.username);
                    } else if (response.code() == 401) {
                        // unauthorized
                        String message = "";
                        try {
                            message = new JSONObject(response.errorBody().string()).getString("message");
                            if (message.contains("username")) {
                                username.showLabel(rf.already_registered_username);
                            }
                            if (message.contains("email")) {
                                email.showLabel(rf.already_registered_email);
                            }
                        } catch (JSONException | IOException e) {rf.showAlert(rf.operationFailed);}
                    } else if (response.code() == 501) {
                        // internal server error
                        rf.showAlert(rf.operationFailed);
                    }
                }

                @Override
                public void mOnFailure(Call<SuccessUserToken> call, Throwable t) {
                    // response not received
                    rf.showAlert(rf.operationFailed);
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

package com.ted_developers.triviapatente.app.views.access.Register;

import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.loading.ManageLoading;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Antonio on 23/10/16.
 */
public class RegisterPresenter {

    private static String username_key = "username", email_key = "email", password_key = "key", message_key = "message";

    // get from parameters to provide scalability
    public static void register(final RegisterFragment rf, boolean isFirstAttempt) {
        final FirstAccessActivity a = (FirstAccessActivity) rf.getActivity();
        final LabeledInput username = rf.usernameField, email = rf.emailField,
                password = rf.passwordField, repeatPassword = rf.repeatPasswordField;
        final ManageLoading loadingManager = rf.registerButton;
        // after
        // hide alert
        rf.alertMessageView.hideAlert();
        // force check and enable autocheck
        if(isFirstAttempt) {
            // force check
            username.check();
            email.check();
            password.check();
            repeatPassword.check();

            // set autocheck
            username.setAutoCheck(true);
            email.setAutoCheck(true);
            password.setAutoCheck(true);
            repeatPassword.setAutoCheck(true);
        }
        username.setText(username.getText().toString().trim());
        email.setText(email.getText().toString().trim());
        if (username.isValid() && email.isValid() && password.isValid() && repeatPassword.isValid()) {
            // if no error raised
            // get values
            final String username_value = username.toString(), email_value = email.toString(), password_value = password.toString();
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
                        // auth success
                        // do not stop loading if success
                        doThen = false;
                        FirstAccessActivity.openMainPage(a, response.body());
                    } else if (response.code() == 403) {
                        // unauthorized
                        try {
                            String message = new JSONObject(response.errorBody().string()).getString(message_key);
                            if (message.contains(username_key)) {
                                username.showLabel(rf.already_registered_username);
                            }
                            if (message.contains(email_key)) {
                                email.showLabel(rf.already_registered_email);
                            }
                        } catch (JSONException | IOException e) {rf.alertMessageView.showAlert(rf.operationFailed);}
                    } else if (response.code() == 501) {
                        // internal server error
                        rf.alertMessageView.showAlert(rf.operationFailed);
                    }
                }

                @Override
                public void mOnFailure(Call<SuccessUserToken> call, Throwable t) {
                    // response not received
                    rf.alertMessageView.showAlert(rf.operationFailed);
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

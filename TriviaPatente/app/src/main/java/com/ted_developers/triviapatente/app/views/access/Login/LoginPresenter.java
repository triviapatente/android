package com.ted_developers.triviapatente.app.views.access.Login;

import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.loading.ManageLoading;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Antonio on 23/10/16.
 */
public class LoginPresenter {
    public static void login(final LoginFragment lf) {
        final FirstAccessActivity a = (FirstAccessActivity) lf.getActivity();
        final LabeledInput user = lf.usernameField, password = lf.passwordField;
        final ManageLoading loadingManager = (ManageLoading) lf.loginButton;
        // eventually hide alert and forgot button
        lf.alertMessageView.hideAlert();
        lf.hideForgotButton();
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
                        // do not stop loading if success
                        doThen = false;
                        FirstAccessActivity.openMainPage(a, response.body());
                    } else if (response.code() == 400) {
                        // unauthorized
                        // show alert and forgot button
                        lf.alertMessageView.showAlert(lf.forgotUsernamePassword);
                        lf.showForgotButton();
                    } else if (response.code() == 501) {
                        // internal server error
                        lf.alertMessageView.showAlert(lf.operationFailed);
                    }
                }

                @Override
                public void mOnFailure(Call<SuccessUserToken> call, Throwable t) {
                    // response not received
                    lf.alertMessageView.showAlert(lf.operationFailed);
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

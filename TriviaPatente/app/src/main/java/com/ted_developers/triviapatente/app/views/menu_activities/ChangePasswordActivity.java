package com.ted_developers.triviapatente.app.views.menu_activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.LoadingButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ChangePasswordActivity extends TPActivity {

    @BindString(R.string.activity_change_password_title) String title;
    @BindView(R.id.old_password_field) LabeledInput oldPasswordInput;
    @BindView(R.id.new_password_field) LabeledInput newPasswordInput;
    @BindView(R.id.repeat_password_field) LabeledInput repeatPasswordInput;
    @BindString(R.string.activity_change_password_old_password) String oldPassword;
    @BindString(R.string.activity_change_password_new_password) String newPassword;
    @BindString(R.string.activity_change_password_repeat_password) String repeatPassword;
    @BindString(R.string.not_matching_pwd) String not_matching_pwd;
    @BindString(R.string.field_required) String field_required;
    @BindString(R.string.wrong_pwd) String wrong_pwd;
    @BindString(R.string.same_pwd) String same_pwd;
    @BindString(R.string.not_valid_password) public String not_valid_password_error;

    @BindInt(R.integer.min_password_length) public int minPasswordLength;
    @BindView(R.id.confirmButton) LoadingButton confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initLabeledInputs();
    }

    private void initLabeledInputs() {
        oldPasswordInput.setHint(oldPassword);
        newPasswordInput.setHint(newPassword);
        repeatPasswordInput.setHint(repeatPassword);
        oldPasswordInput.setPassword(true);
        newPasswordInput.setPassword(true);
        repeatPasswordInput.setPassword(true);
    }

    @OnClick(R.id.confirmButton)
    public void confirmButtonClick() {
        // hide all labels
        oldPasswordInput.hideLabel();
        newPasswordInput.hideLabel();
        repeatPasswordInput.hideLabel();
        boolean validData = true;
        if("".equals(oldPasswordInput.getText().toString())) {
            oldPasswordInput.showLabel(field_required);
            validData = false;
        }
        if("".equals(newPasswordInput.getText().toString())) {
            newPasswordInput.showLabel(field_required);
            validData = false;
        } else if(newPasswordInput.getText().length() < minPasswordLength) {
            newPasswordInput.showLabel(not_valid_password_error);
            validData = false;
        } else if(oldPasswordInput.getText().toString().equals(newPasswordInput.getText().toString())) {
            newPasswordInput.showLabel(same_pwd);
            validData = false;
        }
        if("".equals(repeatPasswordInput.getText().toString())) {
            repeatPasswordInput.showLabel(field_required);
            validData = false;
        } else if(!repeatPasswordInput.getText().toString().equals(newPasswordInput.getText().toString())) {
            repeatPasswordInput.showLabel(not_matching_pwd);
            validData = false;
        }
        if(validData) updatePassword(oldPasswordInput.getText().toString(), newPasswordInput.getText().toString());
    }

    private void updatePassword(final String oldPassword, final String newPassword) {
        confirmButton.startLoading();
        Call<SuccessUserToken> call = RetrofitManager.getHTTPAuthEndpoint().changePassword(oldPassword, newPassword);
        call.enqueue(new TPCallback<SuccessUserToken>() {
            @Override
            public void mOnResponse(Call<SuccessUserToken> call, Response<SuccessUserToken> response) {
                if(response.code() == 403) {
                    oldPasswordInput.showLabel(wrong_pwd);
                } else if(response.code() == 200 && response.body().success) {
                    SharedTPPreferences.saveToken(response.body().token);
                    finish();
                }
            }
            @Override
            public void mOnFailure(Call<SuccessUserToken> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), ChangePasswordActivity.this.httpConnectionError, Snackbar.LENGTH_SHORT)
                        .setAction(ChangePasswordActivity.this.httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updatePassword(oldPassword, newPassword);
                            }
                        })
                        .show();
            }
            @Override
            public void then() {
                confirmButton.stopLoading();
            }
        });
    }

    @Override
    protected String getToolbarTitle() { return title; }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }
}

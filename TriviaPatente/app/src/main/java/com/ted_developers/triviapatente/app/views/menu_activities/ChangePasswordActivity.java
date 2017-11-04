package com.ted_developers.triviapatente.app.views.menu_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.LoadingButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

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

    private void updatePassword(String oldPassword, String newPassword) {
        confirmButton.startLoading();
        Toast.makeText(this, "Password cambiata da " + oldPassword + " a " + newPassword, Toast.LENGTH_SHORT).show();
        // TODO update password
        confirmButton.stopLoading();
        finish();
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

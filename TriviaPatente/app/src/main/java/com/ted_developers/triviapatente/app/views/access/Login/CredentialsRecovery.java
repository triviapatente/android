package com.ted_developers.triviapatente.app.views.access.Login;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.LoadingButton;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class CredentialsRecovery extends TPActivity {
    @BindView(R.id.forgot_username_password_recovery_button) LoadingButton recoveryButton;

    @BindView(R.id.username_field) LabeledInput usernameField;
    @BindString(R.string.hint_username_or_email) String usernameemailHint;

    @BindView(R.id.forgot_username_password_explanatory) TextView explanatory;
    @BindString(R.string.forgot_username_password_explanatory) String explanatoryStr;
    @BindString(R.string.credentials_recovery) String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials_recovery);

        // provide auto hide of soft keyboard
        activityContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        // translate emoticons
        explanatory.setText(TPUtils.translateEmoticons(explanatoryStr));

        // init username field
        usernameField.setHint(usernameemailHint);
    }

    @Override
    protected String getToolbarTitle(){
        return title;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }
    @Override
    protected int getBackButtonVisibility() { return View.VISIBLE; }

    // hide keyboard
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // recovery password or username button
    @OnClick(R.id.forgot_username_password_recovery_button)
    public void recoveryUsernamePassword(){
        recoveryButton.startLoading();
        Toast.makeText(this, "Miao credici", Toast.LENGTH_SHORT).show();
        // TODO change password request
        recoveryButton.stopLoading();
    }

}

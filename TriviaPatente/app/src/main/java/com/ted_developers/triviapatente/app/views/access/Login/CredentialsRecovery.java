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
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.LoadingButton;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.Success;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CredentialsRecovery extends TPActivity {
    @BindView(R.id.forgot_username_password_recovery_button) LoadingButton recoveryButton;

    @BindView(R.id.username_field) LabeledInput usernameField;
    @BindString(R.string.hint_username_or_email) String usernameemailHint;

    @BindView(R.id.forgot_username_password_explanatory) TextView explanatory;
    @BindString(R.string.forgot_username_password_explanatory) String explanatoryStr;
    @BindString(R.string.credentials_recovery) String title;

    @BindString(R.string.mail_sent) String mail_sent;

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
        Call<Success> call = RetrofitManager.getHTTPAuthEndpoint().requestNewPassword(usernameField.getText().toString());
        call.enqueue(new TPCallback<Success>() {
            @Override
            public void mOnResponse(Call<Success> call, Response<Success> response) {
                if(response.code() == 401) {
                    Toast.makeText(CredentialsRecovery.this, mail_sent, Toast.LENGTH_SHORT).show();
                } else if(response.code() == 200 && response.body().success) {
                    Toast.makeText(CredentialsRecovery.this, mail_sent, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void mOnFailure(Call<Success> call, Throwable t) {}
            @Override
            public void then() {
                recoveryButton.stopLoading();
            }
        });
    }

}

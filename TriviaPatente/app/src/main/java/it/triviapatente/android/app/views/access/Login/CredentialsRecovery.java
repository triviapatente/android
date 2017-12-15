package it.triviapatente.android.app.views.access.Login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.TPCallback;
import it.triviapatente.android.app.utils.custom_classes.input.LabeledInput;
import it.triviapatente.android.app.utils.custom_classes.buttons.LoadingButton;
import it.triviapatente.android.app.utils.custom_classes.input.LabeledInputError;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.responses.Success;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CredentialsRecovery extends TPActivity {
    @BindView(R.id.forgot_username_password_recovery_button)
    LoadingButton recoveryButton;

    @BindView(R.id.username_field)
    LabeledInput usernameField;
    @BindString(R.string.hint_username_or_email) String usernameemailHint;

    @BindView(R.id.forgot_username_password_explanatory) TextView explanatory;
    @BindString(R.string.forgot_username_password_explanatory) String explanatoryStr;
    @BindString(R.string.credentials_recovery) String title;

    @BindString(R.string.mail_sent) String mail_sent;
    @BindString(R.string.activity_credentials_recovery_no_user_matching) String no_user_matching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials_recovery);

        // translate emoticons
        explanatory.setText(TPUtils.translateEmoticons(explanatoryStr));
        mail_sent = TPUtils.translateEmoticons(mail_sent);

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

    // recovery password or username button
    @OnClick(R.id.forgot_username_password_recovery_button)
    public void recoveryUsernamePassword(){
        if(!usernameField.hasAutoCheck()) {
            usernameField.setErrorsToCheck(null, LabeledInputError.EMPTY, LabeledInputError.BLANK);
            usernameField.setAutoCheck(true);
            usernameField.check();
        }
        usernameField.setText(usernameField.getText().toString().trim());
        if(usernameField.isValid()) {
            recoveryButton.startLoading();
            Call<Success> call = RetrofitManager.getHTTPAuthEndpoint().requestNewPassword(usernameField.getText().toString());
            call.enqueue(new TPCallback<Success>() {
                @Override
                public void mOnResponse(Call<Success> call, Response<Success> response) {
                    if(response.code() == 404) {
                        Toast.makeText(CredentialsRecovery.this, no_user_matching, Toast.LENGTH_SHORT).show();
                    } else if(response.code() == 200 && response.body().success) {
                        Toast.makeText(CredentialsRecovery.this, mail_sent, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void mOnFailure(Call<Success> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recoveryUsernamePassword();
                                }
                            })
                            .show();
                }
                @Override
                public void then() {
                    recoveryButton.stopLoading();
                }
            });
        }
    }

}

package it.triviapatente.android.app.views.menu_activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.TPCallback;
import it.triviapatente.android.app.utils.custom_classes.dialogs.TPPolicyAndTermsDialog;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.responses.Success;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ContactsActivity extends TPActivity {
    @BindString(R.string.activity_contacts_title) String title;
    @BindString(R.string.activity_contacts_terms_and_conditions1) String terms_and_conditions1;
    @BindString(R.string.activity_contacts_terms_and_conditions2) String terms_and_conditions2;
    @BindString(R.string.activity_contacts_terms_and_conditions3) String terms_and_conditions3;
    @BindString(R.string.activity_contacts_terms_and_conditions4) String terms_and_conditions4;
    @BindColor(R.color.greenTermsAndConditions) int green;
    @BindView(R.id.terms_and_conditions) TextView termsandconditionsLink;

    @BindView(R.id.messageInput) EditText messageEditText;
    @BindString(R.string.activity_contacts_messageSentString) String messageSentString;
    @BindString(R.string.activity_contacts_messageNotSentString) String messageNotSentString;

    @BindView(R.id.motivations_spinner) Spinner scopeSpinner;
    @BindArray(R.array.scope_array) String[] scopeArray;

    @BindView(R.id.sendButton) ImageButton sendButton;

    @BindView(R.id.loadingView) RelativeLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        messageNotSentString = TPUtils.translateEmoticons(messageNotSentString);

        setTermsAndPolicyText();

        loadingView.setVisibility(View.GONE);
    }
    private Spannable markTermsText(int lower, int upper, Spannable spannable) {
        spannable.setSpan(new ForegroundColorSpan(green),
                lower,
                upper,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void setTermsAndPolicyText() {
        String str = terms_and_conditions1 + terms_and_conditions2 + terms_and_conditions3 + terms_and_conditions4;
        Spannable spannable = new SpannableString(str);
        String lower = terms_and_conditions1;
        String upper = terms_and_conditions1 + terms_and_conditions2;
        spannable = markTermsText(lower.length(), upper.length(), spannable);
        lower = upper + terms_and_conditions3;
        spannable = markTermsText(lower.length(), str.length(), spannable);
        termsandconditionsLink.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @OnClick(R.id.motivations_header)
    public void openScopeSpinner() {
        scopeSpinner.performClick();
    }

    @OnClick(R.id.terms_and_conditions)
    public void termsAndConditionsClick() {
        TPPolicyAndTermsDialog dialog = new TPPolicyAndTermsDialog(this);
        dialog.show();
    }

    @OnClick(R.id.sendButton)
    public void sendButtonClick() {
        if(messageEditText.getText().length() <= 0) {
            Toast.makeText(this, messageNotSentString, Toast.LENGTH_SHORT).show();
        } else {
            sendButton.setEnabled(false);
            loadingView.setVisibility(View.VISIBLE);
            Call<Success> call = RetrofitManager.getHTTPBaseEndpoint().contact(
                    messageEditText.getText().toString(),
                    scopeArray[scopeSpinner.getSelectedItemPosition()]
            );
            call.enqueue(new TPCallback<Success>() {
                @Override
                public void mOnResponse(Call<Success> call, Response<Success> response) {
                    if(response.body().success) {
                        Toast.makeText(ContactsActivity.this, messageSentString, Toast.LENGTH_SHORT).show();
                        messageEditText.setText("");
                    }
                    else Toast.makeText(ContactsActivity.this, messageNotSentString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void mOnFailure(Call<Success> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendButtonClick();
                                }
                            })
                            .show();
                }

                @Override
                public void then() {
                    sendButton.setEnabled(true);
                    loadingView.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected String getToolbarTitle(){ return title; }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }
}

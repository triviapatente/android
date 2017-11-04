package com.ted_developers.triviapatente.app.views.menu_activities;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class ContactsActivity extends TPActivity {
    @BindString(R.string.activity_contacts_title) String title;
    @BindString(R.string.activity_contacts_terms_and_conditions1) String terms_and_conditions1;
    @BindString(R.string.activity_contacts_terms_and_conditions2) String terms_and_conditions2;
    @BindColor(R.color.greenTermsAndConditions) int green;
    @BindView(R.id.terms_and_conditions) TextView termsandconditionsLink;

    @BindView(R.id.messageInput) EditText messageEditText;
    @BindString(R.string.activity_contacts_messageSentString) String messageSentString;
    @BindString(R.string.activity_contacts_messageNotSentString) String messageNotSentString;
    @BindString(R.string.terms_and_conditions_easteregg) String terms_and_conditions_easteregg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        messageNotSentString = TPUtils.translateEmoticons(messageNotSentString);

        String str = terms_and_conditions1 + terms_and_conditions2;
        Spannable spannable = new SpannableString(str);
        spannable.setSpan(
                new ForegroundColorSpan(green),
                terms_and_conditions1.length(),
                str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        termsandconditionsLink.setText(spannable, TextView.BufferType.SPANNABLE);

        terms_and_conditions_easteregg = TPUtils.translateEmoticons(terms_and_conditions_easteregg);
        // hide keyboard not provided... in android the common way is to click the appropriate button.
    }

    @OnClick(R.id.terms_and_conditions)
    public void termsAndConditionsClick() {
        Toast.makeText(this, terms_and_conditions_easteregg, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Sciuar", Toast.LENGTH_SHORT).show();
        // TODO see terms
    }

    @OnClick(R.id.sendButton)
    public void sendButtonClick() {
        if(messageEditText.getText().length() <= 0) {
            Toast.makeText(this, messageNotSentString, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, messageSentString, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Sciuar", Toast.LENGTH_SHORT).show();
            // TODO send message
            this.finish();
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

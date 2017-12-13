package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.models.auth.User;

import org.w3c.dom.Text;

/**
 * Created by Antonio on 04/11/17.
 */

public class TPNewTermsPolicyDialog extends Dialog implements View.OnClickListener {
    public TPNewTermsPolicyDialog(@NonNull Context context, boolean terms, boolean policy, String termsLastUpdate, String policyLastUpdate) {
        super(context);
        this.termsLastUpdate = termsLastUpdate;
        this.policyLastUpdate = policyLastUpdate;
        init(terms, policy);
    }


    private boolean termsAccepted, policyAccepted;
    private String termsLastUpdate, policyLastUpdate;

    private void init(boolean terms, boolean policy) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_update_terms_policy);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.3f);
        // TODO spannable
        findViewById(R.id.positiveButton).setOnClickListener(this);
        findViewById(R.id.positiveButton).setEnabled(true);
        // first show eventually terms
        termsAccepted = !terms;
        policyAccepted = !policy;
        if(terms) setTermsUpdateUI();
        else setPolicyUpdateUI();
    }


    private Spannable markTermsText(int lower, int upper, Spannable spannable) {
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.greenTermsAndConditions)),
                lower,
                upper,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void setClickableMessage(String text, String highlightedText, final String path) {
        String str = text + highlightedText;
        Spannable spannable = new SpannableString(str);
        String lower = text;
        String upper = text + highlightedText;
        spannable = markTermsText(lower.length(), upper.length(), spannable);
        ((TextView) findViewById(R.id.modal_message)).setText(spannable);
        findViewById(R.id.modal_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(path);
            }
        });
    }

    private void redirect(String path) {
        String url = getContext().getString(R.string.baseUrl) + path;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(browserIntent);
    }

    private void setPolicyUpdateUI() {
        setClickableMessage(
                getContext().getString(R.string.modal_new_policy_message),
                getContext().getString(R.string.privacy_policy),
                getContext().getString(R.string.privacy_policy_path)
        );
    }

    private void setTermsUpdateUI() {
        setClickableMessage(
                getContext().getString(R.string.modal_new_terms_message),
                getContext().getString(R.string.terms_and_conditions),
                getContext().getString(R.string.terms_path)
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton: {
                if(!termsAccepted) {
                    termsAccepted = true;
                    acceptTermsUpdate(); // accept terms
                    if(!policyAccepted) {
                        // show policy update
                        setPolicyUpdateUI();
                        break; // do not close modal
                    }
                } else acceptPolicyUpdate(); // terms has been accepted, accept also policy
                dismiss(); // close modal
            } break;
        }
    }

    private void acceptTermsUpdate() {
        User user = SharedTPPreferences.currentUser();
        user.termsAndConditionsLastUpdate = termsLastUpdate;
        SharedTPPreferences.saveUser(user);
    }

    private void acceptPolicyUpdate() {
        User user = SharedTPPreferences.currentUser();
        user.privacyPolicyLastUpdate = policyLastUpdate;
        SharedTPPreferences.saveUser(user);
    }
}

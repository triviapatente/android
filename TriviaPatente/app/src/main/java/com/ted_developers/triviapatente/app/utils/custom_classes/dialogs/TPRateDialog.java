package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.views.menu_activities.ContactsActivity;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 04/11/17.
 */

public class TPRateDialog extends Dialog implements View.OnClickListener {

    public TPRateDialog(@NonNull Context context) {
        super(context);
        init(false);
    }

    public TPRateDialog(@NonNull Context context, boolean automaticPopup) {
        super(context);
        init(automaticPopup);
    }

    Button positiveButtonAmazing, positiveButtonBad;

    private void init(boolean automaticPopup) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_rate);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.3f);
        findViewById(R.id.negativeButton).setOnClickListener(this);
        positiveButtonAmazing = ((Button) findViewById(R.id.positiveButtonAmazing));
        positiveButtonBad = ((Button) findViewById(R.id.positiveButtonBad));
        // amazing
        positiveButtonAmazing.setText(TPUtils.translateEmoticons(getContext().getString(R.string.modal_rate_amazing_experience)));
        positiveButtonAmazing.setOnClickListener(this);
        positiveButtonAmazing.setEnabled(true);
        // bad
        positiveButtonBad.setText(TPUtils.translateEmoticons(getContext().getString(R.string.modal_rate_bad_experience)));
        positiveButtonBad.setOnClickListener(this);
        positiveButtonBad.setEnabled(true);
        if(!automaticPopup) {
            findViewById(R.id.notShowingAgainContainer).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negativeButton: closeAndSave(); break;
            case R.id.positiveButtonAmazing: updateModal(true); break;
            case R.id.positiveButtonBad: updateModal(false); break;
        }
    }

    private void updateModal(boolean amazing) {
        ((TextView) findViewById(R.id.modal_message)).setText(amazing ? R.string.modal_rate_message_amazing : R.string.modal_rate_message_bad);
        findViewById(R.id.separator).setVisibility(View.GONE);
        findViewById(R.id.notShowingAgainContainer).setVisibility(View.GONE);
        if(amazing) {
            positiveButtonBad.setVisibility(View.GONE);
            positiveButtonAmazing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectAndSave(true);
                }
            });
            positiveButtonAmazing.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.modal_confirmed_button));
        }
        else {
            positiveButtonAmazing.setVisibility(View.GONE);
            positiveButtonBad.setText(TPUtils.translateEmoticons(getContext().getString(R.string.modal_rate_confirm_bad)));
            positiveButtonBad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectAndSave(false);
                }
            });
            positiveButtonBad.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.modal_confirmed_button));
            findViewById(R.id.modal_icon).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.image_rocket_low));
        }
    }

    private void redirectAndSave(boolean amazing) {
        ((CheckBox) findViewById(R.id.notShowingAgainCheckbox)).setChecked(true); // not showing again until new update
        closeAndSave();
        if(amazing) redirectToStore();
        else getContext().startActivity(new Intent(getContext(), ContactsActivity.class));
    }

    private void redirectToStore() {
        String url = getContext().getString(R.string.baseUrl) + getContext().getString(R.string.store_path);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(browserIntent);
    }

    private void closeAndSave() {
        User user = SharedTPPreferences.currentUser();
        if(user.showRatePopup && ((CheckBox) findViewById(R.id.notShowingAgainCheckbox)).isChecked()) {
            user.showRatePopup = false;
        }
        SharedTPPreferences.saveUser(user);
        dismiss();
    }
}

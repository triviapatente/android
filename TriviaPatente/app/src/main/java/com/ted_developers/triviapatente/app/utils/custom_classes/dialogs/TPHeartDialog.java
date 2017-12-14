package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 04/11/17.
 */

public class TPHeartDialog extends Dialog implements View.OnClickListener {

    public TPHeartDialog(@NonNull Context context) {
        super(context);
        init(false);
    }

    public TPHeartDialog(@NonNull Context context, boolean automaticPopup) {
        super(context);
        init(automaticPopup);
    }

    private void init(boolean automaticPopup) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_heart_infinite);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.3f);
        ((TextView) findViewById(R.id.modal_message)).setText(TPUtils.translateEmoticons(getContext().getString(R.string.modal_hearts_infinite_text)));
        findViewById(R.id.negativeButton).setOnClickListener(this);
        findViewById(R.id.positiveButton).setOnClickListener(this);
        findViewById(R.id.positiveButton).setEnabled(true);
        if(!automaticPopup) {
            findViewById(R.id.notShowingAgainContainer).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negativeButton: closeAndSave(); break;
            case R.id.positiveButton: v.setEnabled(false); inviteFriends(); closeAndSave(); break;
        }
    }

    private void closeAndSave() {
        User user = SharedTPPreferences.currentUser();
        if(user.showLifePopup && ((CheckBox) findViewById(R.id.notShowingAgainCheckbox)).isChecked()) {
            user.showLifePopup = false;
        }
        SharedTPPreferences.saveUser(user);
        dismiss();
    }

    private void inviteFriends() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.invite_friends_string));
        sendIntent.setType("text/plain");
        getContext().startActivity(sendIntent);
    }
}

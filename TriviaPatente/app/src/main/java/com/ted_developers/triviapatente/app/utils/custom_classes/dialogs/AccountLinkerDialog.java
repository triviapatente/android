package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 04/12/16.
 */
public abstract class AccountLinkerDialog extends Dialog implements android.view.View.OnClickListener {
    ImageButton exitButton;
    Button connectConfirmerButton;
    @LayoutRes int themeResId = R.layout.modal_view_connect_to_facebook;


    public AccountLinkerDialog(Context context) {
        super(context);
    }

    public AccountLinkerDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.themeResId = themeResId;
    }

    protected AccountLinkerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /*@Override
    public void onStart() {
        super.onStart();

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.holo_red_dark);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(themeResId);
        exitButton = (ImageButton) findViewById(R.id.exit_button);
        connectConfirmerButton = (Button) findViewById(R.id.connectionConfirmer);
        exitButton.setOnClickListener(this);
        connectConfirmerButton.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_button: onExit(); return;
            case R.id.connectionConfirmer: onConfirm(); break;
            default: break;
        }
    }

    public abstract void onExit();
    public abstract void onConfirm();
}

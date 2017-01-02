package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ted_developers.triviapatente.R;
import com.viewpagerindicator.TabPageIndicator;

import butterknife.OnTouch;

/**
 * Created by Antonio on 04/12/16.
 */
public abstract class TPDialog extends Dialog implements android.view.View.OnClickListener {
    Button positiveButton, negativeButton;
    @LayoutRes int themeResId = R.layout.modal_view_connect_to_facebook;
    boolean cancelable = true;
    float dimAmount = 0.4f;
    OnCancelListener cancelListener = null;

    public TPDialog(Context context) {
        super(context);
    }

    public TPDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.themeResId = themeResId;
    }

    public TPDialog(Context context, int themeResId, float dimAmount) {
        super(context, themeResId);
        this.themeResId = themeResId;
        this.dimAmount = dimAmount;
    }

    public TPDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.cancelable = cancelable;
        this.cancelListener = cancelListener;
    }

    public TPDialog(Context context, int themeResId, float dimAmount, boolean cancelable, OnCancelListener cancelListener) {
        super(context, themeResId);
        this.cancelable = cancelable;
        this.cancelListener = cancelListener;
        this.themeResId = themeResId;
        this.dimAmount = dimAmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(themeResId);
        negativeButton = (Button) findViewById(R.id.negativeButton);
        positiveButton = (Button) findViewById(R.id.positiveButton);
        negativeButton.setOnClickListener(this);
        positiveButton.setOnClickListener(this);
        setCanceledOnTouchOutside(cancelable);
        setOnCancelListener(cancelListener);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(dimAmount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negativeButton: onNegativeButtonClick(); return;
            case R.id.positiveButton: onPositiveButtonClick(); break;
            default: break;
        }
    }

    public abstract void onNegativeButtonClick();
    public abstract void onPositiveButtonClick();
}

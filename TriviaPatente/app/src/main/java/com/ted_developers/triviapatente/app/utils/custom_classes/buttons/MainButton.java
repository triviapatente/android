package com.ted_developers.triviapatente.app.utils.custom_classes.buttons;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ted_developers.triviapatente.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Antonio on 30/10/16.
 */
public class MainButton extends LinearLayout {
    private TextView mainTextView;
    private TextSwitcher hintTextSwitcher;
    private String[] hintTexts;
    private ImageView image;
    private Activity activity = null;
    private int rotationTime = 5000;
    private Timer rotator = null;

    public MainButton(Context context) {
        super(context);
        init(context);
    }

    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutParams params;
        int horizontalMargin = (int) getResources().getDimension(R.dimen.main_button_margin_horizontal);
        int fieldHeight = (int) getResources().getDimension(R.dimen.main_page_button_heigth);

        // image on the left
        image = new ImageView(context);
        // layout params
        int imageSize = (int) getResources().getDimension(R.dimen.main_page_image_size);
        params = new LayoutParams(imageSize, imageSize);
        params.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        params.gravity = Gravity.CENTER;
        image.setLayoutParams(params);
        this.addView(image);

        // button name on the center
        mainTextView = new TextView(context);
        // button on the center params
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, horizontalMargin, 0);
        params.gravity = Gravity.CENTER;
        mainTextView.setLayoutParams(params);
        // text
        mainTextView.setGravity(Gravity.CENTER);
        mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.TPTextSizeMedium));
        this.addView(mainTextView);

        // hints on the right
        hintTextSwitcher = new TextSwitcher(context);
        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(context, R.anim.slide_up_in);
        Animation out = AnimationUtils.loadAnimation(context,R.anim.slide_up_out);
        // set the animation type of textSwitcher
        hintTextSwitcher.setInAnimation(in);
        hintTextSwitcher.setOutAnimation(out);
        // hints params
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, horizontalMargin, 0);
        params.gravity = Gravity.CENTER;
        hintTextSwitcher.setLayoutParams(params);
        this.addView(hintTextSwitcher);
    }

    private void setHintFactory(final int color) {
        hintTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView myText = new TextView(getContext());
                myText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                myText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.TPTextSizeVerySmall));
                myText.setTextColor(color);
                myText.setSingleLine(true);
                myText.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                return myText;
            }
        });
    }

    public void setButtonText(String buttonText) {
        mainTextView.setText(buttonText);
    }

    public void setButtonText(String buttonText, int color) {
        mainTextView.setTextColor(color);
        setButtonText(buttonText);
    }

    public void setHintText(String[] hintTexts) {
        setHintText(hintTexts, Color.WHITE);
    }

    public void setHintText(String[] hintTexts, int color) {
        if(activity != null && hintTexts != null && hintTexts.length > 0) {
            setHintFactory(color);
            if(hintTexts.length == 1) { hintTextSwitcher.setText(hintTexts[0]); }
            else {
                this.hintTexts = hintTexts;
                hintStartRotation();
            }
        }
    }

    public void setActivity(Activity a) {
        activity = a;
    }

    public void setRotationTime(int rotationTime) {
        this.rotationTime = rotationTime;
    }

    public void setImage(Drawable img) {
        image.setImageDrawable(img);
    }

    private void hintStartRotation() {
        // start hints rotation
        if(rotator != null) {
            rotator.cancel();
        }
        rotator = new Timer();
        rotator.scheduleAtFixedRate(
                new TimerTask() {
                    int counter = 0;
                    public void run() {
                        if(activity != null && hintTexts != null && hintTexts.length > 1) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hintTextSwitcher.setText(hintTexts[counter]);
                                    counter = (counter + 1) % hintTexts.length;
                                }
                            });
                        }
                    }
                }, 0, rotationTime);
    }
}
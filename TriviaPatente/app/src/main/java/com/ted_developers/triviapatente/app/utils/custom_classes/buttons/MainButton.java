package com.ted_developers.triviapatente.app.utils.custom_classes.buttons;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ted_developers.triviapatente.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Antonio on 30/10/16.
 */
public class MainButton extends RelativeLayout {
    private TextView mainTextView;
    private TextSwitcher hintTextSwitcher;
    private String[] hintTexts;
    public String currentText = "";
    private ImageView image;
    private int rotationTime = 5000;
    private Timer rotator = null;
    private @ColorInt int textSwitcherColor;
    private Context context;
    private boolean disabled = false;

    public MainButton(Context context) {
        super(context);
        init(context, null);
    }

    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //init(context);
        inflateLayout(context);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MainButton,
                0, 0);
        try {
            setImage(a.getDrawable(R.styleable.MainButton_main_button_image));
            setButtonText(a.getString(R.styleable.MainButton_main_button_name), a.getColor(R.styleable.MainButton_main_button_text_color, Color.WHITE));
            setRotationTime(a.getInteger(R.styleable.MainButton_main_button_hint_rotation_speed, 5000));
            disabled = a.getBoolean(R.styleable.MainButton_main_button_disabled, false);
        } finally {
            a.recycle();
        }

        if(!disabled) {
            initTextSwitcher();
            findViewById(R.id.comingSoon).setVisibility(View.GONE);
        } else {
            findViewById(R.id.upperLayer).setVisibility(View.VISIBLE);
            setEnabled(false);
        }
    }

    private void initTextSwitcher() {
        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(context, R.anim.slide_up_in);
        Animation out = AnimationUtils.loadAnimation(context,R.anim.slide_up_out);
        // set the animation type of textSwitcher
        hintTextSwitcher.setInAnimation(in);
        hintTextSwitcher.setOutAnimation(out);
    }

    private void inflateLayout(Context context) {
        this.context = context;

        // inflate layout

        LayoutInflater li = LayoutInflater.from(context);
        li.inflate(R.layout.layout_main_button, this);

        // get components
        image = (ImageView) findViewById(R.id.main_button_image);
        mainTextView = (TextView) findViewById(R.id.main_button_text);
        hintTextSwitcher = (TextSwitcher) findViewById(R.id.hintSwitcher);
    }

    private void setHintFactory(final int color) {
        textSwitcherColor = color;
        try {
            hintTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    TextView myText = new TextView(getContext());
                    myText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    myText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.TPTextSizeVerySmall));
                    myText.setTextColor(textSwitcherColor);
                    myText.setSingleLine(true);
                    myText.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    return myText;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if(context != null && hintTexts != null && hintTexts.length > 0) {
            setHintFactory(color);
            if(hintTexts.length == 1) {
                if(!hintTexts[0].equals(currentText)) {
                    hintTextSwitcher.setText(hintTexts[0]);
                    currentText = hintTexts[0];
                }
            }
            else {
                this.hintTexts = hintTexts;
                hintStartRotation();
            }
        }
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
                        if(!disabled && context != null && hintTexts != null && hintTexts.length > 1) {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hintTextSwitcher.setText(hintTexts[counter]);
                                    currentText = hintTexts[counter];
                                    counter = (counter + 1) % hintTexts.length;
                                }
                            });
                        }
                    }
                }, 0, rotationTime);
    }
}

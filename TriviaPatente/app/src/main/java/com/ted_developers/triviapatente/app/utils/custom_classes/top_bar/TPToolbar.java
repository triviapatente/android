package com.ted_developers.triviapatente.app.utils.custom_classes.top_bar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.ViewSwitcher;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 31/10/16.
 */
public class TPToolbar extends RelativeLayout {
    // back button
    protected LinearLayout backButton;
    protected TextView backButtonText;
    // hearts box
    protected LinearLayout heartsBox;
    protected ImageView heartImage;
    protected TextSwitcher lifeCounter;
    protected TextSwitcher heartsTimer;
    // title
    protected TextView toolbarTitle;
    // profile picture
    protected ImageView profilePicture;
    // settings
    protected LinearLayout settings;
    // menu
    private View menu;
    private boolean show_menu;


    public TPToolbar(Context context) {
        super(context);
        init(context);
    }

    public TPToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TPToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TPToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.tp_toolbar, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(CENTER_IN_PARENT);
        v.setLayoutParams(params);
        this.addView(v);
        bindElements();
        initElements(context);
    }

    private void initElements(final Context context) {
        // init text switchers
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        lifeCounter.setInAnimation(in);
        lifeCounter.setOutAnimation(out);
        lifeCounter.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView myText = new TextView(getContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                myText.setLayoutParams(params);
                myText.setGravity(Gravity.CENTER | Gravity.TOP);
                TextViewCompat.setTextAppearance(myText, R.style.TPTextStyleSmall);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });
        heartsTimer.setInAnimation(in);
        heartsTimer.setOutAnimation(out);
        heartsTimer.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView myText = new TextView(getContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                myText.setLayoutParams(params);
                myText.setGravity(Gravity.CENTER_VERTICAL);
                TextViewCompat.setTextAppearance(myText, R.style.TPTextStyleMedium);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });
    }

    // bind all elements through their id
    private void bindElements() {
        // back button text
        backButton = (LinearLayout) findViewById(R.id.backButton);
        backButtonText = (TextView) findViewById(R.id.backButtonText);
        // hearts box
        heartsBox = (LinearLayout) findViewById(R.id.heartsBox);
        heartImage = (ImageView) findViewById(R.id.heartImage);
        lifeCounter = (TextSwitcher) findViewById(R.id.lifeCounter);
        heartsTimer = (TextSwitcher) findViewById(R.id.heartsTimer);
        // title
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        // profile picture
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        // settings
        settings = (LinearLayout) findViewById(R.id.settings);
    }

    public void setMenu() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        menu = inflater.inflate(R.layout.tp_menu, null);
        TPToolbar.this.menu.animate().alpha(0.0f).setDuration(0);
        show_menu = true;
        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_menu) {
                    TPToolbar.this.menu.animate().alpha(1.0f).setDuration(200);
                    show_menu = false;
                } else {
                    TPToolbar.this.menu.animate().alpha(0.0f).setDuration(200);
                    show_menu = true;
                }
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) getContext().getResources().getDimension(R.dimen.menu_width),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.BELOW, this.getId());
        menu.setLayoutParams(params);
        // get parent
        RelativeLayout parent = (RelativeLayout) getParent();
        parent.addView(menu);
    }

    public void setTitle(String title) {
        toolbarTitle.setText(title);
    }

    public void setProfilePicture(Drawable image) {
        profilePicture.setImageDrawable(image);
    }

    public void setHeartImage() {
        heartImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart_red));
    }

    public void setPlusImage() {
        heartImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.plus));
    }

    public void setHeartsTimer(int seconds) {
        String time = DateUtils.formatElapsedTime(seconds);
        heartsTimer.setText(time);
    }

    public void setLifeCounter(String text) {
        lifeCounter.setText(text);
    }

    public void setBackButtonText(String text) { backButtonText.setText(text); }
}

package com.ted_developers.triviapatente.app.utils.custom_classes.top_bar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

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
    protected TextView lifeCounter;
    protected TextView heartsTimer;
    // title
    protected TextView toolbarTitle;
    // profile picture
    protected ImageView profilePicture;
    // settings
    protected LinearLayout settings;


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
    }

    // bind all elements through their id
    private void bindElements() {
        // back button text
        backButton = (LinearLayout) findViewById(R.id.backButton);
        backButtonText = (TextView) findViewById(R.id.backButtonText);
        // hearts box
        heartsBox = (LinearLayout) findViewById(R.id.heartsBox);
        heartImage = (ImageView) findViewById(R.id.heartImage);
        lifeCounter = (TextView) findViewById(R.id.lifeCounter);
        heartsTimer = (TextView) findViewById(R.id.heartsTimer);
        // title
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        // profile picture
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        // settings
    }

    public void setTitle(String title) {
        toolbarTitle.setText(title);
    }

    public void setProfilePicture(Drawable image) {
        profilePicture.setImageDrawable(image);
    }
}

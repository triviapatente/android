package com.ted_developers.triviapatente.app.utils.custom_classes.top_bar;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.util.TypedValue;
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
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;

import java.util.concurrent.Callable;

/**
 * Created by Antonio on 31/10/16.
 */
public class TPToolbar extends RelativeLayout {
    // back button
    protected LinearLayout backButton;
    protected TextView backButtonText;
    // hearts box
    protected RelativeLayout heartsBox;
    protected ImageView heartImage;
    protected TextSwitcher lifeCounter;
    // title
    protected TextView toolbarTitle;
    // profile picture
    protected RoundedImageView profilePicture;
    // settings
    protected LinearLayout settings;
    // menu
    public View menu;
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

    public int getMenuVisibility() {
        return menu.getVisibility();
    }

    private void initElements(final Context context) {
        // init text switchers
        lifeCounter.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView myText = new TextView(context);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                myText.setLayoutParams(params);
                myText.setGravity(Gravity.CENTER | Gravity.TOP);
                myText.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.TPTextSizeSmall));
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
        heartsBox = (RelativeLayout) findViewById(R.id.heartsBoxImage);
        heartImage = (ImageView) findViewById(R.id.heartImage);
        lifeCounter = (TextSwitcher) findViewById(R.id.lifeCounter);
        // title
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        // profile picture
        profilePicture = (RoundedImageView) findViewById(R.id.profilePicture);
        // settings
        settings = (LinearLayout) findViewById(R.id.settings);
    }

    public void setMenu() {
        // get parent
        final RelativeLayout parent = (RelativeLayout) getParent();
        // inflate
        LayoutInflater inflater = LayoutInflater.from(getContext());
        menu = inflater.inflate(R.layout.tp_menu, null);
        menu.setVisibility(GONE);
        show_menu = true;
        // set on click
        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_menu) {
                    showMenu();
                } else {
                    hideMenu();
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
        parent.addView(menu);
    }

    public void showMenu() {
        menu.setVisibility(VISIBLE);
        menu.animate().alpha(1.0f).setDuration(200);
        show_menu = false;
    }

    public void hideMenu() {
        menu.animate().alpha(0.0f).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                menu.setVisibility(GONE);
            }
        });
        show_menu = true;
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

    public void setLifeCounter(String text) {
        lifeCounter.setText(text);
    }

    public void setBackButtonText(String text) { backButtonText.setText(text); }

    public void setBackButtonOnClick(OnClickListener listener) {
        backButton.setOnClickListener(listener);
    }
}

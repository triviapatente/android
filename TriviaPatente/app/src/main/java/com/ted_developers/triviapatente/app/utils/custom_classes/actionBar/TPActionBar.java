package com.ted_developers.triviapatente.app.utils.custom_classes.actionBar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.views.AlphaView;

/**
 * Created by Antonio on 31/10/16.
 */
public class TPActionBar extends RelativeLayout {
    // back button
    protected LinearLayout backButton;
    protected TextView backButtonText;
    // hearts box
    protected RelativeLayout heartsBox;
    protected ImageView heartImage;
    protected TextView lifeCounter;
    // title
    protected TextView toolbarTitle;
    // profile picture
    protected RoundedImageView profilePicture;
    // settings
    protected LinearLayout settings;
    // menu
    public View menu;
    private boolean show_menu;
    public Button menuProfileOptionButton, menuSettingsOptionButton, menuAboutOptionButton, menuLogoutOptionButton;
    public int id;

    public TPActionBar(Context context) {
        super(context);
        init(context);
    }

    public TPActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TPActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TPActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TPActionBar,
                0, 0);
        try {
            setTitle(a.getString(R.styleable.TPActionBar_action_bar_title));
            setBackButtonText(a.getString(R.styleable.TPActionBar_action_bar_back_title));
            id = a.getInt(R.styleable.TPActionBar_action_bar_type, 0);
            setType();
        } finally {
            a.recycle();
        }
    }

    private void init(Context context) {
        inflate(context, R.layout.action_bar, this);
        bindElements();
        initElements(context);
    }

    public int getMenuVisibility() {
        if(menu == null) return GONE;
        return menu.getVisibility();
    }

    private void initElements(final Context context) {
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
            }
        });
        profilePicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlphaView.class);
                context.startActivity(intent);
            }
        });
        heartsBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlphaView.class);
                context.startActivity(intent);
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
        lifeCounter = (TextView) findViewById(R.id.lifeCounter);
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
        menu = inflater.inflate(R.layout.layout_menu, null);
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
        setMenuOptions();
        // set buttons click
    }

    private void setMenuOptions() {
        // find menu options
        menuProfileOptionButton = (Button) menu.findViewById(R.id.menuProfileOption);
        menuAboutOptionButton = (Button) menu.findViewById(R.id.menuAboutOption);
        menuSettingsOptionButton = (Button) menu.findViewById(R.id.menuSettingsOption);
        menuLogoutOptionButton = (Button) menu.findViewById(R.id.menuLogoutOption);
    }

    public void showMenu() {
        menu.setVisibility(VISIBLE);
        menu.animate().alpha(1.0f).setDuration(200);
        show_menu = false;
    }

    public void hideMenu() {
        hideMenu(null);
    }

    public void hideMenu(final SimpleCallback cb) {
        menu.animate().alpha(0.0f).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                menu.setVisibility(GONE);
                if(cb != null) {
                    cb.execute();
                }
            }
        });
        show_menu = true;
    }

    public void setTitle(String title) {
        toolbarTitle.setText(title);
    }
    public String getTitle() {
        return toolbarTitle.getText().toString();
    }
    public void setProfilePicture(String imageURL) {
        Picasso.with(getContext())
                .load(imageURL)
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into(profilePicture);
    }

    public void setHeartImage() {
        heartImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.image_heart_red));
    }

    public void setPlusImage() {
        heartImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.image_plus));
    }

    public void setLifeCounter(int counter) {
        String strCounter = (counter == - 1)? "∞" : String.valueOf(counter);
        lifeCounter.setText(strCounter);
    }

    public void setBackButtonText(String text) { backButtonText.setText(text); }

    public void setBackButtonOnClick(final Class<? extends Activity> nextActivityClass) {
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), nextActivityClass);
                getContext().startActivity(intent);
                ((Activity)getContext()).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
    }

    // set action bar type
    private void setType() {
        switch (TPActionBarType.fromId(id)) {
            case backPicture: {
                // hide heart box
                heartsBox.setVisibility(GONE);
                // hide settings
                settings.setVisibility(GONE);
            } break;
            case backPictureMenu: {
                // hide heart box
                heartsBox.setVisibility(GONE);
            } break;
            case heartPictureMenu: {
                // hide back button
                backButton.setVisibility(GONE);
            } break;
        }
    }
}

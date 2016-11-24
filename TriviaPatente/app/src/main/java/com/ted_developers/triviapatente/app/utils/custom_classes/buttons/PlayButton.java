package com.ted_developers.triviapatente.app.utils.custom_classes.buttons;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.widget.Button;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 12/11/16.
 */
public class PlayButton extends Button {
    // strings
    String playNow, newGame, details;
    // drawables (different types of buttons)
    Drawable playNowDrawable, newGameDrawable, detailsDrawable;
    // colors
    int playNowColor, newGameColor, detailsColor;

    public PlayButton(Context context) {
        super(context);
        bindElements(context);
    }

    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        bindElements(context);
    }

    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindElements(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        bindElements(context);
    }

    private void bindElements(Context context) {
        TextViewCompat.setTextAppearance(this, R.style.TPTextStyleSmall);
        // bind strings
        playNow = getResources().getString(R.string.play_now_button_text);
        newGame = getResources().getString(R.string.new_game_button_text);
        details = getResources().getString(R.string.details_button_text);
        // bind drawables
        playNowDrawable = ContextCompat.getDrawable(context, R.drawable.green_on_white);
        newGameDrawable = ContextCompat.getDrawable(context, R.drawable.red_on_white_button);
        detailsDrawable = ContextCompat.getDrawable(context, R.drawable.yellow_on_white_button);
        // bind colors
        playNowColor = ContextCompat.getColor(context, R.color.green);
        newGameColor = ContextCompat.getColor(context, R.color.red);
        detailsColor = ContextCompat.getColor(context, R.color.yellow);
    }

    // the button is a play now one
    public void setPlayNow() {
        this.setBackground(playNowDrawable);
        this.setText(playNow);
        this.setTextColor(playNowColor);
    }

    // the button is a new game one
    public void setNewGame() {
        this.setBackground(newGameDrawable);
        this.setText(newGame);
        this.setTextColor(newGameColor);
    }

    // the button is a details one
    public void setDetails() {
        this.setBackground(detailsDrawable);
        this.setText(details);
        this.setTextColor(detailsColor);
    }
}

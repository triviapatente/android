package com.ted_developers.triviapatente.app.utils.custom_classes.buttons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.views.game_page.GameMainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 12/11/16.
 */
public class PlayButton extends Button implements View.OnClickListener {
    // TODO get custom attributes from xml to set state
    // on click
    Long gameID;
    User opponent;
    boolean new_game;

    public PlayButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnClickListener(this);
    }

    // the button is a play now one
    public void setPlayNow() {
        setUIData(R.drawable.button_green_on_white, R.color.green_on_white, R.string.play_now_button_text);
        setClickable(true);
    }
    // the button is a play now one
    public void setReplayNow() {
        setUIData(R.drawable.button_green_on_white, R.color.green_on_white, R.string.replay_now_button_text);
        setClickable(true);
    }

    // the button is a new game one
    //colorFilled determines whether the button has white (colorFilled = false) or coloured background
    public void setNewGame(Boolean colorFilled) {
        if(colorFilled) {
            setUIData(R.drawable.button_red, android.R.color.white, R.string.new_game_button_text);
        } else {
            setUIData(R.drawable.button_red_on_white, R.color.red_on_white, R.string.new_game_button_text);
        }
        setClickable(true);
    }

    // the button is a summary one
    public void setSummary() {
        setUIData(R.drawable.button_red_on_white, R.color.red_on_white, R.string.summary_button_text);
        setClickable(true);
    }

    // the button is a details one
    public void setWait() {
        setUIData(R.drawable.button_yellow_on_white, R.color.yellow_on_white, R.string.wait_button_text);
        setClickable(true);
    }

    private void setUIData(@DrawableRes int drawableRes, @ColorRes int colorRes, @StringRes int stringRes) {
        Context context = getContext();
        this.setText(context.getString(stringRes));
        this.setTextColor(ContextCompat.getColorStateList(context, colorRes));
        this.setBackground(ContextCompat.getDrawable(context, drawableRes));
    }

    public void sendInvite(User element) {
        this.opponent = element;
        new_game = true;
    }

    public void goToGame(Long gameID, User user) {
        this.gameID = gameID;
        this.opponent = user;
        new_game = false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), GameMainPageActivity.class);
        Context context = getContext();
        if(new_game) {
            intent.putExtra(context.getString(R.string.extra_boolean_game), true);
        } else if(gameID != null) {
            intent.putExtra(context.getString(R.string.extra_long_game), gameID);
        }
        if(opponent != null) {
            intent.putExtra(context.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
        }
        getContext().startActivity(intent);
    }
}
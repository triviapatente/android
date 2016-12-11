package com.ted_developers.triviapatente.app.utils.custom_classes.buttons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.views.game_page.GameMainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 12/11/16.
 */
public class PlayButton extends Button implements View.OnClickListener {
    // strings
    String playNow, newGame, details, endGame;
    // drawables (different types of buttons)
    Drawable playNowDrawable, newGameDrawable, detailsDrawable;
    // colors
    int playNowColor, newGameColor, detailsColor;
    // on click
    Long gameID;
    User opponent;
    boolean new_game;
    String extraBooleanGame, extraLongGame, extraStringOpponent;


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
        setOnClickListener(this);
        // bind strings
        playNow = getResources().getString(R.string.play_now_button_text);
        newGame = getResources().getString(R.string.new_game_button_text);
        endGame = getResources().getString(R.string.summary_button_text);
        details = getResources().getString(R.string.details_button_text);

        extraStringOpponent = getResources().getString(R.string.extra_string_opponent);
        extraBooleanGame = getResources().getString(R.string.extra_boolean_game);
        extraLongGame = getResources().getString(R.string.extra_long_game);
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

    // the button is a summary one
    public void setSummary() {
        this.setBackground(newGameDrawable);
        this.setText(endGame);
        this.setTextColor(newGameColor);
    }

    // the button is a details one
    public void setDetails() {
        this.setBackground(detailsDrawable);
        this.setText(details);
        this.setTextColor(detailsColor);
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
        if(new_game) {
            intent.putExtra(extraBooleanGame, true);
        } else if(gameID != null) {
            intent.putExtra(extraLongGame, gameID);
        }
        if(opponent != null) {
            intent.putExtra(extraStringOpponent, RetrofitManager.gson.toJson(opponent));
        }
        getContext().startActivity(intent);
    }
}

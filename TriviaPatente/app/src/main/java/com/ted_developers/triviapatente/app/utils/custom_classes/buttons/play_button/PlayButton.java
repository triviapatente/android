package com.ted_developers.triviapatente.app.utils.custom_classes.buttons.play_button;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
    // ui data centralized
    ui_data uiData;
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
        // bind strings for extras
        extraStringOpponent = getResources().getString(R.string.extra_string_opponent);
        extraBooleanGame = getResources().getString(R.string.extra_boolean_game);
        extraLongGame = getResources().getString(R.string.extra_long_game);
        // init ui_data
        ui_data.init(context);
    }

    // the button is a play now one
    public void setPlayNow() {
        uiData = ui_data._play_now;
        setUiData();
    }

    // the button is a new game one
    public void setNewGame() {
        uiData = ui_data._new_game;
        setUiData();
    }

    // the button is a summary one
    public void setSummary() {
        uiData = ui_data._summary;
        setUiData();
    }

    // the button is a details one
    public void setDetails() {
        uiData = ui_data._details;
        setUiData();
    }

    // the button is a contact one
    public void setContact() {
        uiData = ui_data._contact;
        setUiData();
    }

    private void setUiData() {
        this.setBackground(uiData.getDrawable());
        this.setText(uiData.getString());
        this.setTextColor(uiData.getColor());
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
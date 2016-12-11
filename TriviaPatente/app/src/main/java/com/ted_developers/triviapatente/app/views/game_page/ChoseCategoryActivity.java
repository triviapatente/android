package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Round;

import butterknife.BindString;
import butterknife.BindView;

public class ChoseCategoryActivity extends TPActivity {

    // data
    @BindString(R.string.extra_string_round) String extraStringRound;
    @BindString(R.string.extra_string_opponent) String extraStringOpponent;
    private User opponent;
    private Round currentRound;
    // toolbar
    @BindView(R.id.toolbar) BackPictureTPToolbar toolbar;
    // game header
    @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    @BindString(R.string.choose_category_game_header_subtitle) String chooseCategoryGameHeaderSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_category);
        // init
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(extraStringOpponent), User.class);
        initToolbar();
        initGameHeader();
    }

    private void initToolbar() {
        // title
        if(opponent != null && toolbar.getTitle().equals("")) {
            if(opponent.name == null || opponent.surname == null) {
                toolbar.setTitle(opponent.username);
            } else {
                toolbar.setTitle(opponent.name + " " + opponent.surname);
            }
        }
        // picture
        // todo get dinamically
        toolbar.setProfilePicture(ContextCompat.getDrawable(this, R.drawable.no_image));
    }

    private void initGameHeader() {
        gameHeaderTitle.setText("Round " + currentRound.number);
        gameHeaderSubtitle.setText(chooseCategoryGameHeaderSubtitle  );
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }
}

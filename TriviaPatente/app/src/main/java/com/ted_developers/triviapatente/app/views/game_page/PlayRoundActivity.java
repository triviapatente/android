package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPActionBar;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayRoundActivity extends TPActivity {
    // game data
    User opponent;
    Round currentRound;
    Category currentCategory;
    // action bar
    @BindView(R.id.toolbar) BackPictureTPActionBar toolbar;
    // game header
    @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    @BindView(R.id.subtitleImage) ImageView gameHeaderSubtitleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_round)), Round.class);
        currentCategory = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_category)), Category.class);
        initActionbar();
        initGameHeader();
    }

    private void initActionbar() {
        // title
        if(opponent != null && toolbar.getTitle().equals("")) {
            if(opponent.name == null || opponent.surname == null) {
                toolbar.setTitle(opponent.username);
            } else {
                toolbar.setTitle(opponent.name + " " + opponent.surname);
            }
        }
        // profile picture
        // todo do dinamically
        toolbar.setProfilePicture(ContextCompat.getDrawable(this, R.drawable.no_image));
    }

    private void initGameHeader() {
        // game header title
        gameHeaderTitle.setText("Round " + currentRound.number);
        // game header subtitle
        gameHeaderSubtitle.setText(currentCategory.hint);
        // todo set game header subtitle image dinamically
        gameHeaderSubtitleImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.no_image));
        gameHeaderSubtitleImage.setVisibility(View.VISIBLE);
    }

    // option button panel
    @OnClick(R.id.gameChatButton)
    public void gameChatButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @OnClick(R.id.gameDetailsButton)
    public void gameDetailsButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @OnClick(R.id.gameLeaveButton)
    public void gameLeaveButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}

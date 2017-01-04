package com.ted_developers.triviapatente.app.utils.baseActivityClasses;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Antonio on 04/01/17.
 */
public class TPGameActivity extends TPActivity {
    // game header
    protected @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    protected @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    protected @BindView(R.id.subtitleImage) ImageView gameHeaderSubtitleImage;
    // game data
    // game data
    protected User opponent;
    protected Round currentRound;
    protected Category currentCategory;
    protected Long gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_round)), Round.class);
        currentCategory = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_category)), Category.class);
        gameID = intent.getLongExtra(this.getString(R.string.extra_long_game), (currentRound == null)? -1 : currentRound.game_id);
        super.onCreate(savedInstanceState);
    }

    // game header centralized management
    protected void setGameHeader(String titleText, String subtitleText, Drawable subtitleImage) {
        gameHeaderTitle.setText(titleText);
        gameHeaderSubtitle.setText(subtitleText);
        if(subtitleImage == null) {
            gameHeaderSubtitleImage.setVisibility(View.GONE);
        } else {
            gameHeaderSubtitleImage.setImageDrawable(subtitleImage);
            gameHeaderSubtitleImage.setVisibility(View.VISIBLE);
        }
    }

    // game options centralized management
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
        //TPUtils.blurContainerIntoImageView(TPGameActivity.this, gameContainer, blurredBackgroundView);
        //blurredBackgroundContainer.setVisibility(View.VISIBLE);
        //showing modal
        //leaveDialog.show();
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        if(opponent != null) {
            actionBar.setTitle(opponent.toString());
        }
        actionBar.setBackButtonOnClick(MainPageActivity.class);
    }

    @Override
    protected Drawable getActionBarProfilePicture() {
        // todo return opponent image
        return ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }
}

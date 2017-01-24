package com.ted_developers.triviapatente.app.utils.baseActivityClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPLeaveDialog;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.game_page.FragmentGameHeader;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.modules.game.HTTPGameEndpoint;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessDecrement;

import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Antonio on 04/01/17.
 */
public class TPGameActivity extends TPActivity {
    // game data
    public User opponent;
    public Round currentRound;
    public Category currentCategory;
    public Long gameID;
    // game header
    protected FragmentGameHeader gameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_round)), Round.class);
        currentCategory = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_category)), Category.class);
        gameID = intent.getLongExtra(this.getString(R.string.extra_long_game), (currentRound == null)? -1 : currentRound.game_id);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        // init fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        gameHeader = (FragmentGameHeader) fragmentManager.findFragmentById(R.id.gameHeader);
        gameHeader.setHeader(currentRound, currentCategory);
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
    protected String getActionBarProfilePicture() {
        return (opponent == null)? null : TPUtils.getUserImageFromID(this, opponent.id);
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

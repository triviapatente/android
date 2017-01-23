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
    protected User opponent;
    protected Round currentRound;
    protected Category currentCategory;
    protected Long gameID;
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


    // game options centralized management
    // option button panel
    @Optional
    @OnClick(R.id.gameChatButton)
    public void gameChatButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        /*Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));*/
        startActivity(intent);
    }
    @Optional
    @OnClick(R.id.gameDetailsButton)
    public void gameDetailsButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @Optional
    @OnClick(R.id.gameLeaveButton)
    public void gameLeaveButtonClick() {
        TPUtils.blurContainerIntoImageView(TPGameActivity.this, activityContainer, blurredBackgroundView);
        blurredBackgroundContainer.setVisibility(View.VISIBLE);
        //showing modal
        final HTTPGameEndpoint httpGameEndpoint = RetrofitManager.getHTTPGameEndpoint();
        httpGameEndpoint.getLeaveDecrement(gameID).enqueue(new TPCallback<SuccessDecrement>() {
            @Override
            public void mOnResponse(Call<SuccessDecrement> call, Response<SuccessDecrement> response) {
                if(response.body().success) {
                    new TPLeaveDialog(TPGameActivity.this, response.body().decrement, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            blurredBackgroundContainer.setVisibility(View.GONE);
                        }
                    }) {
                        @Override
                        public void onNegativeButtonClick() {
                            httpGameEndpoint.leaveGame(gameID).enqueue(new TPCallback<Success>() {
                                @Override
                                public void mOnResponse(Call<Success> call, Response<Success> response) {
                                    if(response.body().success) {
                                        TPGameActivity.this.onBackPressed();
                                    }
                                }

                                @Override
                                public void mOnFailure(Call<Success> call, Throwable t) {}

                                @Override
                                public void then() {}
                            });
                        }

                        @Override
                        public void onPositiveButtonClick() {
                            cancel();
                        }
                    }.show();
                }
            }

            @Override
            public void mOnFailure(Call<SuccessDecrement> call, Throwable t) {

            }

            @Override
            public void then() {

            }
        });
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

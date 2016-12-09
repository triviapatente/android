package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading.Circle;
import com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading.CircleRotatingAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;
import com.ted_developers.triviapatente.models.responses.SuccessInitRound;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class GameMainPageActivity extends TPActivity {
    // opponent data
    Drawable opponentImage;
    User opponent;
    Long gameID;
    // toolbar
    @BindView(R.id.toolbar) BackPictureTPToolbar toolbar;
    @BindString(R.string.main_page_title) String mainPageTitle;
    // wait page
    @BindView(R.id.waitPage) RelativeLayout waitPage;
    @BindView(R.id.bigProfilePicture) RoundedImageView profilePicture;
    // titles
    @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    @BindString(R.string.wait_page_title_pendingInvite) String pendingInviteTitle;
    // subtitles
    @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    @BindView(R.id.subtitleImage) RoundedImageView subtitleImage;
    @BindString(R.string.wait_page_subtitle_pendingInvite) String pendingInviteSubtitle;
    @BindString(R.string.wait_page_subtitle_waitingCategory) String waitingCategorySubtitle;
    @BindString(R.string.wait_page_subtitle_offline) String offlineSubtitle;
    // status
    @BindView(R.id.status) TextView gameStatus;
    @BindString(R.string.wait_page_offline_status) String offlineStatus;
    @BindString(R.string.wait_page_playing_status) String playingStatus;
    @BindString(R.string.wait_page_waitingCategory_status) String waitingCategoryStatus;
    @BindString(R.string.wait_page_pendingInvite_status) String pendingInviteStatus;
    // animation
    @BindView(R.id.loadingCircle) Circle loadingCircle;
    @BindColor(R.color.red) int redColor;
    @BindColor(R.color.green) int greenColor;
    @BindColor(R.color.yellow) int yellowColor;
    // sockets
    @BindString(R.string.room_name_game) String roomName;
    GameSocketManager gameSocketManager = new GameSocketManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main_page);
        //init
        Intent intent = getIntent();
        try {
            opponent = RetrofitManager.gson.fromJson(intent.getStringExtra("opponent"), User.class);
        } catch (Exception e) {
            opponent = null;
        }
        smallInit();
        startLoading();
        if(intent.getBooleanExtra("new_game", false)) {
            Call<SuccessGameUser> call;
            if(opponent != null) {
                call = RetrofitManager.getHTTPGameEndpoint().newGame(opponent.id);
            } else {
                call = RetrofitManager.getHTTPGameEndpoint().newRandomGame();
            }
            call.enqueue(new TPCallback<SuccessGameUser>() {
                @Override
                public void mOnResponse(Call<SuccessGameUser> call, Response<SuccessGameUser> response) {
                    if(response.code() == 200 && response.body().success) {
                        gameID = response.body().game.id;
                        opponent = response.body().user;
                    }
                }

                @Override
                public void mOnFailure(Call<SuccessGameUser> call, Throwable t) {}

                @Override
                public void then() {
                    fullInit();
                }
            });
        } else {
            gameID = intent.getLongExtra("game_id", -1);
            fullInit();
        }
    }

    private void smallInit() {
        opponentImage = ContextCompat.getDrawable(this, R.drawable.no_image);
        setToolbarTitle();
        toolbar.setProfilePicture(opponentImage);
        toolbar.setBackButtonText(mainPageTitle);
        toolbar.setBackButtonOnClick(this, MainPageActivity.class);
        profilePicture.setImageDrawable(opponentImage);
    }

    private void startLoading() {
        CircleRotatingAnimation animation = new CircleRotatingAnimation(loadingCircle);
        animation.setDuration(10000);
        animation.setRepeatCount(Animation.INFINITE);
        loadingCircle.startAnimation(animation);
    }

    private void setToolbarTitle() {
        if(opponent != null && toolbar.getTitle().equals("")) {
            if(opponent.name == null || opponent.surname == null) {
                toolbar.setTitle(opponent.username);
            } else {
                toolbar.setTitle(opponent.name + " " + opponent.surname);
            }
        }
    }

    private void fullInit() {
        gameSocketManager.join_room(gameID, roomName, new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                if(response.success) {
                    gameSocketManager.init_round(gameID, new SocketCallback<SuccessInitRound>() {
                        @Override
                        public void response(final SuccessInitRound response) {
                            if(response.success) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setToolbarTitle();
                                        if("invite".equals(response.waiting)) {
                                            waitingInvite();
                                        } else if(response.waiting_for.username.equals(opponent.username)) {
                                            // todo do dinamically
                                            opponentImage = ContextCompat.getDrawable(GameMainPageActivity.this, R.drawable.no_image);
                                            toolbar.setProfilePicture(opponentImage);
                                            profilePicture.setImageDrawable(opponentImage);
                                            if(response.isOpponentOnline) {
                                                if("game".equals(response.waiting)) {
                                                    waitingRound(response.round, response.category);
                                                } else if("category".equals(response.waiting)) {
                                                    waitingCategory(response.round);
                                                }
                                            } else {
                                                offline(response.round);
                                            }
                                        } else {
                                            if("game".equals(response.waiting)) {
                                                playRound();
                                            } else if("category".equals(response.waiting)) {
                                                chooseCategory();
                                            }
                                        }
                                    }
                                });
                            } else {
                                // todo communicate error on init round
                                Log.i("TEST", "error");
                            }
                        }
                    });
                } else {
                    // todo manage error on join room
                    Log.i("TEST", "ERRORE NEL JOIN ROOM!!");
                }
            }
        });
    }

    private void waitingInvite() {
        gameHeaderTitle.setText(pendingInviteTitle);
        gameHeaderSubtitle.setText(pendingInviteSubtitle);
        gameStatus.setText(pendingInviteStatus);
        subtitleImage.setVisibility(View.GONE);
        // todo set spinning color of the right color
    }

    private void waitingCategory(Round round) {
        gameHeaderTitle.setText("Round " + round.number);
        gameHeaderSubtitle.setText(waitingCategorySubtitle);
        gameStatus.setText(waitingCategoryStatus);
        subtitleImage.setVisibility(View.GONE);
        // todo set spinning color of the right color
    }

    private void waitingRound(Round round, Category category) {
        gameHeaderTitle.setText("Round " + round.number);
        // todo get image and set it
        subtitleImage.setBackground(getResources().getDrawable(R.drawable.no_image));
        subtitleImage.setVisibility(View.VISIBLE);
        gameHeaderSubtitle.setText(category.name);
        gameStatus.setText(playingStatus);
        // todo set spinning color of the right color
    }

    private void offline(Round round) {
        gameHeaderTitle.setText("Round " + round.number);
        gameHeaderSubtitle.setText(offlineSubtitle);
        gameStatus.setText(offlineStatus);
        subtitleImage.setVisibility(View.GONE);
        // todo set spinning color of the right color
    }

    private void chooseCategory() {
        // todo open activity
    }

    private void playRound() {
        // todo open activity
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }
}

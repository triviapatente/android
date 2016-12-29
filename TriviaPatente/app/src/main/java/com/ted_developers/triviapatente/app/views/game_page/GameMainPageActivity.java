package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading.Circle;
import com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading.CircleRotatingAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPActionBar;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.game_page.play_round.PlayRoundActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.Accepted;
import com.ted_developers.triviapatente.models.responses.RoundUserData;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;
import com.ted_developers.triviapatente.models.responses.SuccessInitRound;
import com.ted_developers.triviapatente.models.responses.WinnerPartecipationsUserleft;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class GameMainPageActivity extends TPActivity {
    // option button
    @BindView(R.id.gameChatButton) Button gameChatButton;
    @BindView(R.id.gameDetailsButton) Button gameDetailsButton;
    @BindView(R.id.gameLeaveButton) Button gameLeaveButton;
    // data
    @BindString(R.string.extra_string_opponent) String extraStringOpponent;
    @BindString(R.string.extra_boolean_game) String extraBooleanGame;
    @BindString(R.string.extra_long_game) String extraLongGame;
    @BindString(R.string.extra_string_round) String extraStringRound;
    @BindString(R.string.extra_string_category) String extraStringCategory;
    private Drawable opponentImage;
    private User opponent;
    private Long gameID;
    private Category currentCategory;
    private Round currentRound;
    // action_bar
    @BindView(R.id.toolbar)
    BackPictureTPActionBar toolbar;
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
    @BindColor(R.color.redLight) int redColorLight;
    @BindColor(R.color.green) int greenColor;
    @BindColor(R.color.greenLight) int greenColorLight;
    @BindColor(R.color.yellow) int yellowColor;
    @BindColor(R.color.yellowLight) int yellowColorLight;
    @BindColor(R.color.mainColorLight) int mainColorLight;
    @BindColor(android.R.color.white) int whiteColor;
    // sockets
    @BindString(R.string.room_name_game) String roomName;
    @BindString(R.string.invite_refused) String inviteRefused;
    GameSocketManager gameSocketManager = new GameSocketManager();
    // sockets parameters
    @BindString(R.string.socket_response_waiting_category) String waitingCategory;
    @BindString(R.string.socket_response_waiting_game) String waitingGame;
    @BindString(R.string.socket_response_waiting_invite) String waitingInvite;
    // sockets events
    @BindString(R.string.socket_event_game_ended) String eventGameEnded;
    @BindString(R.string.socket_event_game_left) String eventGameLeft;
    @BindString(R.string.socket_event_invite_processed) String eventInviteProcessed;
    @BindString(R.string.socket_event_round_ended) String eventRoundEnded;
    @BindString(R.string.socket_event_round_started) String eventRoundStarted;
    @BindString(R.string.socket_event_category_chosen) String eventCategoryChosen;
    // sockets callbacks
    SocketCallback initRoundCallback = new SocketCallback<SuccessInitRound>() {
        @Override
        public void response(final SuccessInitRound response) {
            if(response.success) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentRound = response.round;
                        currentCategory = response.category;
                        if(response.ended != null && response.ended) {
                            // todo go to round details
                        } else if(waitingInvite.equals(response.waiting)) {
                            waitingInvite();
                        } else if(response.waiting_for.username.equals(opponent.username)) {
                            if(response.isOpponentOnline) {
                                if(waitingGame.equals(response.waiting)) {
                                    waitingRound();
                                } else if(waitingCategory.equals(response.waiting)) {
                                    waitingCategory();
                                }
                            } else {
                                offline();
                            }
                        } else {
                            if(waitingGame.equals(response.waiting)) {
                                playRound();
                            } else if(waitingCategory.equals(response.waiting)) {
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
    }, roundCallback = new SocketCallback<RoundUserData>() {
        @Override
        public void response(RoundUserData response) {
            if(response.globally == null) {
                // round started
                GameMainPageActivity.this.waitingRound();
            } else {
                // round ended
                init_round();
            }
        }
    }, gameCallback = new SocketCallback<WinnerPartecipationsUserleft>() {
        @Override
        public void response(WinnerPartecipationsUserleft response) {
            // todo go to round details
        }
    }, inviteCallback = new SocketCallback<Accepted>() {
        @Override
        public void response(Accepted response) {
            if(response.accepted) {
                init_round();
            } else {
                Toast.makeText(GameMainPageActivity.this, inviteRefused,
                        Toast.LENGTH_LONG).show();
                GameMainPageActivity.this.finish();
            }
        }
    }, chosenCategoryCallback = new SocketCallback<Category>() {
        @Override
        public void response(Category response) {
            currentCategory = response;
            // todo go to play round
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main_page);
        //init
        Intent intent = getIntent();
        try {
            opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(extraStringOpponent), User.class);
        } catch (Exception e) {
            opponent = null;
        }
        smallInit();
        startLoading();
        if(intent.getBooleanExtra(extraBooleanGame, false)) {
            Call<SuccessGameUser> call;
            if(opponent != null) {
                call = RetrofitManager.getHTTPGameEndpoint().newGame(opponent.id);
                setToolbarTitle();
                setOpponentData();
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
                    setToolbarTitle();
                    setOpponentData();
                    join_room();
                    init_listening();
                }
            });
        } else {
            gameID = intent.getLongExtra(extraLongGame, -1);
            join_room();
            init_listening();
        }
    }

    private void smallInit() {
        opponentImage = ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture);
        setToolbarTitle();
        toolbar.setProfilePicture(opponentImage);
        toolbar.setBackButtonOnClick(MainPageActivity.class);
        profilePicture.setImageDrawable(opponentImage);
    }

    private void startLoading() {
        CircleRotatingAnimation animation = new CircleRotatingAnimation(loadingCircle);
        animation.setDuration(7000);
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

    private void setOpponentData() {
        // todo do dinamically
        opponentImage = ContextCompat.getDrawable(GameMainPageActivity.this, R.drawable.image_no_profile_picture);
        toolbar.setProfilePicture(opponentImage);
        profilePicture.setImageDrawable(opponentImage);
    }

    private void join_room() {
        gameSocketManager.join_room(gameID, roomName, new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                if(response.success) {
                    init_round();
                } else {
                    Log.i("TEST", "ERRORE NEL JOIN ROOM");
                }
            }
        });
    }

    private void init_round() {
        gameSocketManager.init_round(gameID, initRoundCallback);
    }

    private void init_listening() {
        listen(eventGameEnded, WinnerPartecipationsUserleft.class, gameCallback);
        listen(eventGameLeft, WinnerPartecipationsUserleft.class, gameCallback);
        listen(eventInviteProcessed, Accepted.class, inviteCallback);
        listen(eventRoundStarted, RoundUserData.class, roundCallback);
        listen(eventRoundEnded, RoundUserData.class, roundCallback);
        listen(eventCategoryChosen, Category.class, chosenCategoryCallback);
    }

    private void updateWaitPage(String title, String subtitle, String status, Drawable subtitleImage, @ColorInt int overColor, @ColorInt int underColor) {
        gameHeaderTitle.setText(title);
        gameHeaderSubtitle.setText(subtitle);
        gameStatus.setText(status);
        if(this.subtitleImage == null) {
            this.subtitleImage.setVisibility(View.GONE);
        } else {
            this.subtitleImage.setImageDrawable(subtitleImage);
            this.subtitleImage.setVisibility(View.VISIBLE);
        }
        loadingCircle.setColorUnder(underColor);
        loadingCircle.setColorOver(overColor);
    }

    private void waitingInvite() {
        if(visible) {
            updateWaitPage(pendingInviteTitle, pendingInviteSubtitle, pendingInviteStatus, null, redColor, redColorLight);
        }
    }

    private void waitingCategory() {
        if(visible) {
            updateWaitPage("Round " + currentRound.number, waitingCategorySubtitle, waitingCategoryStatus, null, yellowColor, yellowColorLight);
        }
    }

    private void waitingRound() {
        // todo get image and set it
        Drawable d = getResources().getDrawable(R.drawable.image_no_profile_picture);
        if(visible) {
            updateWaitPage("Round " + currentRound.number, currentCategory.name, playingStatus, d, greenColor, greenColorLight);
        }
    }

    private void offline() {
        if(visible) {
            updateWaitPage("Round " + currentRound.number, offlineSubtitle, offlineStatus, null, whiteColor, mainColorLight);
        }
    }

    private void chooseCategory() {
        Intent intent = new Intent(this, ChooseCategoryActivity.class);
        intent.putExtra(extraStringRound, RetrofitManager.gson.toJson(currentRound));
        intent.putExtra(extraStringOpponent, RetrofitManager.gson.toJson(opponent));
        startActivity(intent);
        finish();
    }

    private void playRound() {
        Intent intent = new Intent(this, PlayRoundActivity.class);
        intent.putExtra(extraStringOpponent, RetrofitManager.gson.toJson(opponent));
        intent.putExtra(extraStringRound, RetrofitManager.gson.toJson(currentRound));
        intent.putExtra(extraStringCategory, RetrofitManager.gson.toJson(currentCategory));
        startActivity(intent);
        finish();
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
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
    }
}

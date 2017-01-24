package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.ReceivedData;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading.Circle;
import com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading.CircleRotatingAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.game_page.play_round.PlayRoundActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
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

public class GameMainPageActivity extends TPGameActivity {
    // data
    @BindString(R.string.extra_boolean_game) String extraBooleanGame;
    // wait page
    @BindView(R.id.waitPage) RelativeLayout waitPage;
    @BindView(R.id.bigProfilePicture) RoundedImageView profilePicture;
    // status
    @BindView(R.id.status) TextView gameStatus;
    @BindString(R.string.wait_page_offline_status) String offlineStatus;
    @BindString(R.string.wait_page_playing_status) String playingStatus;
    @BindString(R.string.wait_page_waitingCategory_status) String waitingCategoryStatus;
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
    GameSocketManager gameSocketManager = new GameSocketManager();
    // sockets parameters
    @BindString(R.string.socket_response_waiting_category) String waitingCategory;
    @BindString(R.string.socket_response_waiting_game) String waitingGame;
    // sockets events
    @BindString(R.string.socket_event_game) String eventGameEvent;
    @BindString(R.string.socket_event_round_ended) String eventRoundEnded;
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
    }, chosenCategoryCallback = new SocketCallback<Category>() {
        @Override
        public void response(Category response) {
            currentCategory = response;
            playRound();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main_page);
        //init
        smallInit();
        startLoading();
        if(getIntent().getBooleanExtra(extraBooleanGame, false)) { createGame(); }
        else {
            setOpponentData();
            join_room();
            init_listening();
        }
    }

    private void createGame() {
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
                    Game game = response.body().game;
                    gameID = game.id;
                    opponent = response.body().user;
                    ReceivedData.addGame(game);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessGameUser> call, Throwable t) {}

            @Override
            public void then() {
                setOpponentData();
                join_room();
                init_listening();
            }
        });
    }

    private void smallInit() {
        if(opponent != null) { actionBar.setTitle(opponent.toString()); }
        actionBar.setBackButtonOnClick(MainPageActivity.class);
        profilePicture.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture));
    }

    private void startLoading() {
        CircleRotatingAnimation animation = new CircleRotatingAnimation(loadingCircle);
        animation.setDuration(7000);
        animation.setRepeatCount(Animation.INFINITE);
        loadingCircle.startAnimation(animation);
    }

    private void setOpponentData() {
        if(opponent != null) { actionBar.setTitle(opponent.toString()); }
        actionBar.setProfilePicture(TPUtils.getUserImageFromID(this, opponent.id));
        Picasso.with(this)
                .load(TPUtils.getUserImageFromID(this, opponent.id))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into(profilePicture);
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
        listen(eventGameEvent, WinnerPartecipationsUserleft.class, gameCallback);
        listen(eventRoundEnded, RoundUserData.class, roundCallback);
        listen(eventCategoryChosen, Category.class, chosenCategoryCallback);
    }

    private void updateWaitPage(String status, @ColorInt int overColor, @ColorInt int underColor) {
        if(visible) {
            gameHeader.setHeader(currentRound, currentCategory);
            gameStatus.setText(status);
            loadingCircle.setColorUnder(underColor);
            loadingCircle.setColorOver(overColor);
        }
    }

    private void waitingCategory() {
        updateWaitPage(waitingCategoryStatus, yellowColor, yellowColorLight);
    }

    private void waitingRound() {
        updateWaitPage(playingStatus, greenColor, greenColorLight);
    }

    private void offline() {
        updateWaitPage(offlineStatus, whiteColor, mainColorLight);
    }

    private void chooseCategory() {
        Intent intent = new Intent(this, ChooseCategoryActivity.class);
        intent.putExtra(this.getString(R.string.extra_string_round), RetrofitManager.gson.toJson(currentRound));
        intent.putExtra(this.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
        startActivity(intent);
        finish();
    }

    private void playRound() {
        Intent intent = new Intent(this, PlayRoundActivity.class);
        intent.putExtra(this.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
        intent.putExtra(this.getString(R.string.extra_string_round), RetrofitManager.gson.toJson(currentRound));
        intent.putExtra(this.getString(R.string.extra_string_category), RetrofitManager.gson.toJson(currentCategory));
        startActivity(intent);
        finish();
    }
}

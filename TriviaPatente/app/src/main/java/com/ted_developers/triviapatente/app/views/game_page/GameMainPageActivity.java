package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.support.annotation.ColorInt;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.ReceivedData;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.animation.circleLoading.Circle;
import com.ted_developers.triviapatente.app.utils.custom_classes.animation.circleLoading.CircleRotatingAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.views.game_page.play_round.PlayRoundActivity;
import com.ted_developers.triviapatente.app.views.game_page.round_details.RoundDetailsActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.responses.RoundUserData;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessCategory;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;
import com.ted_developers.triviapatente.models.responses.SuccessInitRound;
import com.ted_developers.triviapatente.models.responses.WinnerPartecipationsUserleft;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class GameMainPageActivity extends TPGameActivity {
    @BindString(R.string.main_page_title) String title;
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
    @BindString(R.string.socket_event_user_left) String eventUserLeft;
    @BindString(R.string.socket_event_user_joined) String eventUserJoined;
    @BindString(R.string.socket_event_category_chosen) String eventCategoryChosen;

    private Boolean isWaiting = false;
    // sockets callbacks
    SocketCallback roundInitCallback = new SocketCallback<SuccessInitRound>() {
        @Override
        public void response(final SuccessInitRound response) {
            if(response.success) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentRound = response.round;
                        currentCategory = response.category;
                        GameMainPageActivity.this.processResponse(response);
                    }
                });
            }
        }
    }, roundCallback = new SocketCallback<RoundUserData>() {
        @Override
        public void response(final RoundUserData response) {
            final Boolean globally = response.globally;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(globally == null) {
                        // round started
                        currentCategory = null;
                        waitingCategory();
                    } else if(globally) {
                        // round ended
                        init_round();
                    }
                }
            });
        }
    }, gameCallback = new SocketCallback<WinnerPartecipationsUserleft>() {
        @Override
        public void response(WinnerPartecipationsUserleft response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    roundDetails();
                }
            });
        }
    }, chosenCategoryCallback = new SocketCallback<SuccessCategory>() {
        @Override
        public void response(SuccessCategory response) {
            currentCategory = response.category;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playRound();
                }
            });
        }
    }, userJoinedLeftCallback = new SocketCallback<Success>() {
        @Override
        public void response(Success response) {
            GameMainPageActivity.this.init_round();
        }
    };

    @Override
    protected String getToolbarTitle(){ return (opponent == null)? title : opponent.toString(); }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main_page);
        //init
        startLoading();
        Intent intent = getIntent();
        if(intent.getBooleanExtra(getString(R.string.extra_boolean_game), false)) { createGame(); }
        else {
            setOpponentData();
            if(intent.getBooleanExtra(getString(R.string.extra_boolean_join_room), true)) { join_room(); }
            else {
                init_round();
                init_listening();
            }
        }
    }
    public void processResponse(SuccessInitRound response) {
        if (response.ended != null && response.ended) {
            this.redirect("round_details");
        } else if (response.waiting != null && response.waiting_for != null) {
            isWaiting = !(response.waiting_for.equals(currentUser));
            if (!isWaiting) {
                this.redirect(response.waiting);
            } else if (response.isOpponentOnline != null && !response.isOpponentOnline) {
                this.offline();
            } else {
                switch (response.waiting) {
                    case "category":
                        this.waitingCategory();
                        break;
                    case "game":
                        this.waitingRound();
                        break;
                }
            }
        }
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.stop_listening();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.join_room();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stop_listening();
    }

    public void redirect(String state) {
        switch (state) {
            case "category":
                this.chooseCategory();
                break;
            case "game":
                this.playRound();
                break;
            case "round_details":
                this.roundDetails();
                break;
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
            }
        });
    }

    private void startLoading() {
        CircleRotatingAnimation animation = new CircleRotatingAnimation(loadingCircle);
        animation.setDuration(7000);
        animation.setRepeatCount(Animation.INFINITE);
        loadingCircle.startAnimation(animation);
    }

    private void setOpponentData() {
        setToolbarTitle(opponent.toString());
        /*
        if(opponent != null) { actionBar.setTitle(opponent.toString()); }
        actionBar.setProfilePicture(TPUtils.getUserImageFromID(this, opponent.id));
        */
        /*TPUtils.picasso
                .load(TPUtils.getUserImageFromID(this, opponent.id))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into(profilePicture);*/
        TPUtils.injectUserImage(this, opponent, profilePicture);
    }

    private void join_room() {
        gameSocketManager.join(gameID, roomName, new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                if(response.success) {
                    init_listening();
                    init_round();
                }
            }
        });
    }
    private void init_round() {
        gameSocketManager.init_round(gameID, roundInitCallback);
    }

    private void init_listening() {
        listen(eventGameEvent, WinnerPartecipationsUserleft.class, gameCallback);
        listen(eventRoundEnded, RoundUserData.class, roundCallback);
        listen(eventCategoryChosen, SuccessCategory.class, chosenCategoryCallback);
        listen(eventUserJoined, Success.class, userJoinedLeftCallback);
        listen(eventUserLeft, Success.class, userJoinedLeftCallback);
    }
    private void stop_listening() {
        gameSocketManager.stopListen(eventGameEvent, eventRoundEnded, eventUserJoined, eventUserLeft, eventCategoryChosen);
    }

    private void updateWaitPage(String status, @ColorInt int overColor, @ColorInt int underColor) {
        if(visible) {
            gameHeader.setHeader(currentRound, currentCategory, isWaiting);
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

    private void roundDetails() {
        Intent intent = new Intent(GameMainPageActivity.this, RoundDetailsActivity.class);
        intent.putExtra(this.getString(R.string.extra_long_game), gameID);
        intent.putExtra(getString(R.string.extra_string_opponent), new Gson().toJson(opponent));
        startActivity(intent);
    }
}

package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;
import com.ted_developers.triviapatente.models.responses.SuccessInitRound;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
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
    // header
    @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    @BindView(R.id.gameHeaderSubtitle) TextView getGameHeaderSubtitle;
    // choose category page
    @BindView(R.id.chooseCategoryPage) RelativeLayout chooseCategoryPage;
    // wait page
    @BindView(R.id.waitPage) RelativeLayout waitPage;
    @BindView(R.id.bigProfilePicture) RoundedImageView profilePicture;
    @BindString(R.string.wait_page_offline_status) String offlineStatus;
    @BindString(R.string.wait_page_playing_status) String playingStatus;
    @BindString(R.string.wait_page_waitingCategory_status) String waitingCategory;
    @BindString(R.string.wait_page_waitingResponse_status) String waitingResponse;
    @BindView(R.id.status) TextView gameStatus;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;

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
                                        opponentImage = ContextCompat.getDrawable(GameMainPageActivity.this, R.drawable.no_image);
                                        chooseCategoryPage.setVisibility(View.GONE);
                                        toolbar.setProfilePicture(opponentImage);
                                        profilePicture.setImageDrawable(opponentImage);
                                        loadingView.setVisibility(View.GONE);
                                        // todo get title and subtitle
                                        gameHeaderTitle.setText("Game id");
                                        getGameHeaderSubtitle.setText(gameID.toString());
                                        loadingView.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                // todo communicate error on init round
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

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }
}

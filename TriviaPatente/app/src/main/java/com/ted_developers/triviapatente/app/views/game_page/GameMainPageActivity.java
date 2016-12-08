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

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class GameMainPageActivity extends AppCompatActivity {
    // opponent data
    Drawable opponentImage;
    String opponentIdentifier;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        //init
        Intent intent = getIntent();
        smallInit();
        if(intent.getBooleanExtra("new_game", false)) {
            Long userId = intent.getLongExtra("user_id", -1);
            Call<SuccessGameUser> call;
            if(userId != -1) {
                call = RetrofitManager.getHTTPGameEndpoint().newGame(userId);
            } else {
                call = RetrofitManager.getHTTPGameEndpoint().newRandomGame();
            }
            call.enqueue(new TPCallback<SuccessGameUser>() {
                @Override
                public void mOnResponse(Call<SuccessGameUser> call, Response<SuccessGameUser> response) {
                    if(response.code() == 200 && response.body().success) {
                        gameID = response.body().game.id;
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
        toolbar.setProfilePicture(opponentImage);
        profilePicture.setImageDrawable(opponentImage);
        toolbar.setBackButtonText(mainPageTitle);
        toolbar.setBackButtonOnClick(this, MainPageActivity.class);
    }

    private void fullInit() {
        // todo do stuff with gameID
        opponentImage = ContextCompat.getDrawable(this, R.drawable.no_image);
        initToolbar();
        // todo filter on socket
        chooseCategoryPage.setVisibility(View.GONE);
        initWaitPage();
        loadingView.setVisibility(View.GONE);
        // todo get title and subtitle
        gameHeaderTitle.setText("Game id");
        getGameHeaderSubtitle.setText(gameID.toString());
        loadingView.setVisibility(View.GONE);
    }

    private void initWaitPage() {
        profilePicture.setImageDrawable(opponentImage);
        // todo filter in base allo stato
        gameStatus.setText(waitingCategory);
    }

    private void initToolbar() {
        toolbar.setTitle(opponentIdentifier);
        toolbar.setProfilePicture(opponentImage);
    }
}

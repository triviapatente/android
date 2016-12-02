package com.ted_developers.triviapatente.app.views.main_page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.button.main.MainButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPRecentGamesFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal.RecentGameHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.output.MessageBox;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.HeartsPictureSettingsTPToolbar;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;
import com.ted_developers.triviapatente.app.views.game_page.NewGameActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.responses.SuccessGames;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {
    // top bar
    @BindView(R.id.toolbar) HeartsPictureSettingsTPToolbar toolbar;
    @BindString(R.string.main_page_title) String toolbarTitle;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // buttons name
    @BindString(R.string.button_new_game) String newGame;
    @BindString(R.string.button_rank) String rank;
    @BindString(R.string.button_stats) String stats;
    @BindString(R.string.button_shop) String shop;
    // options button
    @BindView(R.id.option_panel) LinearLayout optionPanel;
    @BindView(R.id.new_game) MainButton buttonNewGame;
    @BindView(R.id.stats) MainButton buttonShowStats;
    @BindView(R.id.shop) MainButton buttonShop;
    @BindView(R.id.rank) MainButton buttonShowRank;
    // buttons image
    @BindDrawable(R.drawable.chart_line) Drawable statsImage;
    @BindDrawable(R.drawable.trophy) Drawable rankImage;
    @BindDrawable(R.drawable.car) Drawable newGameImage;
    @BindDrawable(R.drawable.heart) Drawable shopImage;
    // hints
    @BindString(R.string.friends) String friendRankHint;
    @BindString(R.string.global) String globalRankHint;
    @BindString(R.string.multiple_invites) String multipleInvites;
    @BindString(R.string.single_invites) String singleInvites;
    // recent games
    @BindString(R.string.recent_games) String recentGamesTitle;
    TPExpandableList<Game> recentGames;
    @BindDimen(R.dimen.recent_game_height) int recentGameHeight;
    // server down
    @BindView(R.id.serverDownAlert) MessageBox serverDownAlert;
    @BindString(R.string.server_down_message) String serverDownMessage;

    private AuthSocketManager socketManager = new AuthSocketManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(MainPageActivity.this);
        recentGames = (TPExpandableList<Game>) getSupportFragmentManager().findFragmentById(R.id.recentGames);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        // hide other elements
        bulkVisibilitySetting(View.GONE);
        // init
        init();
    }

    private void init() {
        // connect to socket
        BaseSocketManager.connect(new SimpleCallback() {
            // on connect
            @Override
            public void execute() {
                socketManager.authenticate(new SocketCallback<Hints>() {
                    @Override
                    public void response(final Hints response) {
                        if (response.success) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // init items
                                    initOptionsButton();
                                    initOptionsHints(response);
                                    initToolbar();
                                    // load recent games
                                    loadRecentGames();
                                    // show items
                                    bulkVisibilitySetting(View.VISIBLE);
                                    // stop loading
                                    loadingView.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            backToFirstAccess();
                        }
                    }
                });
            }
        }, new SimpleCallback() {
            // on timeout
            @Override
            public void execute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serverDownAlert.showAlert(serverDownMessage);
                        serverDownAlert.setVisibility(View.VISIBLE);
                        // hide items (if triggered when items already displayed)
                        bulkVisibilitySetting(View.GONE);
                        // stop loading
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void initToolbar() {
        // set title
        toolbar.setTitle(toolbarTitle);
        // set profile picture
        // TODO get dinamically
        toolbar.setProfilePicture(getResources().getDrawable(R.drawable.no_image));
        // set hearts box
        // todo do bene
        toolbar.startTimer(this);
        // set menu
        toolbar.setMenu();
    }

    private void initOptionsButton() {
        // new game
        buttonNewGame.setButtonText(newGame, Color.WHITE);
        buttonNewGame.setImage(newGameImage);

        // rank
        buttonShowRank.setButtonText(rank, Color.WHITE);
        buttonShowRank.setImage(rankImage);

        // stats
        buttonShowStats.setButtonText(stats, Color.WHITE);
        buttonShowStats.setImage(statsImage);

        // shop
        buttonShop.setButtonText(shop, Color.WHITE);
        buttonShop.setImage(shopImage);
    }

    private void initOptionsHints(Hints hints) {
        List<String> hintsStrings = new ArrayList<String>();
        if(hints.stats.length > 0) {
            // build hints
            hintsStrings.clear();
            for(Category c : hints.stats) {
                hintsStrings.add(c.hint + ": " + (c.correct_answers/((c.total_answers == 0)?1:c.total_answers)) + "%");
            }
            // activity required to rotate hints
            buttonShowStats.setActivity(this);
            // set hints
            buttonShowStats.setHintText(listConverter(hintsStrings));
        }
        if(hints.friends_rank_position != null || hints.global_rank_position != null) {
            // build hints
            hintsStrings.clear();
            if(hints.friends_rank_position != null) {
                hintsStrings.add(friendRankHint + " " + hints.friends_rank_position);
            }
            if(hints.global_rank_position != null) {
                hintsStrings.add(globalRankHint + " " + hints.global_rank_position);
            }
            // activity required to rotate hints
            buttonShowRank.setActivity(this);
            // set hints
            buttonShowRank.setHintText(listConverter(hintsStrings));
        }
        // it is sad to say "no invites" :(
        if(hints.invites > 0) {
            // build hints
            String hint = hints.invites + " ";
            if(hints.invites == 1) {
                hint += singleInvites;
            } else {
                hint += multipleInvites;
            }
            // activity required to rotate hints
            buttonNewGame.setActivity(this);
            // set hints
            buttonNewGame.setHintText(new String[] { hint });
        }
    }

    private void loadRecentGames() {
        recentGames.setListTitle(recentGamesTitle);
        // request recent games
        Call<SuccessGames> call = RetrofitManager.getHTTPGameEndpoint().getRecentsGames();
        call.enqueue(new TPCallback<SuccessGames>() {
            @Override
            public void mOnResponse(Call<SuccessGames> call, Response<SuccessGames> response) {
                if(response.code() == 200 && response.body().success) {
                    int counter = 0;
                    if(response.body().recent_games != null) {
                        recentGames.setItems(response.body().recent_games, R.layout.recent_game, RecentGameHolder.class, TPRecentGamesFooter.class, recentGameHeight);
                        counter = response.body().recent_games.size();
                    }
                    recentGames.setListCounter(counter);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessGames> call, Throwable t) {}

            @Override
            public void then() {}
        });
    }

    private void bulkVisibilitySetting(int visibility) {
        toolbar.setVisibility(visibility);
        recentGames.getView().setVisibility(visibility);
        optionPanel.setVisibility(visibility);
    }

    private String[] listConverter(List<String> list) {
        return list.toArray(new String[] {});
    }

    private void backToFirstAccess() {
        // open first access page
        Intent myIntent = new Intent(this, FirstAccessActivity.class);
        startActivity(myIntent);
    }

    // touch handler
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(toolbar.getMenuVisibility() == View.VISIBLE) {
            toolbar.hideMenu();
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    // button clicks
    @OnTouch(R.id.new_game)
    public boolean newGameClick(MotionEvent event) {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
        return false;
    }
}

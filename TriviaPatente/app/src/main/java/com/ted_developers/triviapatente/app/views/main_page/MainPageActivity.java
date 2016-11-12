package com.ted_developers.triviapatente.app.views.main_page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.button.main.MainButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.RecentGameHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.HeartsPictureSettingsTPToolbar;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindDrawable;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    TPExpandableList<Game> recentGames;

    private AuthSocketManager socketManager = new AuthSocketManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new ProgressTask().execute();
    }

    private void init() {
        // connect to socket
        BaseSocketManager.connect(new SimpleCallback() {
            @Override
            public void execute() {
                socketManager.authenticate(new SocketCallback<Hints>() {
                    @Override
                    public void response(final Hints response) {
                        if(response.success) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initOptionsButton();
                                    initOptionsHints(response);
                                    initToolbar();
                                    loadRecentGames();
                                }
                            });
                        } else {
                            backToFirstAccess();
                        }
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
        toolbar.setProfilePicture(getResources().getDrawable(R.drawable.antonioterpin));
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
                hintsStrings.add(c.hint + ": " + c.percentage + "%");
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
        recentGames = (TPExpandableList<Game>) getSupportFragmentManager().findFragmentById(R.id.recentGames);
        // todo request
        List<Game> gameList = new ArrayList<Game>();
        gameList.add(new Game());
        gameList.add(new Game());
        gameList.add(new Game());
        Game g = gameList.get(0);
        g.ended = true;
        g.opponent_name = "Luigi";
        gameList.set(0, g);
        g = gameList.get(1);
        g.ended = false;
        g.opponent_name = "Ciul";
        g.my_turn = true;
        gameList.set(1, g);
        g = gameList.get(2);
        g.ended = false;
        g.my_turn = false;
        g.opponent_name = "Anto";
        gameList.set(2, g);
        recentGames.setGames(gameList, R.layout.recent_game, RecentGameHolder.class);
    }


    private String[] listConverter(List<String> list) {
        return list.toArray(new String[] {});
    }

    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
            loadingView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            MainPageActivity.this.init();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loadingView.setVisibility(View.GONE);
        }
    }

    private void backToFirstAccess() {
        // open first access page
        Intent myIntent = new Intent(this, FirstAccessActivity.class);
        startActivity(myIntent);
    }
}

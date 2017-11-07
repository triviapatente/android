package com.ted_developers.triviapatente.app.views.main_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.ReceivedData;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.MainButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPEmoticonFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.RecentGameHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.output.MessageBox;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.find_opponent.NewGameActivity;
import com.ted_developers.triviapatente.app.views.rank.RankActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.EventAction;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.responses.ActionRecentGame;
import com.ted_developers.triviapatente.models.responses.SuccessGames;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class MainPageActivity extends TPActivity implements View.OnClickListener{
    @BindString(R.string.main_page_title) String pageTitle;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // buttons name
    @BindString(R.string.button_rank) String rank;
    @BindString(R.string.button_stats) String stats;
    @BindString(R.string.button_shop) String shop;
    // options button
    @BindView(R.id.option_panel) LinearLayout optionPanel;
    @BindView(R.id.new_game) MainButton buttonNewGame;
    @BindView(R.id.stats) MainButton buttonShowStats;
    @BindView(R.id.shop) MainButton buttonShop;
    @BindView(R.id.rank) MainButton buttonShowRank;
    // hints
    @BindString(R.string.friends) String friendRankHint;
    @BindString(R.string.friendsNotAvailable) String friendRankHintNotAvailable;
    @BindString(R.string.global) String globalRankHint;
    // recent games
    @BindString(R.string.recent_games) String recentGamesTitle;
    @BindString(R.string.no_games) String recentGamesAlternativeTitle;
    @BindView(R.id.syncProgress) ProgressBar syncProgress;
    TPExpandableList<Game> recentGames;
    @BindDimen(R.dimen.recent_game_height) int recentGameHeight;
    // connection error
    @BindView(R.id.serverDownAlert) MessageBox serverDownAlert;
    @BindString(R.string.server_down_message) String serverDownMessage;
    @BindView(R.id.retryConnectionButton) ImageButton retryConnectionButton;
    // recent game event

    private boolean onCreate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        recentGames = (TPExpandableList<Game>) getSupportFragmentManager().findFragmentById(R.id.recentGames);
        recentGames.setSyncButtonOnClickListner(this);
        baseSocketManager.listen(getString(R.string.socket_event_recent_game), ActionRecentGame.class, new SocketCallback<ActionRecentGame>() {
            @Override
            public void response(ActionRecentGame response) {
                if(visible) {
                    loadRecentGames();
                }
            }
        });
    }

    @Override
    protected String getToolbarTitle(){
        return pageTitle;
    }

    private void init() { init(null); }
    private void init(final View syncButton) {
        // connect to socket
        if(!BaseSocketManager.isConnected()) {
            // start loading
            loadingView.setVisibility(View.VISIBLE);
            BaseSocketManager.connect(new SimpleCallback() {
                // on connect
                @Override
                public void execute() {
                    authSocketManager.global_infos(new SocketCallback<Hints>() {
                        @Override
                        public void response(final Hints response) {
                            if (response.success) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // init items
                                        ReceivedData.statsHints = response.stats;
                                        ReceivedData.friends_rank_position = response.friends_rank_position;
                                        ReceivedData.global_rank_position = response.global_rank_position;
                                        initOptionButtons();
                                        // load recent games
                                        loadRecentGames(syncButton);
                                        // show popup if desired and last shown was yesterday
                                        if(currentUser.showPopup &&
                                                System.currentTimeMillis() - currentUser.lastPopupShowedDateMillis > 24 * 60 * 60 * 1000) {
                                            showHeartPopup(true);
                                            currentUser.lastPopupShowedDateMillis = System.currentTimeMillis();
                                            SharedTPPreferences.saveUser(currentUser);
                                        }
                                    }
                                });
                            } else { backToFirstAccess(); }
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
                            errorConnectingToServer();
                        }
                    });
                }
            });
        } else loadRecentGames(syncButton);
    }

    private void initOptionButtons() {
        // activity required to rotate hints
        initStatsHints();
        initRankHints();
    }

    private void initStatsHints() {
        List<String> hintsStrings = new ArrayList<String>();
        if(ReceivedData.statsHints != null && ReceivedData.statsHints.size() > 0) {
            // build hints
            hintsStrings.clear();
            for(Category c : ReceivedData.statsHints) {
                hintsStrings.add(c.hint + ": " + (c.correct_answers/((c.total_answers == 0)?1:c.total_answers)) + "%");
            }
            // set hints
            buttonShowStats.setHintText(listConverter(hintsStrings));
        }
    }

    private void initRankHints() {
        List<String> hintsStrings = new ArrayList<String>();
        // build hints
        hintsStrings.clear();
        hintsStrings.add(globalRankHint + " " + ReceivedData.global_rank_position);
        if(ReceivedData.friends_rank_position != null) {
            hintsStrings.add(friendRankHint + " " + ReceivedData.friends_rank_position);
        } else {
            hintsStrings.add(friendRankHintNotAvailable);
        }
        // set hints
        buttonShowRank.setHintText(listConverter(hintsStrings));
    }

    private void loadRecentGames() { loadRecentGames(null); }
    private void loadRecentGames(final View syncButton) {
        if(syncButton != null) {
            syncButton.setVisibility(View.GONE);
            syncProgress.setVisibility(View.VISIBLE);
        }
        recentGames.setTitles(recentGamesTitle, recentGamesAlternativeTitle);
        // request recent games
        Call<SuccessGames> call = RetrofitManager.getHTTPGameEndpoint().getRecentsGames();
        call.enqueue(new TPCallback<SuccessGames>() {
            @Override
            public void mOnResponse(Call<SuccessGames> call, Response<SuccessGames> response) {
                if(response.code() == 200 && response.body().success) {
                    if(response.body().recent_games != null) {
                        ReceivedData.recentGames = response.body().recent_games;
                        ReceivedData.orderGames();
                    }
                    updateRecentGames();
                    // stop loading
                    loadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessGames> call, Throwable t) {
            }

            @Override
            public void then() {
                if(syncButton != null) {
                    syncProgress.setVisibility(View.GONE);
                    syncButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateRecentGames() {
        recentGames.setItems(ReceivedData.recentGames,
                R.layout.list_element_recent_game_holder, RecentGameHolder.class,
                R.layout.list_element_recent_game_footer, TPEmoticonFooter.class,
                recentGameHeight);
        recentGames.setListCounter(ReceivedData.recentGames.size());
    }

    private void errorConnectingToServer() {
        if(toolbar != null) toolbar.setVisibility(View.GONE);
        serverDownAlert.showAlert(serverDownMessage);
        serverDownAlert.setVisibility(View.VISIBLE);
        // hide items (if triggered when items already displayed)
        recentGames.getView().setVisibility(View.GONE);
        optionPanel.setVisibility(View.GONE);
        // stop loading
        loadingView.setVisibility(View.GONE);
        retryConnectionButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.retryConnectionButton)
    public void retryConnection() {
        // reset ui items and launch init again
        if(toolbar != null) toolbar.setVisibility(View.VISIBLE);
        serverDownAlert.hideAlert();
        serverDownAlert.setVisibility(View.GONE);
        recentGames.getView().setVisibility(View.VISIBLE);
        optionPanel.setVisibility(View.VISIBLE);
        retryConnectionButton.setVisibility(View.GONE);
        init();
    }

    private String[] listConverter(List<String> list) {
        return list.toArray(new String[] {});
    }

    // main button clicks
    @OnClick(R.id.new_game)
    public void newGameClick() {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.shop)
    public void shopClick() {}
    @OnClick(R.id.stats)
    public void statsClick() {}
    @OnClick(R.id.rank)
    public void rankClick() {
        Intent i = new Intent(this, RankActivity.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        recentGames.setMinimizedHeightMode();

        // check web socket connection
        init();

        // update hints
        initOptionButtons();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.syncRecentGames) {
            loadRecentGames(v);
        }
    }
}


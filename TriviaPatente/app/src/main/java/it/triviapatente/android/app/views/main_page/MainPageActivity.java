package it.triviapatente.android.app.views.main_page;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.ReceivedData;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.buttons.MainButton;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.TPCallback;
import it.triviapatente.android.app.utils.custom_classes.dialogs.TPNewTermsPolicyDialog;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.footer.TPEmoticonFooter;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.RecentGameHolder;
import it.triviapatente.android.app.utils.custom_classes.output.MessageBox;
import it.triviapatente.android.app.utils.custom_classes.listViews.expandable_list.TPExpandableList;
import it.triviapatente.android.app.views.find_opponent.NewGameActivity;
import it.triviapatente.android.app.views.game_page.GameMainPageActivity;
import it.triviapatente.android.app.views.rank.RankActivity;
import it.triviapatente.android.firebase.FirebaseInstanceIDService;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.Hints;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Game;
import it.triviapatente.android.models.responses.ActionRecentGame;
import it.triviapatente.android.models.responses.SuccessGames;
import it.triviapatente.android.socket.modules.base.BaseSocketManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class MainPageActivity extends TPActivity implements View.OnClickListener{

    public static boolean launched = true; // it is the first time this activity has been resumed

    @BindString(R.string.main_page_title) String pageTitle;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // buttons name
    @BindString(R.string.button_rank) String rank;
    @BindString(R.string.button_stats) String stats;
    @BindString(R.string.button_shop) String shop;
    // options button
    @BindView(R.id.option_panel) LinearLayout optionPanel;
    @BindView(R.id.new_game)
    MainButton buttonNewGame;
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
    @BindView(R.id.serverDownAlert)
    MessageBox serverDownAlert;
    @BindString(R.string.server_down_message) String serverDownMessage;
    @BindView(R.id.retryConnectionButton) ImageButton retryConnectionButton;

    private boolean onCreate = true;

    private User pushUser;
    private Game pushGame;
    private void getPushValues() {

        String pushUserData = getIntent().getStringExtra(getString(R.string.extra_firebase_opponent_param));
        String pushGameData = getIntent().getStringExtra(getString(R.string.extra_firebase_game_param));
        if (pushUserData != null)
            pushUser = RetrofitManager.gson.fromJson(pushUserData, User.class);
        if (pushGameData != null)
            pushGame = RetrofitManager.gson.fromJson(pushGameData, Game.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        serverDownMessage = TPUtils.translateEmoticons(serverDownMessage);
        recentGames = (TPExpandableList<Game>) getSupportFragmentManager().findFragmentById(R.id.recentGames);
        recentGames.setSyncButtonOnClickListner(this);
        getPushValues();
        baseSocketManager.listen(getString(R.string.socket_event_recent_game), ActionRecentGame.class, new SocketCallback<ActionRecentGame>() {
            @Override
            public void response(ActionRecentGame response) {
                if(visible) {
                    loadRecentGames();
                }
            }
        });
    }

    private void pushRedirect() {
        Intent i = new Intent(this, GameMainPageActivity.class);
        i.putExtra(this.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(pushUser));
        i.putExtra(this.getString(R.string.extra_long_game), pushGame.id);

        startActivity(i);
    }

    @Override
    protected String getToolbarTitle(){
        return pageTitle;
    }

    private void init() { init(null); }
    private void init(final View syncButton) {
        FirebaseInstanceIDService.sendRegistration();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
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
                                        // ReceivedData.statsHints = response.stats;
                                        // ReceivedData.friends_rank_position = response.friends_rank_position;
                                        ReceivedData.global_rank_position = response.global_rank_position;
                                        if (pushGame != null && pushUser != null) {
                                            pushRedirect();
                                        } else {
                                            // new terms or policy?
                                            boolean newTerms = false, newPolicy = false;
                                            if (currentUser.privacyPolicyLastUpdate == null)
                                                currentUser.privacyPolicyLastUpdate = response.privacy_policy_last_update;
                                            else
                                                newPolicy = !currentUser.privacyPolicyLastUpdate.equals(response.privacy_policy_last_update);
                                            if (currentUser.termsAndConditionsLastUpdate == null)
                                                currentUser.termsAndConditionsLastUpdate = response.terms_and_conditions_last_update;
                                            else
                                                newTerms = !currentUser.termsAndConditionsLastUpdate.equals(response.terms_and_conditions_last_update);
                                            if (newTerms || newPolicy)
                                                new TPNewTermsPolicyDialog(
                                                        MainPageActivity.this,
                                                        newTerms,
                                                        newPolicy,
                                                        response.terms_and_conditions_last_update,
                                                        response.privacy_policy_last_update
                                                ).show();
                                        }
                                        initOptionButtons();
                                        // load recent games
                                        loadRecentGames(syncButton);

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
        } else {
            if (pushGame != null && pushUser != null) {
                pushRedirect();
            }
            loadRecentGames(syncButton);
        }
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
        //Facebook link code
        /*if(ReceivedData.friends_rank_position != null) {
            hintsStrings.add(friendRankHint + " " + ReceivedData.friends_rank_position);
        } else {
            hintsStrings.add(friendRankHintNotAvailable);
        }*/
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
                        //ReceivedData.orderGames();
                    }
                    updateRecentGames();
                    // stop loading
                    loadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessGames> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                        .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadRecentGames(syncButton);
                            }
                        })
                        .show();
            }

            @Override
            public void then() {
                if(syncButton != null) {
                    syncProgress.setVisibility(View.GONE);
                    syncButton.setVisibility(View.VISIBLE);
                } else {
                    // show popup if desired and last shown was yesterday
                    if(launched) showAutomaticPopup();
                    launched = false; // it is not the first time this activity has been resumed
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
        if(loadingView.getVisibility() == View.GONE && mDrawerLayout != null) {
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawers();
            else mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.syncRecentGames) {
            loadRecentGames(v);
        }
    }
}


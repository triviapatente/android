package com.ted_developers.triviapatente.app.views.main_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.ReceivedData;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.MainButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPEmoticonFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.RecentGameHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.output.MessageBox;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.HeartsPictureSettingsTPActionBar;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;
import com.ted_developers.triviapatente.app.views.game_page.NewGameActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.responses.InviteUser;
import com.ted_developers.triviapatente.models.responses.SuccessGames;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Response;

public class MainPageActivity extends TPActivity implements Button.OnClickListener{
    // content
    @BindView(R.id.content) RelativeLayout mainPageContent;
    @BindView(R.id.mainPageContainer) RelativeLayout mainPageContainer;
    // top bar
    @BindView(R.id.toolbar) HeartsPictureSettingsTPActionBar toolbar;
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
    @BindString(R.string.global) String globalRankHint;
    @BindString(R.string.multiple_invites) String multipleInvites;
    @BindString(R.string.single_invites) String singleInvites;
    // recent games
    @BindString(R.string.recent_games) String recentGamesTitle;
    @BindString(R.string.no_games) String recentGamesAlternativeTitle;
    TPExpandableList<Game> recentGames;
    @BindDimen(R.dimen.recent_game_height) int recentGameHeight;
    // server down
    @BindView(R.id.serverDownAlert) MessageBox serverDownAlert;
    @BindString(R.string.server_down_message) String serverDownMessage;
    private AuthSocketManager socketManager = new AuthSocketManager();
    // menu options
    @BindView(R.id.modal_logout) LinearLayout modalLogout;
    @BindView(R.id.fullBlurredView) ImageView blurredBackgroundView;
    @BindView(R.id.fullBlurredContainer) RelativeLayout blurredBackgroundContainer;
    private boolean isBlurred = false;
    // sockets
    @BindString(R.string.socket_event_invite_created) String eventInviteCreated;
    SocketCallback<InviteUser> inviteCreatedCallback = new SocketCallback<InviteUser>() {
        @Override
        public void response(InviteUser response) {
            if(visible) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initInviteHints();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        recentGames = (TPExpandableList<Game>) getSupportFragmentManager().findFragmentById(R.id.recentGames);
        // listening to events
        listen(eventInviteCreated, InviteUser.class, inviteCreatedCallback);
    }

    private void init() {
        // connect to socket
        if(!BaseSocketManager.isConnected()) {
            // start loading
            loadingView.setVisibility(View.VISIBLE);
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
                                        ReceivedData.statsHints = response.stats;
                                        ReceivedData.friends_rank_position = response.friends_rank_position;
                                        ReceivedData.global_rank_position = response.global_rank_position;
                                        ReceivedData.numberOfInvites = response.invites;
                                        initOptionButtons();
                                        initToolbar();
                                        // load recent games
                                        loadRecentGames();
                                        // show all
                                        mainPageContent.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                // todo avvisare login richiesto
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
                            errorConnectingToServer();
                        }
                    });
                }
            });
        }
    }

    private void initToolbar() {
        // set profile picture
        // TODO get dinamically
        toolbar.setProfilePicture(getResources().getDrawable(R.drawable.image_no_profile_picture));
        // todo set hearts box
        toolbar.setHeartImage();
        toolbar.setLifeCounter(3);
        // set menu
        toolbar.setMenu();
        // set menu options click listeners
        setMenuOptionsOnClickListener();
    }

    private void initOptionButtons() {
        // activity required to rotate hints
        buttonShowStats.setActivity(this);
        initStatsHints();
        buttonShowRank.setActivity(this);
        initRankHints();
        buttonNewGame.setActivity(this);
        initInviteHints();
    }

    private void initStatsHints() {
        List<String> hintsStrings = new ArrayList<String>();
        if(ReceivedData.statsHints.length > 0) {
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
        if(ReceivedData.friends_rank_position != null || ReceivedData.global_rank_position != null) {
            // build hints
            hintsStrings.clear();
            if(ReceivedData.friends_rank_position != null) {
                hintsStrings.add(friendRankHint + " " + ReceivedData.friends_rank_position);
            }
            if(ReceivedData.global_rank_position != null) {
                hintsStrings.add(globalRankHint + " " + ReceivedData.global_rank_position);
            }
            // set hints
            buttonShowRank.setHintText(listConverter(hintsStrings));
        }
    }

    private void initInviteHints() {
        // it is sad to say "no invites" :(
        Integer numberOfInvites = (ReceivedData.pendingInvites == null)? ReceivedData.numberOfInvites : ReceivedData.pendingInvites.size();
        if(numberOfInvites != null && numberOfInvites > 0) {
            // build hints
            String hint = numberOfInvites + " ";
            if(numberOfInvites == 1) {
                hint += singleInvites;
            } else {
                hint += multipleInvites;
            }
            // set hints
            if(!hint.equals(buttonNewGame.currentText)) {
                buttonNewGame.setHintText(new String[] { hint });
            }
        } else {
            buttonNewGame.setHintText(new String[] {""});
        }
    }

    private void loadRecentGames() {
        recentGames.setTitles(recentGamesTitle, recentGamesAlternativeTitle);
        // request recent games
        Call<SuccessGames> call = RetrofitManager.getHTTPGameEndpoint().getRecentsGames();
        call.enqueue(new TPCallback<SuccessGames>() {
            @Override
            public void mOnResponse(Call<SuccessGames> call, Response<SuccessGames> response) {
                if(response.code() == 200 && response.body().success) {
                    int counter = 0;
                    if(response.body().recent_games != null) {
                        ReceivedData.recentGames = response.body().recent_games;
                        recentGames.setItems(ReceivedData.recentGames,
                                R.layout.list_element_recent_game_holder, RecentGameHolder.class,
                                R.layout.list_element_recent_game_footer, TPEmoticonFooter.class,
                                recentGameHeight);
                        counter = response.body().recent_games.size();
                    }
                    recentGames.setListCounter(counter);
                    // stop loading
                    loadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessGames> call, Throwable t) {
            }

            @Override
            public void then() {}
        });
    }

    private void errorConnectingToServer() {
        serverDownAlert.showAlert(serverDownMessage);
        serverDownAlert.setVisibility(View.VISIBLE);
        // hide items (if triggered when items already displayed)
        bulkVisibilitySetting(View.GONE);
        // stop loading
        loadingView.setVisibility(View.GONE);
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
        finish();
    }

    // touch handler
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isBlurred) {
            return true;
        }
        if(MotionEvent.ACTION_UP == ev.getAction()
                && toolbar != null && toolbar.getMenuVisibility() == View.VISIBLE
                && !TPUtils.isPointInsideView((int) ev.getX(), (int) ev.getY(), toolbar.menu)) {
            toolbar.hideMenu();
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    // button clicks
    @OnClick(R.id.new_game)
    public void newGameClick() {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.shop)
    public void shopClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @OnClick(R.id.stats)
    public void statsClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @OnClick(R.id.rank)
    public void rankClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // init
        init();

        initInviteHints();
        initRankHints();
    }

    @Override
    public void onBackPressed() {
        // todo implement exit page
    }

    public void setMenuOptionsOnClickListener() {
        toolbar.menuLogoutOptionButton.setOnClickListener(this);
    }

    // menu buttons onclick
    public void logout() {
        // setting blurry background
        toolbar.hideMenu(new SimpleCallback() {
            @Override
            public void execute() {
                Blurry.with(MainPageActivity.this).sampling(3).radius(13).capture(mainPageContainer).into(blurredBackgroundView);
                blurredBackgroundContainer.setVisibility(View.VISIBLE);
                isBlurred = true;
                // showing modal
                modalLogout.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuLogoutOption: logout(); break;
            default: return;
        }
    }
}

package it.triviapatente.android.app.views.find_opponent;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import it.triviapatente.android.R;

import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.dialogs.TPDialog;
import it.triviapatente.android.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import it.triviapatente.android.app.utils.custom_classes.callbacks.TPCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.footer.TPTellAFriendFooter;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.ProposedOpponentHolder;
import it.triviapatente.android.app.views.AlphaView;
import it.triviapatente.android.app.views.game_page.GameMainPageActivity;
import it.triviapatente.android.app.views.rank.RankActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.responses.SuccessUsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Response;

public class FindOpponentActivity extends RankActivity {
    @BindString(R.string.find_opponent_title) String title;
    // all or friends
    @BindView(R.id.all_or_friends) LinearLayout allOrFriendsBlock;
    @BindView(R.id.all_button) Button allButton;
    @BindView(R.id.dummy_layout) LinearLayout dummyLayout;
    @BindView(R.id.friends_button) Button friendsButton;
    @BindDrawable(R.drawable.button_all_not_selected) Drawable allButtonNotSelected;
    @BindDrawable(R.drawable.button_all_selected) Drawable allButtonSelected;
    @BindDrawable(R.drawable.button_friends_not_selected) Drawable friendsButtonNotSelected;
    @BindDrawable(R.drawable.button_friends_selected) Drawable friendsButtonSelected;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @BindColor(android.R.color.white) @ColorInt int whiteColor;
    // search
    @BindView(R.id.search_bar) EditText searchBar;
    // players
    @BindView(R.id.playerList) RecyclerView playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    @BindView(R.id.no_users) TextView noUsersAlert;
    List<User> suggestedUsers;
    // friends not shown
    // TODO no more needed
    List<User> fakeUsers = Arrays.asList(
            new User(-1l, "TriviaPatente", null, true, 8000),
            new User(-1l, "UnGioco", null, true, 7689),
            new User(-1l, "Davvero", null, false, 6578),
            new User(-1l, "Davvero", null, true, 6000),
            new User(-1l, "Davvero", null, false, 5789),
            new User(-1l, "Fantastico", null, false, 5788),
            new User(-1l, "Probabilmente", null, true, 5748),
            new User(-1l, "IlMigliore", null, true, 3499),
            new User(-1l, "InAssoluto", null, true, 3000),
            new User(-1l, "LoAdoro", null, false, 2999)
    );
    // modal
    @BindView(R.id.playersListBlock) RelativeLayout playersListBlock;
    private boolean firstTime = true;
    private TPDialog facebookDialog;

    @Override
    protected String getToolbarTitle(){ return title; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_opponent);
        // init action_bar
        if(getIntent().getBooleanExtra("random", false)) {
            searchRandomOpponent();
        } else {
            // init
            init(false);
        }
    }
    @Override
    protected void initRefreshLayout() {
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        disableRefreshLayout(); //disable touch
    }

    @Override
    protected void initSearchBar() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    FindOpponentActivity.this.doSearch(searchBar.getText().toString());
                    TPUtils.hideKeyboard(FindOpponentActivity.this, dummyLayout);
                }
                return false;
            }
        });
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")) {
                    loadPlayers();
                }
            }
        });
    }

    @Override
    protected void doSearch(final String username) {
        if(all) {
            if(username.equals("")) loadPlayers();
            else {
                startLoading();
                searchCall = RetrofitManager.getHTTPGameEndpoint().getSearchResult(username);
                searchCall.enqueue(new TPCallback<SuccessUsers>() {
                    @Override
                    public void mOnResponse(Call<SuccessUsers> call, Response<SuccessUsers> response) {
                        if(response.code() == 200) {
                            if(response.body().users.size() == 0) {
                                setPlayersListItems(new ArrayList<User>());
                                playersList.setVisibility(View.GONE);
                                noUsersAlert.setVisibility(View.VISIBLE);
                            } else {
                                setPlayersListItems(response.body().users);
                                playersList.setVisibility(View.VISIBLE);
                                noUsersAlert.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void mOnFailure(Call<SuccessUsers> call, Throwable t) {
                        Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                                .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callSearch(username);
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void then() {
                        stopLoading();
                    }
                });
            }
        } else {
            // todo search on friends
        }
    }

    private void initDialog() {
        facebookDialog = new TPDialog(this, R.layout.modal_view_connect_to_facebook, 0.4f, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                allButtonClick();
            }
        }) {
            @Override
            public void onNegativeButtonClick() {
                allButtonClick();
                this.hide();
            }

            @Override
            public void onPositiveButtonClick() {
                // todo connect to facebook
                Intent intent = new Intent(FindOpponentActivity.this, AlphaView.class);
                FindOpponentActivity.this.startActivity(intent);
            }
        };
        WindowManager.LayoutParams params = facebookDialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP;
        params.y = /*actionBar.getMeasuredHeight()*/ + allOrFriendsBlock.getMeasuredHeight();
    }

    @Override
    protected void setPlayersListItems(List<User> userList) {
        playersList.setAdapter(new TPListAdapter<>(this, userList, R.layout.list_element_proposed_opponent_holder, ProposedOpponentHolder.class, R.layout.list_element_tell_a_friend_footer, TPTellAFriendFooter.class, playerListItemHeight, playersList));
    }

    @Override
    protected void loadPlayers() {
        startLoading();
        Call<SuccessUsers> call = RetrofitManager.getHTTPGameEndpoint().getSuggestedUsers();
        call.enqueue(new TPCallback<SuccessUsers>() {
            @Override
            public void mOnResponse(Call<SuccessUsers> call, Response<SuccessUsers> response) {
                if(response.code() == 200 && response.body().success && response.body().users != null) {
                    suggestedUsers = response.body().users;
                    setPlayersListItems(suggestedUsers);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessUsers> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                        .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadPlayers();
                            }
                        })
                        .show();
            }

            @Override
            public void then() {
                // show other items
                playersList.setVisibility(View.VISIBLE);
                // stop loading
                stopLoading();
            }
        });
    }

    @OnClick(R.id.all_button)
    public void allButtonClick() {
        all = true;
        searchBar.setText("");
        allButton.setBackground(allButtonSelected);
        friendsButton.setBackground(friendsButtonNotSelected);
        allButton.setTextColor(whiteColor);
        friendsButton.setTextColor(mainColor);
        loadPlayers();
    }

    @OnClick(R.id.friends_button)
    public void friendsButtonClick() {
        all = false;
        searchBar.setText("");
        startLoading();
        friendsButton.setBackground(friendsButtonSelected);
        allButton.setBackground(allButtonNotSelected);
        allButton.setTextColor(mainColor);
        friendsButton.setTextColor(whiteColor);
        // todo check if connected
        if (false) {
            // todo load friends
        } else {
            setPlayersListItems(fakeUsers);
            if(firstTime) {
                initDialog();
                firstTime = false;
            }
            stopLoading();

            /*playersList.post(new Runnable() {
                @Override
                public void run() {
                    Blurry.with(FindOpponentActivity.this)
                            .radius(10)
                            .sampling(1)
                            .capture(playersList)
                            .into(blurImageView);
                }
            });
            blurImageView.setVisibility(View.VISIBLE);*/
            facebookDialog.show();
        }
    }

    // random opponent
    private void searchRandomOpponent() {
        Intent intent = new Intent(FindOpponentActivity.this, GameMainPageActivity.class);
        intent.putExtra(getResources().getString(R.string.extra_boolean_game), true);
        FindOpponentActivity.this.startActivity(intent);
    }
}

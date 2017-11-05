package com.ted_developers.triviapatente.app.views.rank;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.DividerItemDecoration;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPTellAFriendFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.PlayerRankHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.ProposedOpponentHolder;
import com.ted_developers.triviapatente.app.views.find_opponent.FindOpponentActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.Matches;
import com.ted_developers.triviapatente.models.responses.RankPosition;
import com.ted_developers.triviapatente.models.responses.SuccessUsers;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Response;

public class RankActivity extends TPActivity {

    @BindString(R.string.activity_rank_title) String title;
    @BindView(R.id.search_bar) EditText searchBar;
    private boolean all = true;
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    @BindView(R.id.playerList) RecyclerView playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    @BindView(R.id.no_users) TextView noUsersAlert;
    List<User> users;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @BindView(R.id.rank_scroll) ImageButton rankScroll;
    private boolean scrollToTop = true;

    @BindString(R.string.direction_down) String down;
    @BindString(R.string.direction_up) String up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        init();
    }

    protected void init() {
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        initSearchBar();
        initPlayerList();
        loadPlayers();
        hideKeyboard();
    }

    protected void setPlayersListItems(List<User> userList) {
        playersList.setAdapter(
                new TPListAdapter<>(
                        this,
                        userList,
                        R.layout.list_element_player_rank_holder,
                        PlayerRankHolder.class,
                        R.layout.list_element_tell_a_friend_footer,
                        TPTellAFriendFooter.class,
                        playerListItemHeight,
                        playersList)
        );
    }

    protected void loadPlayers() { loadPlayers(null, null); }
    protected void loadPlayers(Integer thresold, String direction) {
        if(users == null) {
            loadingView.setVisibility(View.VISIBLE);
            Call<RankPosition> call = RetrofitManager.getHTTPRankEndpoint().getUsers(thresold, direction);
            call.enqueue(new TPCallback<RankPosition>() {
                @Override
                public void mOnResponse(Call<RankPosition> call, Response<RankPosition> response) {
                    if(response.code() == 200 && response.body().rank != null) {
                        users = response.body().rank;
                        setPlayersListItems(users);
                    }
                    // show other items
                    playersList.setVisibility(View.VISIBLE);
                    // stop loading
                    loadingView.setVisibility(View.GONE);
                }

                @Override
                public void mOnFailure(Call<RankPosition> call, Throwable t) {}

                @Override
                public void then() {}
            });
        } else {
            setPlayersListItems(users);
        }
    }

    protected void initPlayerList() {
        playersList.setLayoutManager(new LinearLayoutManager(this));
        playersList.setOnTouchListener(new OnSwipeTouchListener(this));
        playersList.addItemDecoration(new DividerItemDecoration(mainColor, playersList.getWidth()));
    }

    protected void initSearchBar() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    RankActivity.this.doSearch(searchBar.getText().toString());
                    hideKeyboard();
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
        hideKeyboard();
    }

    // TODO unificando qua e in find opponent si può togliere l'override di la (per @donadev)
    protected void doSearch(String username) {
        if(all) {
            if(username.equals("")) loadPlayers();
            else {
                loadingView.setVisibility(View.VISIBLE);
                Call<Matches> call = RetrofitManager.getHTTPRankEndpoint().getSearchResult(username);
                call.enqueue(new TPCallback<Matches>() {
                    @Override
                    public void mOnResponse(Call<Matches> call, Response<Matches> response) {
                        if(response.code() == 200) {
                            if(response.body().matches.size() == 0) {
                                noUsersAlert.setVisibility(View.VISIBLE);
                            } else {
                                setPlayersListItems(response.body().matches);
                                noUsersAlert.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void mOnFailure(Call<Matches> call, Throwable t) {}

                    @Override
                    public void then() {
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            // todo search on friends
        }
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Optional
    @OnClick(R.id.rank_scroll)
    public void rankScrollClick() {
        if(scrollToTop) {
            rankScroll.setBackground(ContextCompat.getDrawable(this, R.drawable.rank_scroll_down_button));
            loadPlayers(1, down);
        } else {
            rankScroll.setBackground(ContextCompat.getDrawable(this, R.drawable.rank_scroll_up_button));
            loadPlayers(null, null);
        }
        scrollToTop = !scrollToTop;
    }

    @OnClick(R.id.x_button)
    public void xButtonClick() {
        searchBar.setText("");
    }

    @Override
    protected String getToolbarTitle(){
        return title;
    }
    @Override
    protected int getSettingsVisibility(){
        return View.GONE;
    }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }
}

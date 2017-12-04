package com.ted_developers.triviapatente.app.views.rank;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.DividerItemDecoration;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPTellAFriendFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.PlayerRankHolder;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.RankPosition;
import com.ted_developers.triviapatente.models.responses.SuccessUsers;

import java.util.ArrayList;
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
    protected boolean all = true;
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    @BindView(R.id.playerList) RecyclerView playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    @BindView(R.id.no_users) TextView noUsersAlert;
    @BindString(R.string.no_user_found) String noUsersAlertString;
    List<User> users;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @Nullable @BindView(R.id.rank_scroll) ImageButton rankScroll;
    private boolean scrollToTop = true;

    @BindString(R.string.direction_down) String down;
    @BindString(R.string.direction_up) String up;

    private boolean absolute_first = false, absolute_last = false, loadable = true;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        init();
    }

    protected void init() {
        // update unicode strings
        noUsersAlertString = TPUtils.translateEmoticons(noUsersAlertString);
        noUsersAlert.setText(noUsersAlertString);
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        initSearchBar();
        initPlayerList();
        loadPlayers();
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
        addPagination();
    }

    private void addPagination() {
        mLayoutManager = new LinearLayoutManager(this);
        playersList.setLayoutManager(mLayoutManager);
        playersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(loadable) {
                    if(mLayoutManager.findFirstVisibleItemPosition() == 0 && !absolute_first) {
                        loadable = false;
                        // scroll down on first item which is not the absolute first
                        // once per instance this is not true, the first time the first may be the absolute first
                        addPlayers(users.get(0).position, down, true);
                    } else if(mLayoutManager.findLastVisibleItemPosition() == users.size() - 1 && !absolute_last) {
                        loadable = false;
                        // scroll up on last item which is not the absolute last
                        // once per instance this is not true, the first time the last may be the absolute last
                        addPlayers(users.get(users.size() - 1).position, up, false);
                    }
                }
            }
        });
    }

    private void addPlayers(Integer thresold, String direction, final boolean first) {
        // TODO maybe it can be integrated with loadPlayers.. don't know.. should think about
        loadingView.setVisibility(View.VISIBLE);
        Call<RankPosition> call = RetrofitManager.getHTTPRankEndpoint().getUsers(thresold, direction);
        call.enqueue(new TPCallback<RankPosition>() {
            @Override
            public void mOnResponse(Call<RankPosition> call, Response<RankPosition> response) {
                if(response.code() == 200 && response.body().rank != null) {
                    users.addAll(first ? 0 : users.size() - 1, response.body().rank);
                    setPlayersListItems(users);
                    if(response.body().rank.size() == 0) {
                        // no users and first user is first.. absolute first
                        if(first) absolute_first = true;
                        else absolute_last = true;
                    }
                }
                // stop loading
                loadingView.setVisibility(View.GONE);
                loadable = true;
            }

            @Override
            public void mOnFailure(Call<RankPosition> call, Throwable t) {}

            @Override
            public void then() {}
        });
    }

    protected void loadPlayers() { loadPlayers(null, null, null); }
    protected void loadPlayers(Integer thresold, String direction, final Integer selectedPosition) {
        loadingView.setVisibility(View.VISIBLE);
        Call<RankPosition> call = RetrofitManager.getHTTPRankEndpoint().getUsers(thresold, direction);
        call.enqueue(new TPCallback<RankPosition>() {
            @Override
            public void mOnResponse(Call<RankPosition> call, Response<RankPosition> response) {
                if(response.code() == 200 && response.body().rank != null) {
                    users = response.body().rank;
                    setPlayersListItems(users);
                    if(selectedPosition == null) {
                        // center on my position
                        int numberOfItemsInHalfList = (int) (playersList.getHeight() / ( 2 * playerListItemHeight)), position = users.indexOf(currentUser);
                        playersList.scrollToPosition((numberOfItemsInHalfList >= position) ? 0 : users.indexOf(currentUser) - numberOfItemsInHalfList);
                    } else playersList.scrollToPosition(selectedPosition);
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
                    TPUtils.hideKeyboard(RankActivity.this);
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
        TPUtils.hideKeyboard(this);
    }

    // TODO unificando qua e in find opponent si può togliere l'override di la (per @donadev)
    protected void doSearch(String username) {
        if(all) {
            if(username.equals("")) loadPlayers();
            else {
                loadingView.setVisibility(View.VISIBLE);
                Call<SuccessUsers> call = RetrofitManager.getHTTPRankEndpoint().getSearchResult(username);
                call.enqueue(new TPCallback<SuccessUsers>() {
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
                    public void mOnFailure(Call<SuccessUsers> call, Throwable t) {}

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
    @Optional
    @OnClick(R.id.rank_scroll)
    public void rankScrollClick() {
        if(scrollToTop) {
            rankScroll.setBackground(ContextCompat.getDrawable(this, R.drawable.rank_scroll_down_button));
            loadPlayers(1, up, 0);
            absolute_first = true; // is the absolute first
        } else {
            rankScroll.setBackground(ContextCompat.getDrawable(this, R.drawable.rank_scroll_up_button));
            loadPlayers();
            absolute_first = false; // it may not be the absolute first
        }
        scrollToTop = !scrollToTop;
    }
    // touch handler
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        TPUtils.hideKeyboard(this);
        return super.dispatchTouchEvent(ev);
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

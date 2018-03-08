package it.triviapatente.android.app.views.rank;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.OnSwipeTouchListener;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.TPCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.DividerItemDecoration;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.footer.TPTellAFriendFooter;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.PlayerRankHolder;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.responses.RankPosition;
import it.triviapatente.android.models.responses.SuccessUsers;

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
    @BindView(R.id.dummy_layout) LinearLayout dummyLayout;
    protected boolean all = true;
    @BindView(R.id.playerList) RecyclerView playersList;
    @BindView(R.id.swipeRefreshLayout) protected SwipyRefreshLayout refreshLayout;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    @BindView(R.id.no_users) TextView noUsersAlert;
    @BindString(R.string.no_user_found) String noUsersAlertString;
    List<User> users;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @Nullable @BindView(R.id.rank_scroll) ImageButton rankScroll;
    @Nullable @BindView(R.id.rank_scroll_container) ConstraintLayout rankScrollContainer;
    private boolean scrollToTop = true;

    @BindColor(R.color.triviaColor1) @ColorInt int triviaColor1;
    @BindColor(R.color.triviaColor2) @ColorInt int triviaColor2;
    @BindColor(R.color.triviaColor3) @ColorInt int triviaColor3;
    @BindColor(R.color.triviaColor4) @ColorInt int triviaColor4;



    @BindString(R.string.direction_down) String down;
    @BindString(R.string.direction_up) String up;

    private boolean absolute_last = false, loadable = true;
    private void setLoadable(Boolean value) {
        loadable = value;
        if(loadable) enableRefreshLayout();
        else disableRefreshLayout();
    }
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        init(true);
    }
    protected void disableRefreshLayout() {
        refreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }
    protected void enableRefreshLayout() {
        refreshLayout.setOnTouchListener(null);
    }
    private void sharedRefreshLayoutInit() {
        refreshLayout.setColorSchemeColors(triviaColor1, triviaColor2, triviaColor3, triviaColor4);
        initRefreshLayout();
    }
    protected void initRefreshLayout() {
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(users != null) {
                    if (direction == SwipyRefreshLayoutDirection.TOP) {
                        loadPlayers(users.get(0).internalPosition, down, LoadAndScrollTo.no_scroll);
                    } else {
                        loadPlayers(users.get(users.size() - 1).internalPosition, up, LoadAndScrollTo.no_scroll);
                    }
                }
            }
        });
    }

    protected void init(boolean paginationRequired) {
        // update unicode strings
        noUsersAlertString = TPUtils.translateEmoticons(noUsersAlertString);
        noUsersAlert.setText(noUsersAlertString);

        sharedRefreshLayoutInit();
        initSearchBar();
        initPlayerList(paginationRequired);
        loadPlayers();
    }

    protected void setPlayersListItems(List<User> userList) {
        // SETTING AGAIN THE ADAPTER MAKES SURE THAT ALL CALCULATION ARE PERFORMED FROM TOP!
        // IF YOU CHANGE THIS, FIX ALSO THE SCROLL TO POSITION IN LOAD PLAYERS!
        playersList.setAdapter(new TPListAdapter<>(
                this,
                userList,
                R.layout.list_element_player_rank_holder,
                PlayerRankHolder.class,
                0, //R.layout.list_element_tell_a_friend_footer,
                null, //TPTellAFriendFooter.class,
                playerListItemHeight,
                playersList
        ));

    }
    private void updateRankScrollVisibility() {
        updateRankScrollVisibility(
                mLayoutManager.findFirstCompletelyVisibleItemPosition(),
                mLayoutManager.findLastCompletelyVisibleItemPosition()
        );
    }
    private void addPagination() {
        mLayoutManager = new LinearLayoutManager(this);
        playersList.setLayoutManager(mLayoutManager);
        playersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                updateRankScrollVisibility();
                if(mLayoutManager.findFirstVisibleItemPosition() == 0 && users.get(0).position == 1 ||
                        mLayoutManager.findLastVisibleItemPosition() == users.size() - 1 && absolute_last) {
                    disableRefreshLayout();
                } else if(loadable && users != null) {
                    if(dy < 0 && mLayoutManager.findFirstVisibleItemPosition() == 0 && users.get(0).position != 1) {
                        setLoadable(false);
                        // scroll down on first item which is not the absolute first
                        // once per instance this is not true, the first time the first may be the absolute first
                        loadPlayers(users.get(0).internalPosition, down, LoadAndScrollTo.no_scroll);
                    } else if(dy > 0 && mLayoutManager.findLastVisibleItemPosition() == users.size() - 1 && !absolute_last) {
                        setLoadable(false);
                        // scroll up on last item which is not the absolute last
                        // once per instance this is not true, the first time the last may be the absolute last
                        loadPlayers(users.get(users.size() - 1).internalPosition, up, LoadAndScrollTo.no_scroll);
                    }
                }
            }
        });
    }

    enum LoadAndScrollTo {
        top,
        bottom,
        userPosition,
        no_scroll
    }

    protected void startLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }
    protected void stopLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private int getNumberOfViewableItems() {
        return playersList.getHeight() / playerListItemHeight;
    }
    protected void loadPlayers() {loadPlayers(null); }
    protected void loadPlayers(SimpleCallback cb) {loadPlayers(null, null, LoadAndScrollTo.userPosition, cb); }
    protected void loadPlayers(final Integer thresold, final String direction, final LoadAndScrollTo position) { loadPlayers(thresold, direction, position, null); }

        protected void loadPlayers(final Integer thresold, final String direction, final LoadAndScrollTo position, final SimpleCallback cb) {
        noUsersAlert.setVisibility(View.GONE);
        startLoading();
        Call<RankPosition> call = RetrofitManager.getHTTPRankEndpoint().getUsers(thresold, direction);
        call.enqueue(new TPCallback<RankPosition>() {
            @Override
            public void mOnResponse(Call<RankPosition> call, Response<RankPosition> response) {
                if(response.code() == 200 && response.body().rank != null) {
                    List<User> newUsers = response.body().rank;
                    if(newUsers.size() == 0) { absolute_last = true; } // must be the bottom pagination
                    else if(users == null) { users = newUsers; } // all the users are loaded now
                    else {
                        // decide where to put the new users
                        if(newUsers.get(0).internalPosition >= users.get(users.size() - 1).internalPosition) {
                            users.addAll(users.size(), newUsers); // append trailing users
                        } else {
                            users.addAll(0, newUsers); // append leading users
                        }
                    }
                    if(newUsers.size() > 0) {
                        setPlayersListItems(users);
                        int scrollToPosition = 0, offset = getNumberOfViewableItems();
                        switch (position) {
                            case top: scrollToPosition = 0;break;
                            case bottom: scrollToPosition = users.size() - 1; break;
                            case userPosition:
                                int position = users.indexOf(currentUser);
                                // if (position == users.size() - 1) absolute_last = true; // if my user is last, set absolute last
                                /*if (users.size() - position <= offset / 4)
                                    scrollToPosition = users.size() - offset; // if i can't be well centered, try to fill the entire screen
                                else*/
                                    scrollToPosition = (offset >= position) ? 0 : position - offset / 4;
                                break;
                            case no_scroll:
                                if(users.get(0) == newUsers.get(0)) {
                                    // new users has been insert at the beginning
                                    scrollToPosition = newUsers.size() + 1; // to avoid scroll effect
                                } else {
                                    // new users has been insert at the end
                                    scrollToPosition = users.size() - newUsers.size() - offset - 1; // to avoid scroll effect
                                }
                        }
                        playersList.scrollToPosition(scrollToPosition);
                        updateRankScrollVisibility(scrollToPosition, scrollToPosition + offset);
                    }
                }
            }

            @Override
            public void mOnFailure(Call<RankPosition> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                        .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadPlayers(thresold, direction, position);
                            }
                        })
                        .show();
            }

            @Override
            public void then() {
                stopLoading();
                if (cb != null) cb.execute();
                // show other items
                playersList.setVisibility(View.VISIBLE);
                // allow to load more again
                setLoadable(true);
                if (mLayoutManager.findFirstVisibleItemPosition() == 0 && users.get(0).position == 1 ||
                        mLayoutManager.findLastVisibleItemPosition() == users.size() - 1 && absolute_last) {
                    disableRefreshLayout();
                }
            }
        });
    }

    protected void initPlayerList(boolean paginationRequired) {
        playersList.setLayoutManager(new LinearLayoutManager(this));
        playersList.setOnTouchListener(new OnSwipeTouchListener(this));
        playersList.addItemDecoration(new DividerItemDecoration(mainColor, playersList.getWidth()));
        if(paginationRequired) addPagination();
    }

    protected void initSearchBar() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    RankActivity.this.doSearch(searchBar.getText().toString());
                    TPUtils.hideKeyboard(RankActivity.this, dummyLayout);
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

    // TODO unificando qua e in find opponent la chiamata al backend si può togliere l'override di la (per @donadev)
    protected void doSearch(final String username) {
        if(all) {
            if(username.equals("")) loadPlayers();
            else {
                startLoading();
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
                    public void mOnFailure(Call<SuccessUsers> call, Throwable t) {
                        Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                                .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doSearch(username);
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

    @Optional
    @OnClick(R.id.rank_scroll)
    public void rankScrollClick() {
        SimpleCallback cb = new SimpleCallback() {
            @Override
            public void execute() {
                updateRankScrollDirection(!scrollToTop);
            }
        };
        int offset = getNumberOfViewableItems();
        // load new list
        if(scrollToTop) {
            if(users != null && users.size() > 0 && users.get(0).internalPosition == 1) {
                updateRankScrollVisibility(0,  offset);
                playersList.scrollToPosition(0);
            }
            else {
                users = null; // clean user list
                absolute_last = false; // clean settings
                loadPlayers(0, up, LoadAndScrollTo.top, cb);
            }
        }
        else {
            int currentUserIndex;
            if(users != null && (currentUserIndex = users.indexOf(currentUser)) != -1) {
                updateRankScrollVisibility(currentUserIndex, currentUserIndex + offset);
                playersList.scrollToPosition(currentUserIndex);
            }
            else {
                users = null; // clean user list
                absolute_last = false; // clean settings
                loadPlayers(cb);
            }
        }
    }

    protected void updateRankScrollDirection(boolean toTop) {
        if(rankScroll == null) return;
        if(toTop) rankScroll.setBackground(ContextCompat.getDrawable(this, R.drawable.rank_scroll_up_button));
        else rankScroll.setBackground(ContextCompat.getDrawable(this, R.drawable.rank_scroll_down_button));
        scrollToTop = toTop;
    }

    protected void updateRankScrollVisibility(int firstPosition, int lastPosition) {
        if(rankScroll == null || rankScrollContainer == null) return;
        if(users != null) {
            int currentUserIndex = users.indexOf(currentUser);
            boolean currentUserVisible = false, firstUserVisible = false;
            if(currentUserIndex <= lastPosition
                    && currentUserIndex >= firstPosition) {
                // current user is currently visible
                currentUserVisible = true;
            }
            if(users.get(0).internalPosition == 1 && firstPosition == 0) {
                firstUserVisible = true;
            }
            if(currentUserVisible && firstUserVisible) rankScrollContainer.setVisibility(View.GONE); // no need to show button
            else {
                rankScrollContainer.setVisibility(View.VISIBLE);
                if(!currentUserVisible && firstUserVisible) updateRankScrollDirection(false);
                else  updateRankScrollDirection(true);
            }
        }
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

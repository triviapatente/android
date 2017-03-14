package com.ted_developers.triviapatente.app.views.find_opponent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPDialog;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.DividerItemDecoration;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPTellAFriendFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.ProposedOpponentHolder;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.game_page.GameMainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.SuccessUsers;

import java.util.Arrays;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Response;

public class FindOpponentActivity extends TPActivity {
    // all or friends
    @BindView(R.id.all_or_friends) LinearLayout allOrFriendsBlock;
    @BindView(R.id.all_button) Button allButton;
    @BindView(R.id.friends_button) Button friendsButton;
    @BindDrawable(R.drawable.button_all_not_selected) Drawable allButtonNotSelected;
    @BindDrawable(R.drawable.button_all_selected) Drawable allButtonSelected;
    @BindDrawable(R.drawable.button_friends_not_selected) Drawable friendsButtonNotSelected;
    @BindDrawable(R.drawable.button_friends_selected) Drawable friendsButtonSelected;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @BindColor(android.R.color.white) @ColorInt int whiteColor;
    // search
    @BindView(R.id.search_bar) EditText searchBar;
    private boolean all;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // players
    @BindView(R.id.playerList) RecyclerView playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    @BindView(R.id.no_users) TextView noUsersAlert;
    @BindView(R.id.blurImageView) ImageView blurImageView;
    List<User> suggestedUsers;
    // friends not shown
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_opponent);
        // init action_bar
        if(getIntent().getBooleanExtra("random", false)) {
            searchRandomOpponent();
        } else {
            // init
            init();
        }
    }

    private void init() {
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        initSearchBar();
        initPlayerList();
        allButtonClick();
    }

    private void initSearchBar() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    FindOpponentActivity.this.doSearch(searchBar.getText().toString());
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
    }

    private void doSearch(String username) {
        if(all) {
            if(username.equals("")) loadPlayers();
            else {
                loadingView.setVisibility(View.VISIBLE);
                Call<SuccessUsers> call = RetrofitManager.getHTTPGameEndpoint().getSearchResult(username);
                call.enqueue(new TPCallback<SuccessUsers>() {
                    @Override
                    public void mOnResponse(Call<SuccessUsers> call, Response<SuccessUsers> response) {
                        if(response.code() == 200) {
                            if(response.body().users.size() == 0) {
                                noUsersAlert.setVisibility(View.VISIBLE);
                            } else {
                                setPlayersListItems(response.body().users);
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

    private void initPlayerList() {
        playersList.setLayoutManager(new LinearLayoutManager(this));
        playersList.setOnTouchListener(new OnSwipeTouchListener(this));
        playersList.addItemDecoration(new DividerItemDecoration(mainColor, playersList.getWidth()));
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
        params.y = actionBar.getMeasuredHeight() + allOrFriendsBlock.getMeasuredHeight();
    }

    private void setPlayersListItems(List<User> userList) {
        playersList.setAdapter(new TPListAdapter<>(this, userList, R.layout.list_element_proposed_opponent_holder, ProposedOpponentHolder.class, R.layout.list_element_tell_a_friend_footer, TPTellAFriendFooter.class, playerListItemHeight, playersList));
    }

    private void loadPlayers() {
        if(suggestedUsers == null) {
            loadingView.setVisibility(View.VISIBLE);
            Call<SuccessUsers> call = RetrofitManager.getHTTPGameEndpoint().getSuggestedUsers();
            call.enqueue(new TPCallback<SuccessUsers>() {
                @Override
                public void mOnResponse(Call<SuccessUsers> call, Response<SuccessUsers> response) {
                    if(response.code() == 200 && response.body().success && response.body().users != null) {
                        suggestedUsers = response.body().users;
                        Log.d("TEST", String.valueOf(suggestedUsers.size()));
                        setPlayersListItems(suggestedUsers);
                    }
                    // show other items
                    playersList.setVisibility(View.VISIBLE);
                    // stop loading
                    loadingView.setVisibility(View.GONE);
                }

                @Override
                public void mOnFailure(Call<SuccessUsers> call, Throwable t) {}

                @Override
                public void then() {}
            });
        } else {
            setPlayersListItems(suggestedUsers);
        }
    }

    @OnClick(R.id.all_button)
    public void allButtonClick() {
        all = true;
        blurImageView.setVisibility(View.GONE);
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
        loadingView.setVisibility(View.VISIBLE);
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
            loadingView.setVisibility(View.GONE);

            playersList.post(new Runnable() {
                @Override
                public void run() {
                    Blurry.with(FindOpponentActivity.this)
                            .radius(10)
                            .sampling(1)
                            .capture(playersList)
                            .into(blurImageView);
                }
            });
            blurImageView.setVisibility(View.VISIBLE);
            facebookDialog.show();
        }
    }

    @OnClick(R.id.x_button)
    public void xButtonClick() {
        searchBar.setText("");
    }

    // touch handler
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // random opponent
    private void searchRandomOpponent() {
        Intent intent = new Intent(FindOpponentActivity.this, GameMainPageActivity.class);
        intent.putExtra(getResources().getString(R.string.extra_boolean_game), true);
        FindOpponentActivity.this.startActivity(intent);
    }
}

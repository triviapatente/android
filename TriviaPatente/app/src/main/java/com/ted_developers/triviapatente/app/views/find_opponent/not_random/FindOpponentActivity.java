package com.ted_developers.triviapatente.app.views.find_opponent.not_random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.BlurredImagesMaker;
import com.ted_developers.triviapatente.app.utils.custom_classes.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.AccountLinkerDialog;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal.ProposedOpponentHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.game_page.NewGameActivity;
import com.ted_developers.triviapatente.app.views.players_list.TPPLayersList;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.SuccessUsers;

import java.util.Arrays;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class FindOpponentActivity extends AppCompatActivity {
    // all or friends
    @BindView(R.id.all_or_friends) LinearLayout allOrFriendsBlock;
    @BindView(R.id.all_button) Button allButton;
    @BindView(R.id.friends_button) Button friendsButton;
    @BindDrawable(R.drawable.all_button_not_selected) Drawable allButtonNotSelected;
    @BindDrawable(R.drawable.all_button_selected) Drawable allButtonSelected;
    @BindDrawable(R.drawable.friends_button_not_selected) Drawable friendsButtonNotSelected;
    @BindDrawable(R.drawable.friends_button_selected) Drawable friendsButtonSelected;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @BindColor(android.R.color.white) @ColorInt int whiteColor;
    // search
    @BindView(R.id.search_bar) EditText searchBar;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // toolbar
    @BindView(R.id.toolbar) BackPictureTPToolbar toolbar;
    @BindString(R.string.find_opponent_title) String toolbarTitle;
    @BindString(R.string.new_game_title) String backTitle;
    // players
    @BindView(R.id.playerList) RecyclerView playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    // friends not shown
    List<User> fakeUsers = Arrays.asList(
            new User("TriviaPatente", null, true),
            new User("UnGioco", null, true),
            new User("Davvero", null, false),
            new User("Davvero", null, true),
            new User("Davvero", null, false),
            new User("Fantastico", null, false),
            new User("Probabilmente", null, true),
            new User("IlMigliore", null, true),
            new User("InAssoluto", null, true),
            new User("LoAdoro", null, false)
    );
    // modal
    @BindView(R.id.playersListBlock) RelativeLayout playersListBlock;
    private boolean firstTime = true;
    private AccountLinkerDialog facebookDialog;
    @BindView(R.id.blurredView) View blurredView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_opponent);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // init
        init();
    }

    private void init() {
        // hide other elements
        initPlayerList();
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        initToolbar();
        allButtonClick();
        initSearchBar();
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
    }

    private void doSearch(String username) {
        if(username.equals("")) loadPlayers();
        loadingView.setVisibility(View.VISIBLE);
        Call<SuccessUsers> call = RetrofitManager.getHTTPGameEndpoint().getSearchResult(username);
        call.enqueue(new TPCallback<SuccessUsers>() {
            @Override
            public void mOnResponse(Call<SuccessUsers> call, Response<SuccessUsers> response) {
                if(response.code() == 200) {
                    if(response.body().users.size() == 0) {
                        // todo show no user matching
                    } else {
                        setPlayersListItems(response.body().users);
                    }
                }
            }

            @Override
            public void mOnFailure(Call<SuccessUsers> call, Throwable t) {

            }

            @Override
            public void then() {
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    private void initPlayerList() {
        playersList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initDialog() {
        facebookDialog = new AccountLinkerDialog(this) {
            @Override
            public void onExit() {
                allButtonClick();
                this.hide();
            }

            @Override
            public void onConfirm() {
                // todo connect to facebook
            }
        };
        WindowManager.LayoutParams params
                = facebookDialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP;
        params.y = (int) playersListBlock.getY();
        facebookDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void setPlayersListItems(List<User> userList) {
        playersList.setVisibility(View.GONE);
        playersList.setAdapter(new TPListAdapter<User>(this, userList, R.layout.proposed_opponent, ProposedOpponentHolder.class, R.layout.tell_a_friend_footer, TPFooter.class, playerListItemHeight, playersList));
        playersList.setVisibility(View.VISIBLE);
    }

    private void loadPlayers() {
        Call<SuccessUsers> call = RetrofitManager.getHTTPGameEndpoint().getSuggestedUsers();
        call.enqueue(new TPCallback<SuccessUsers>() {
            @Override
            public void mOnResponse(Call<SuccessUsers> call, Response<SuccessUsers> response) {
                if(response.code() == 200 && response.body().success && response.body().users != null) {
                    setPlayersListItems(response.body().users);
                }
                // show other items
                playersList.setVisibility(View.VISIBLE);
                // stop loading
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void mOnFailure(Call<SuccessUsers> call, Throwable t) {

            }

            @Override
            public void then() {

            }
        });
    }

    private void initToolbar() {
        // set title
        toolbar.setTitle(toolbarTitle);
        // set profile picture
        // TODO get dinamically
        toolbar.setProfilePicture(getResources().getDrawable(R.drawable.no_image));
        // set back button
        toolbar.setBackButtonText(backTitle);
        toolbar.setBackButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(FindOpponentActivity.this, NewGameActivity.class);
                FindOpponentActivity.this.startActivity(myIntent);
            }
        });
    }

    @OnClick(R.id.all_button)
    public void allButtonClick() {
        searchBar.setText("");
        blurredView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        allButton.setBackground(allButtonSelected);
        friendsButton.setBackground(friendsButtonNotSelected);
        allButton.setTextColor(whiteColor);
        friendsButton.setTextColor(mainColor);
        loadPlayers();
    }

    @OnClick(R.id.friends_button)
    public void friendsButtonClick() {
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
            // todo blur
            facebookDialog.show();
        }
        loadingView.setVisibility(View.GONE);
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
}

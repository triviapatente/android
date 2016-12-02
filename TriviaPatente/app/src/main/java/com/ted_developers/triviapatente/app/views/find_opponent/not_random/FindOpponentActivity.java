package com.ted_developers.triviapatente.app.views.find_opponent.not_random;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
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
    @BindView(R.id.search_bar) LinearLayout searchBar;
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // toolbar
    @BindView(R.id.toolbar) BackPictureTPToolbar toolbar;
    @BindString(R.string.find_opponent_title) String toolbarTitle;
    @BindString(R.string.new_game_title) String backTitle;
    // players
    TPPLayersList<User> playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;
    // friends not shown
    List<User> friendsNotShown = Arrays.asList(
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
    @BindView(R.id.modal_facebook) RelativeLayout facebookModal;
    @BindView(R.id.exit_button) ImageButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_opponent);
        ButterKnife.bind(this);
        playersList = (TPPLayersList<User>) getSupportFragmentManager().findFragmentById(R.id.players);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide other elements
        playersList.getView().setVisibility(View.GONE);
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        // init
        init();
    }

    private void init() {
        initToolbar();
        allButtonClick();
    }

    private void setPlayersListItems(List<User> userList) {
        playersList.getView().setVisibility(View.GONE);
        playersList.setItems(userList, R.layout.proposed_opponent, ProposedOpponentHolder.class, R.layout.tell_a_friend_footer, TPFooter.class, playerListItemHeight);
        playersList.getView().setVisibility(View.VISIBLE);
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
                playersList.getView().setVisibility(View.VISIBLE);
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
        loadingView.setVisibility(View.VISIBLE);
        allButton.setBackground(allButtonSelected);
        friendsButton.setBackground(friendsButtonNotSelected);
        allButton.setTextColor(whiteColor);
        friendsButton.setTextColor(mainColor);
        facebookModal.setVisibility(View.GONE);
        loadPlayers();
        playersList.getView().setClickable(false);
    }

    @OnClick(R.id.friends_button)
    public void friendsButtonClick() {
        loadingView.setVisibility(View.VISIBLE);
        friendsButton.setBackground(friendsButtonSelected);
        allButton.setBackground(allButtonNotSelected);
        allButton.setTextColor(mainColor);
        friendsButton.setTextColor(whiteColor);
        // todo check if connected
        if (false) {
            // todo load friends
        } else {
            facebookModal.setVisibility(View.VISIBLE);
            setPlayersListItems(friendsNotShown);
            playersList.getView().setClickable(true);
            // todo make blurred background
        }
        loadingView.setVisibility(View.GONE);
    }

    @OnClick(R.id.exit_button)
    public void exitButton() {
        allButtonClick();
    }
}

package com.ted_developers.triviapatente.app.views.find_opponent.not_random;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPRecentGamesFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPTellFriendFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal.ProposedOpponentHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.game_page.NewGameActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.app.views.players_list.TPPLayersList;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Invite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindOpponentActivity extends AppCompatActivity {

    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // toolbar
    @BindView(R.id.toolbar) BackPictureTPToolbar toolbar;
    @BindString(R.string.find_opponent_title) String toolbarTitle;
    @BindString(R.string.new_game_title) String backTitle;
    // players
    TPPLayersList<User> playersList;
    @BindDimen(R.dimen.player_list_item_height) int playerListItemHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_opponent);
        ButterKnife.bind(this);
        playersList = (TPPLayersList<User>) getSupportFragmentManager().findFragmentById(R.id.players);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new ProgressTask().execute();
    }

    private void init() {
        initToolbar();
        loadPlayers();
        loadingView.setVisibility(View.GONE);
    }

    private void loadPlayers() {
        List<User> userList = new ArrayList<>();
        User u = new User();
        u.last_game_won = true;
        u.username = "Antonio Terpin";
        userList.add(u);
        u = new User();
        u.last_game_won = false;
        u.username = "Luigi Donadel";
        userList.add(u);
        for(int i = 0; i < 10; i ++) {userList.add(u);}
        playersList.setItems(userList, R.layout.proposed_opponent, ProposedOpponentHolder.class, TPTellFriendFooter.class, playerListItemHeight);
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


    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
            // start loading
            loadingView.setVisibility(View.VISIBLE);
            // hide other elements
            //bulkVisibilitySetting(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            FindOpponentActivity.this.init();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {}
    }
}

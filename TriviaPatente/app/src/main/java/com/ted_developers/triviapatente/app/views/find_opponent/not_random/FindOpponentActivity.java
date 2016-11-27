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
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.game_page.NewGameActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;

import butterknife.BindColor;
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
    // all or friends
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_opponent);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new ProgressTask().execute();
    }

    private void init() {
        initToolbar();
        loadingView.setVisibility(View.GONE);
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
        allButton.setBackground(allButtonSelected);
        searchBar.setVisibility(View.VISIBLE);
        friendsButton.setBackground(friendsButtonNotSelected);
        allButton.setTextColor(whiteColor);
        friendsButton.setTextColor(mainColor);
        // todo do stuff
    }

    @OnClick(R.id.friends_button)
    public void friendsButtonClick() {
        friendsButton.setBackground(friendsButtonSelected);
        allButton.setBackground(allButtonNotSelected);
        searchBar.setVisibility(View.GONE);
        allButton.setTextColor(mainColor);
        friendsButton.setTextColor(whiteColor);
        // todo do stuff
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

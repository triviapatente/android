package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;

import butterknife.BindString;
import butterknife.BindView;

public class ChooseCategoryActivity extends AppCompatActivity {
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // toolbar
    @BindView(R.id.toolbar)
    BackPictureTPToolbar toolbar;
    @BindString(R.string.new_game_title) String toolbarTitle;
    @BindString(R.string.main_page_title) String backTitle;
    // categories
    @BindView(R.id.proposed_categories) RecyclerView proposedCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        init();
    }

    private void init() {
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        initToolbar();
        loadCategories();
        // stop loading
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
                Intent myIntent = new Intent(ChooseCategoryActivity.this, MainPageActivity.class);
                ChooseCategoryActivity.this.startActivity(myIntent);
            }
        });
    }

    private void loadCategories() {

    }
}

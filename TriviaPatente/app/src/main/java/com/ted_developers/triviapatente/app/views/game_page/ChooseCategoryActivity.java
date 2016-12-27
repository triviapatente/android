package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPActionBar;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPEnterAnimListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.CategoryHolder;
import com.ted_developers.triviapatente.app.views.game_page.play_round.PlayRoundActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.SuccessCategories;
import com.ted_developers.triviapatente.models.responses.SuccessCategory;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;

public class ChooseCategoryActivity extends TPActivity {
    // data
    @BindString(R.string.extra_string_round) String extraStringRound;
    @BindString(R.string.extra_string_opponent) String extraStringOpponent;
    private User opponent;
    private Round currentRound;
    // action_bar
    @BindView(R.id.toolbar)
    BackPictureTPActionBar toolbar;
    // game header
    @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    @BindString(R.string.choose_category_game_header_subtitle) String chooseCategoryGameHeaderSubtitle;
    @BindView(R.id.subtitleImage) RoundedImageView subtitleImage;
    // get categories
    GameSocketManager gameSocketManager = new GameSocketManager();
    @BindView(R.id.proposed_categories) RecyclerView proposedCategories;
    @BindDimen(R.dimen.choose_category_proposed_height) int categoriesHeight;
    @BindView(R.id.loadingView) RelativeLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        // init
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(extraStringOpponent), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(extraStringRound), Round.class);
        initToolbar();
        initGameHeader();
        loadProposedCategories();
    }

    private void initToolbar() {
        // title
        if(opponent != null && toolbar.getTitle().equals("")) {
            if(opponent.name == null || opponent.surname == null) {
                toolbar.setTitle(opponent.username);
            } else {
                toolbar.setTitle(opponent.name + " " + opponent.surname);
            }
        }
        // picture
        // todo get dinamically
        toolbar.setProfilePicture(ContextCompat.getDrawable(this, R.drawable.no_image));
        toolbar.setBackButtonOnClick(MainPageActivity.class);
    }

    private void initGameHeader() {
        gameHeaderTitle.setText("Round " + currentRound.number);
        gameHeaderSubtitle.setText(chooseCategoryGameHeaderSubtitle);
    }

    private void loadProposedCategories() {
        proposedCategories.setLayoutManager(new LinearLayoutManager(this));
        proposedCategories.setOnTouchListener(new OnSwipeTouchListener(this));
        gameSocketManager.get_proposed_categories(currentRound.game_id, currentRound.id, new SocketCallback<SuccessCategories>() {
            @Override
            public void response(SuccessCategories response) {
                final List<Category> categories = response.categories;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TPEnterAnimListAdapter<Category> adapter = new TPEnterAnimListAdapter<Category>(
                                ChooseCategoryActivity.this, categories,
                                R.layout.proposed_category, CategoryHolder.class,
                                0, null,
                                categoriesHeight, proposedCategories);
                        adapter.computeFooterHeightManager.setOption(TPListAdapter.compute_footer_options.SAME_HEIGHT);
                        proposedCategories.setAdapter(adapter);
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
    }

    public void chooseCategory(final Category category) {
        // send category choosen event
        gameSocketManager.choose_category(currentRound.game_id, currentRound.id, category.id, new SocketCallback<SuccessCategory>() {
            @Override
            public void response(SuccessCategory response) {
                if(response.success) {
                    // start play round activity
                    Intent intent = new Intent(ChooseCategoryActivity.this, PlayRoundActivity.class);
                    intent.putExtra(ChooseCategoryActivity.this.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
                    intent.putExtra(ChooseCategoryActivity.this.getString(R.string.extra_string_round), RetrofitManager.gson.toJson(currentRound));
                    intent.putExtra(ChooseCategoryActivity.this.getString(R.string.extra_string_category), RetrofitManager.gson.toJson(category));
                    ChooseCategoryActivity.this.startActivity(intent);
                    ChooseCategoryActivity.this.finish();
                }
            }
        });
    }
}

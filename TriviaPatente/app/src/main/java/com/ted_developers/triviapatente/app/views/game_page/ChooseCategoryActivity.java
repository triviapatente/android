package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPEnterAnimListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.CategoryHolder;
import com.ted_developers.triviapatente.app.views.game_page.play_round.PlayRoundActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.SuccessCategories;
import com.ted_developers.triviapatente.models.responses.SuccessCategory;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;
import java.util.List;
import butterknife.BindDimen;
import butterknife.BindView;

public class ChooseCategoryActivity extends TPGameActivity {
    // get categories
    GameSocketManager gameSocketManager = new GameSocketManager();
    @BindView(R.id.proposed_categories) RecyclerView proposedCategories;
    @BindDimen(R.dimen.choose_category_proposed_height) int categoriesHeight;
    @BindView(R.id.loadingView) RelativeLayout loadingView;

    @Override
    protected String getToolbarTitle(){ return opponent.toString(); }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        // init
        loadProposedCategories();
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
                                R.layout.list_element_proposed_category_holder, CategoryHolder.class,
                                0, null,
                                categoriesHeight, proposedCategories);
                        adapter.computeFooterHeightManager.setOption(TPListAdapter.compute_footer_options.SAME_HEIGHT);
                        proposedCategories.setAdapter(adapter);
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
        gameHeader.setHeader(currentRound, currentCategory, false);
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

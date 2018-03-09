package it.triviapatente.android.app.views.game_page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.OnSwipeTouchListener;
import it.triviapatente.android.app.utils.baseActivityClasses.TPGameActivity;
import it.triviapatente.android.app.utils.custom_classes.listViews.adapters.TPEnterAnimListAdapter;
import it.triviapatente.android.app.utils.custom_classes.listViews.adapters.TPListAdapter;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.CategoryHolder;
import it.triviapatente.android.app.views.game_page.play_round.PlayRoundActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.responses.SuccessCategories;
import it.triviapatente.android.models.responses.SuccessCategory;
import it.triviapatente.android.models.responses.SuccessInitRound;
import it.triviapatente.android.socket.modules.game.GameSocketManager;
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

    @Override
    protected void customReinit() {
        gameSocketManager.init_round(gameID, new SocketCallback<SuccessInitRound>() {
            @Override
            public void response(final SuccessInitRound response) {
                if(response.success) {
                    if(response.ended != null && response.ended) {
                        gotoRoundDetails();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.timeout) Toast.makeText(getApplicationContext(), getString(R.string.httpConnectionError), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void loadProposedCategories() {
        proposedCategories.setLayoutManager(new LinearLayoutManager(this));
        proposedCategories.setOnTouchListener(new OnSwipeTouchListener(this));
        gameSocketManager.get_proposed_categories(currentRound.game_id, currentRound.id, new SocketCallback<SuccessCategories>() {
            @Override
            public void response(SuccessCategories response) {
                if(response.success) {
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
                } else {
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadProposedCategories();
                                }
                            })
                            .show();
                }
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
                } else {
                    if(response.status_code == 403) finish();
                    else if(response.timeout) Toast.makeText(getApplicationContext(), getString(R.string.httpConnectionError), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

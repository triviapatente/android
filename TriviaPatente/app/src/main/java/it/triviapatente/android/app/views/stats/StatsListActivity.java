package it.triviapatente.android.app.views.stats;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.ValueCallback;

import butterknife.BindString;
import butterknife.BindView;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.ReceivedData;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.GlobalInfos;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.socket.modules.base.BaseSocketManager;

public class StatsListActivity extends TPActivity {

    @BindString(R.string.stats_list_title) String title;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mLayout;
    @BindView(R.id.statsList) RecyclerView mRecyclerView;
    private StatsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new StatsAdapter(ReceivedData.statsHints, new ValueCallback<Category>() {
            @Override
            public void onReceiveValue(Category category) {
                Intent i = new Intent(StatsListActivity.this, StatDetailActivity.class);
                i.putExtra(getString(R.string.extra_string_category), RetrofitManager.gson.toJson(category));
                startActivity(i);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                authSocketManager.global_infos(new SocketCallback<GlobalInfos>() {
                    @Override
                    public void response(final GlobalInfos response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ReceivedData.statsHints = response.stats;
                                mAdapter.setItems(ReceivedData.statsHints);
                                mLayout.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });
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

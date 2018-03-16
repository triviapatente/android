package it.triviapatente.android.app.views.training;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.ReceivedData;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.http.modules.base.HTTPBaseEndpoint;
import it.triviapatente.android.http.modules.training.HTTPTrainingEndpoint;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.TrainingStats;
import it.triviapatente.android.models.responses.SuccessTraining;
import it.triviapatente.android.models.responses.SuccessTrainings;
import it.triviapatente.android.models.training.Training;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingActivity extends TPActivity {
    @BindString(R.string.activity_training_title) String title;
    @BindView(R.id.trainingList) RecyclerView trainingList;
    @BindView(R.id.emptyView) TextView emptyView;
    @BindString(R.string.training_list_empty_message) String emptyMessage;
    TrainingGraphFragment graphFragment;
    private TrainingStatViewer noErrorsViewer;
    private TrainingStatViewer errors12Viewer;
    private TrainingStatViewer errors34Viewer;
    private TrainingStatViewer moreErrorsViewer;
    private TrainingsAdapter mAdapter;
    private GridLayoutManager mManager;

    private void initFragments() {
        graphFragment = (TrainingGraphFragment) getSupportFragmentManager().findFragmentById(R.id.statsGraph);
        noErrorsViewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.no_errors_viewer);
        errors12Viewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.one_two_errors_viewer);
        errors34Viewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.three_four_errors_viewer);
        moreErrorsViewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.more_errors_viewer);
    }
    private void updateViewsFromStats(TrainingStats stats) {
        graphFragment.setStats(stats);
        noErrorsViewer.setValues(stats.no_errors, "0 errori");
        errors12Viewer.setValues(stats.errors_12, "1-2 errori");
        errors34Viewer.setValues(stats.errors_34, "3-4 errori");
        moreErrorsViewer.setValues(stats.more_errors, "4+ errori");
    }
    private void updateTrainings(List<Training> trainings) {
        mAdapter.setTrainings(trainings);
        emptyView.setVisibility(trainings.isEmpty() ? View.VISIBLE : View.GONE);
        trainingList.setVisibility(trainings.isEmpty() ? View.GONE : View.VISIBLE);

    }
    private void updateFrom(SuccessTrainings response) {
        updateViewsFromStats(response.stats);
        updateTrainings(response.trainings);
    }
    private SuccessTrainings mockResponse() {
        SuccessTrainings output = new SuccessTrainings();
        output.trainings = new ArrayList<>();
        output.stats = ReceivedData.trainingStats;
        for(int n = 0; n < 50; n++) {
            Training t = new Training();
            t.numberOfErrors = Math.abs(new Random().nextInt() % 5);
            output.trainings.add(t);
        }
        return output;
    }
    private final int DEFAULT_SPAN_COUNT = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        initFragments();
        updateViewsFromStats(ReceivedData.trainingStats);
        mAdapter = new TrainingsAdapter(this);
        mManager = new GridLayoutManager(this, DEFAULT_SPAN_COUNT);
        trainingList.setLayoutManager(mManager);
        trainingList.setAdapter(mAdapter);
        emptyMessage = TPUtils.translateEmoticons(emptyMessage);
        emptyView.setText(emptyMessage);
        load();
    }
    private void load() {
        Call<SuccessTrainings> request = RetrofitManager.getHttpTrainingEndpoint().getTrainings();
        request.enqueue(new Callback<SuccessTrainings>() {
            @Override
            public void onResponse(Call<SuccessTrainings> call, Response<SuccessTrainings> response) {
                SuccessTrainings output = response.body();
                //updateFrom(output);
                updateFrom(mockResponse());
            }

            @Override
            public void onFailure(Call<SuccessTrainings> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                        .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                load();
                            }
                        })
                        .show();
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

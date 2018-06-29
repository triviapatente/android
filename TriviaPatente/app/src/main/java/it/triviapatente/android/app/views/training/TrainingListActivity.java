package it.triviapatente.android.app.views.training;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.ReceivedData;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.DialogBottomSheet;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleItemCallback;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.TrainingStats;
import it.triviapatente.android.models.responses.SuccessTrainings;
import it.triviapatente.android.models.training.Training;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingListActivity extends TPActivity {
    @BindString(R.string.activity_training_title) String title;
    @BindView(R.id.trainingList) RecyclerView trainingList;
    @BindView(R.id.emptyView) TextView emptyView;
    @BindView(R.id.trainingBottomSheet) View trainingBottomSheet;
    @BindString(R.string.training_list_empty_message) String emptyMessage;
    TrainingGraphFragment graphFragment;
    private TrainingStatViewer noErrorsViewer;
    private TrainingStatViewer errors12Viewer;
    private TrainingStatViewer errors34Viewer;
    private TrainingStatViewer moreErrorsViewer;
    private TrainingsAdapter mAdapter;
    private GridLayoutManager mManager;
    private SimpleItemCallback<Training> trainingClickCallback = new SimpleItemCallback<Training>() {
        @Override
        public void execute(Training item) {
            Intent i = new Intent(TrainingListActivity.this, TrainingDetailsActivity.class);
            i.putExtra(getString(R.string.training_details_extra_training_key), RetrofitManager.gson.toJson(item));
            startActivity(i);
        }
    };

    @OnClick(R.id.newTrainingButton)
    void goToTraining() {
        getBottomSheet().show();
    }

    private void redirectToTraining(Boolean random) {
        Intent i = new Intent(this, TrainActivity.class);
        i.putExtra(getString(R.string.extra_string_random), random);
        startActivity(i);
    }

    private DialogBottomSheet getBottomSheet() {
        final DialogBottomSheet sheet = new DialogBottomSheet(trainingBottomSheet);
        sheet.setContent(R.string.new_training_title, R.string.new_training_content);
        sheet.setFirstAction(R.string.new_training_action1, DialogBottomSheet.ButtonType.BLUE, new SimpleCallback() {
            @Override
            public void execute() {
                sheet.dismiss();
                redirectToTraining(false);
            }
        });
        sheet.setSecondAction(R.string.new_training_action2, DialogBottomSheet.ButtonType.BLUE, new SimpleCallback() {
            @Override
            public void execute() {
                sheet.dismiss();
                redirectToTraining(true);
            }
        });
        return sheet;
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void initFragments() {
        graphFragment = (TrainingGraphFragment) getSupportFragmentManager().findFragmentById(R.id.statsGraph);
        noErrorsViewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.no_errors_viewer);
        errors12Viewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.one_two_errors_viewer);
        errors34Viewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.three_four_errors_viewer);
        moreErrorsViewer = (TrainingStatViewer) getSupportFragmentManager().findFragmentById(R.id.more_errors_viewer);
    }
    private void updateViewsFromStats(TrainingStats stats) {
        graphFragment.setStats(stats);
        noErrorsViewer.setValues(stats.no_errors, R.string.training_no_errors_caption);
        errors12Viewer.setValues(stats.errors_12, R.string.training_errors_12_caption);
        errors34Viewer.setValues(stats.errors_34, R.string.training_errors_34_caption);
        moreErrorsViewer.setValues(stats.more_errors, R.string.training_more_errors_caption);
    }
    private void updateTrainings(List<Training> trainings) {
        Collections.sort(trainings);
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
        setContentView(R.layout.activity_training_list);
        initFragments();
        updateViewsFromStats(ReceivedData.trainingStats);
        mAdapter = new TrainingsAdapter(this);
        mAdapter.setOnItemClickListener(trainingClickCallback);
        mManager = new GridLayoutManager(this, DEFAULT_SPAN_COUNT);
        trainingList.setLayoutManager(mManager);
        trainingList.setAdapter(mAdapter);
        emptyMessage = TPUtils.translateEmoticons(emptyMessage);
        emptyView.setText(emptyMessage);
    }
    private void load() {
        Call<SuccessTrainings> request = RetrofitManager.getHttpTrainingEndpoint().getTrainings();
        request.enqueue(new Callback<SuccessTrainings>() {
            @Override
            public void onResponse(Call<SuccessTrainings> call, Response<SuccessTrainings> response) {
                SuccessTrainings output = response.body();
                updateFrom(output);
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

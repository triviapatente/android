package it.triviapatente.android.app.views.training;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.QuizSheetCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.TrainingHolder;
import it.triviapatente.android.app.views.game_page.round_details.RoundDetailsQuestionAdapter;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.responses.SuccessQuizzes;
import it.triviapatente.android.models.responses.SuccessTraining;
import it.triviapatente.android.models.training.Training;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingDetailsActivity extends TPActivity {

    @BindView(R.id.trainingDetailsErrors) TextView trainingDetailsErrors;
    @BindView(R.id.trainingDetailsDate) TextView trainingDetailsDate;
    @BindView(R.id.trainingDetailsIcon) View trainingDetailsIcon;
    @BindView(R.id.trainingDetailsQuestionList) RecyclerView questionList;
    @BindView(R.id.roundDetailsSheet) View roundDetailsSheet;

    @BindString(R.string.training_details_date) String detailsDate;
    @BindString(R.string.training_details_errors_plural) String errorsPlural;
    @BindString(R.string.training_details_errors_plural) String errorsSingular;
    @BindString(R.string.training_details_extra_training_key) String extraKey;
    @BindString(R.string.training_details_title) String title;

    private Training training;
    private TrainingDetailsAdapter mAdapter;
    private LinearLayoutManager mManager;

    private String getDetailString(Training training) {
        if(training.numberOfErrors == 1) return errorsPlural;
        return errorsPlural.replace("%errors%", ""+training.numberOfErrors);
    }
    private String getDateString(Training training) {
        Date createdAt = training.getCreatedAt();
        String dateString = TPUtils.dateToSimpleFormat(createdAt);
        return detailsDate.replace("%date%", dateString);
    }
    private void updateTrainingDetails(Intent intent) {
        String trainingValue = intent.getStringExtra(extraKey);
        this.training = RetrofitManager.gson.fromJson(trainingValue, Training.class);
        this.trainingDetailsDate.setText(getDateString(training));
        this.trainingDetailsErrors.setText(getDetailString(training));
        TrainingHolder holder = new TrainingHolder(trainingDetailsIcon);
        holder.bind(training, this);
        load(training);
    }
    private void load(final Training training) {
        Call<SuccessQuizzes> call = RetrofitManager.getHttpTrainingEndpoint().getTraining(training.id);
        call.enqueue(new Callback<SuccessQuizzes>() {
            @Override
            public void onResponse(Call<SuccessQuizzes> call, Response<SuccessQuizzes> response) {
                if (response.isSuccessful()) {
                    SuccessQuizzes value = response.body();
                    mAdapter.setResponse(value);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.httpConnectionError, Snackbar.LENGTH_INDEFINITE).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessQuizzes> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                        .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                load(training);
                            }
                        })
                        .show();
            }
        });
    }
    private DividerItemDecoration getDivider() {
        DividerItemDecoration mDivider = new DividerItemDecoration(this, mManager.getOrientation());
        mDivider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_line_alpha));
        return mDivider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);
        mAdapter = new TrainingDetailsAdapter(this, new QuizSheetCallback(roundDetailsSheet));
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        questionList.setLayoutManager(mManager);
        questionList.addItemDecoration(getDivider());
        questionList.setAdapter(mAdapter);
        updateTrainingDetails(getIntent());
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

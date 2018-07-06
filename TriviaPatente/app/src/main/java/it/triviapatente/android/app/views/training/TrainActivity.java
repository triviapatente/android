package it.triviapatente.android.app.views.training;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.PreCachingLayoutManager;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.baseActivityClasses.TPGameActivity;
import it.triviapatente.android.app.utils.baseActivityClasses.TPPlayHandler;
import it.triviapatente.android.app.utils.custom_classes.DialogBottomSheet;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.ValuesCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizButtonHolder;
import it.triviapatente.android.app.views.game_page.FragmentGameOptions;
import it.triviapatente.android.app.views.game_page.play_round.QuizzesPagerAdapter;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.responses.SuccessQuizzes;
import it.triviapatente.android.models.responses.SuccessTraining;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainActivity extends TPActivity {

    @BindView(R.id.adView)
    AdView mAdView;

    // quizzes
    @BindView(R.id.quizzes) ViewPager quizzesViewPager;
    @BindView(R.id.trainingBottomSheet) View trainingBottomSheet;
    @BindView(R.id.quizPanel) LinearLayout quizPanel;
    @BindView(R.id.quizPanelScrollView)
    HorizontalScrollView quizScrollView;

    private QuizButtonAdapter panelAdapter;

    public QuizzesPagerAdapter quizzesAdapter;
    private TrainingToolbarHandler toolbarHandler;
    private FragmentGameOptions gameOptions;
    private SimpleCallback leaveGameCallback = new SimpleCallback() {
        @Override
        public void execute() {
            showSheet(getLeaveBottomSheet());
        }
    };

    private Boolean random = false;
    private Boolean getRandomPreference() {
        return getIntent().getBooleanExtra(getString(R.string.extra_string_random), false);
    }
    public void showSheet(DialogBottomSheet sheet) {
        sheet.show();
        sheet.setOnDismissListener(new SimpleCallback() {
            @Override
            public void execute() {
                toolbarHandler.resumeTimer();
            }
        });
        toolbarHandler.freezeTimer();
    }
    public void hideSheet(DialogBottomSheet sheet) {
        sheet.dismiss();
    }
    public void setAnswer(Quiz q, Boolean answer) {
        int position = quizzesAdapter.find(q.id);
        if(position != -1) {
            quizzesAdapter.quizzesList.get(position).my_answer = answer;
            setAnsweredButton(position);
            final int nextPosition = TPPlayHandler.getNextCurrentItem(quizzesAdapter, position);
            if(nextPosition != position) {
                selectButton(nextPosition);
                toolbarHandler.updateProgress(quizzesAdapter);
            } else {
                gameOptions.fromLeaveToComplete();
                if(completedSheetEnabled) {
                    showSheet(getCompletedBottomSheet());
                }
            }
        }
    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE); // JUST TO MAKE SURE
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
            }
        });
    }

    private JsonObject getPayload() {
        JsonObject object = new JsonObject();
        for(int n = 0; n < quizzesAdapter.quizzesList.size(); n++) {
            Quiz q = quizzesAdapter.quizzesList.get(n);
            JsonObject payload = q.getTrainingPayload(n);
            object.add(Long.toString(q.id), payload);
        }
        JsonObject output = new JsonObject();
        output.add("answers", object);
        return output;
    }
    private void sendTraining(DialogBottomSheet sheet) {
        sendTraining(sheet, false);
    }
    private void sendTraining(final DialogBottomSheet sheet, final Boolean goToDetails) {
        sheet.startLoading();
        RetrofitManager.getHttpTrainingEndpoint().answer(getPayload()).enqueue(new Callback<SuccessTraining>() {
            @Override
            public void onResponse(Call<SuccessTraining> call, Response<SuccessTraining> response) {
                sheet.stopLoading();
                if(response.isSuccessful() && response.isSuccessful() && response.body().success) {
                    if(goToDetails) {
                        Intent i = new Intent(TrainActivity.this, TrainingDetailsActivity.class);
                        i.putExtra(getString(R.string.training_details_extra_training_key), RetrofitManager.gson.toJson(response.body().training));
                        startActivity(i);
                    }
                    finish();
                } else {
                    sendFailure(sheet);
                }
            }

            @Override
            public void onFailure(Call<SuccessTraining> call, Throwable t) {
                sheet.stopLoading();
                sendFailure(sheet);
            }
        });
    }


    @Override
    protected String getToolbarTitle(){ return ""; }
    @Override
    protected int getBackButtonVisibility(){
        return View.GONE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }
    private Button selectedButton;
    private ValuesCallback<Integer, Button> onButtonClick = new ValuesCallback<Integer, Button>() {
        @Override
        public void onReceiveValues(Integer position, Button b) {
            quizzesViewPager.setCurrentItem(position);
            if(selectedButton != null) TPPlayHandler.clearSelection(selectedButton);
            TPPlayHandler.setSelected(b);
            selectedButton = b;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        random = getRandomPreference();
        toolbarHandler = new TrainingToolbarHandler(toolbar, this, expirationHandler);
        gameOptions = (FragmentGameOptions) getSupportFragmentManager().findFragmentById(R.id.gameOptions);
        gameOptions.enableTrainMode(leaveGameCallback);
        panelAdapter = new QuizButtonAdapter(getBaseContext(), onButtonClick);
        quizzesViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getQuestions();
        loadAds();
    }
    private SimpleCallback expirationHandler = new SimpleCallback() {
        @Override
        public void execute() {
            showSheet(getExpiredBottomSheet());
        }
    };
    private void sendFailure(final DialogBottomSheet sheet) {
        Snackbar.make(findViewById(android.R.id.content), R.string.httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.httpConnectionErrorRetryButton, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendTraining(sheet);
                    }
                })
                .show();
    }
    private void loadFailure() {
        Snackbar.make(findViewById(android.R.id.content), R.string.httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.httpConnectionErrorRetryButton, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getQuestions();
                    }
                })
                .show();
    }
    private DialogBottomSheet getLeaveBottomSheet() {
        final DialogBottomSheet sheet = new DialogBottomSheet(trainingBottomSheet);
        int answeredQuestions = quizzesAdapter.numberOfAnswered();
        String content = getString(answeredQuestions == quizzesAdapter.quizzesList.size() ? R.string.training_leave_content_all : answeredQuestions == 1 ? R.string.training_leave_content_singular : R.string.training_leave_content_plural).replace("%d", "" + answeredQuestions);
        sheet.setContent(getString(R.string.training_leave_title), content);
        sheet.setFirstAction(R.string.training_leave_action1, DialogBottomSheet.ButtonType.BLUE, new SimpleCallback() {
            @Override
            public void execute() {
                sendTraining(sheet);
            }
        });
        sheet.setSecondAction(R.string.training_leave_action2, DialogBottomSheet.ButtonType.TRANSPARENT, new SimpleCallback() {
            @Override
            public void execute() {
                finish();
            }
        });
        return sheet;
    }
    private DialogBottomSheet getExpiredBottomSheet() {
        final DialogBottomSheet sheet = new DialogBottomSheet(trainingBottomSheet);
        int answeredQuestions = quizzesAdapter.numberOfAnswered();
        String content = getString(answeredQuestions == quizzesAdapter.quizzesList.size() ? R.string.training_expiration_content_all : answeredQuestions == 1 ? R.string.training_expiration_content_singular : R.string.training_expiration_content_plural).replace("%d", "" + answeredQuestions);
        sheet.setImage(R.drawable.timer_dark);
        sheet.setContent(getString(R.string.training_expiration_title), content);
        sheet.setFirstAction(R.string.training_expiration_action1, DialogBottomSheet.ButtonType.BLUE, new SimpleCallback() {
            @Override
            public void execute() {
                sendTraining(sheet);
            }
        });
        sheet.setSecondAction(R.string.training_expiration_action2, DialogBottomSheet.ButtonType.TRANSPARENT, new SimpleCallback() {
            @Override
            public void execute() {
                finish();
            }
        });
        return sheet;
    }
    private DialogBottomSheet getCompletedBottomSheet() {
        final DialogBottomSheet sheet = new DialogBottomSheet(trainingBottomSheet);
        String content = TPUtils.translateEmoticons(getString(R.string.training_completion_content));
        sheet.setImage(R.drawable.completed_icon);
        sheet.setContent(getString(R.string.training_completion_title), content);
        sheet.setFirstAction(R.string.training_completion_action1, DialogBottomSheet.ButtonType.BLUE, new SimpleCallback() {
            @Override
            public void execute() {
                sendTraining(sheet, true);
            }
        });
        sheet.setSecondAction(R.string.training_completion_action2, DialogBottomSheet.ButtonType.TRANSPARENT, new SimpleCallback() {
            @Override
            public void execute() {
                hideSheet(sheet);
                completedSheetEnabled = false;
            }
        });
        return sheet;
    }

    @Override
    public void onBackPressed() {
        getLeaveBottomSheet().show();
    }

    private Boolean completedSheetEnabled = true;
    private QuizButtonHolder getViewHolderFor(int position) {
        return panelAdapter.holders.get(position);
    }
    private Boolean isVisible(QuizButtonHolder holder) {
        int scrollLeft = quizScrollView.getScrollX();
        int scrollRight = scrollLeft + quizScrollView.getWidth();
        int left = TPUtils.getRelativeLeft(holder.itemView, quizPanel);
        int right = left + panelAdapter.getViewWidth();
        return left >= scrollLeft && right <= scrollRight;
    }
    private void selectButton(int position) {
        QuizButtonHolder holder = getViewHolderFor(position);
        if(!isVisible(holder)) {
            int scrollLeft = TPUtils.getRelativeLeft(holder.itemView, quizPanel);
            quizScrollView.smoothScrollTo(scrollLeft, 0);
        }
        holder.quizButton.callOnClick();
    }
    private void setAnsweredButton(int position) {
        QuizButtonHolder holder = getViewHolderFor(position);
        holder.setAnsweredButton();
    }
    private void populateQuizPanel() {
        for(int n = 0; n < panelAdapter.getCount(); n++) {
            View v = panelAdapter.getView(n, quizPanel, quizPanel);
            quizPanel.addView(v);
        }
    }
    private void getQuestions() {
        RetrofitManager.getHttpTrainingEndpoint().getQuestions(random).enqueue(new Callback<SuccessQuizzes>() {
            @Override
            public void onResponse(Call<SuccessQuizzes> call, Response<SuccessQuizzes> response) {
                if(response.isSuccessful() && response.isSuccessful() && response.body().success) {
                    List<Quiz> quizzes = response.body().quizzes;
                    quizzesAdapter = new QuizzesPagerAdapter(quizzes);
                    quizzesViewPager.setAdapter(quizzesAdapter);
                    panelAdapter.initData(quizzes);
                    populateQuizPanel();
                    int position = TPPlayHandler.getNextCurrentItem(quizzesAdapter, 0);
                    selectButton(position);
                    toolbarHandler.startTraining();
                } else {
                    loadFailure();
                }
            }

            @Override
            public void onFailure(Call<SuccessQuizzes> call, Throwable t) {
                loadFailure();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(toolbarHandler != null) toolbarHandler.cancel();
    }
}

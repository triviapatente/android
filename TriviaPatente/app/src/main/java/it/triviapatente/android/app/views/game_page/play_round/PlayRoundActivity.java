package it.triviapatente.android.app.views.game_page.play_round;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.baseActivityClasses.TPGameActivity;
import it.triviapatente.android.app.utils.baseActivityClasses.TPPlayHandler;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.app.views.game_page.GameMainPageActivity;
import it.triviapatente.android.app.views.game_page.round_details.RoundDetailsActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.responses.SuccessAnsweredCorrectly;
import it.triviapatente.android.models.responses.SuccessInitRound;
import it.triviapatente.android.models.responses.SuccessQuizzes;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;

public class PlayRoundActivity extends TPGameActivity implements View.OnClickListener {
    // ads
    @BindView(R.id.adView) AdView mAdView;

    // quizzes
    @BindView(R.id.quizzes) ViewPager quizzesViewPager;
    @BindViews({R.id.firstQuizButton, R.id.secondQuizButton, R.id.thirdQuizButton, R.id.fourthQuizButton}) List<Button> quizButtons;
    QuizzesPagerAdapter quizzesAdapter;
    // quiz button background management
    @DrawableRes int noAnswerRes = R.drawable.button_play_round_no_answer,
                     redRes = R.drawable.button_play_round_red,
                     greenRes = R.drawable.button_play_round_green;
    Drawable[] quizButtonsBackgrounds;
    // quiz send answer
    SocketCallback<SuccessAnsweredCorrectly> answerSocketCallback = new SocketCallback<SuccessAnsweredCorrectly>() {
        @Override
        public void response(final SuccessAnsweredCorrectly response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onReceiveValue(response.success);
                }
            });
            if(response.success) {
                final int position = quizzesViewPager.getCurrentItem();
                setButtonColorFromAnswer(position, response.correct_answer);
                Quiz currentQuiz = quizzesAdapter.quizzesList.get(position);
                currentQuiz.answered_correctly = response.correct_answer;
                quizzesAdapter.quizzesList.set(position, currentQuiz);
                final int nextPosition = TPPlayHandler.getNextCurrentItem(quizzesAdapter, position);
                if(nextPosition != position) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            quizzesViewPager.setCurrentItem(nextPosition);
                        }
                    });
                } else {
                    // my round ended
                    goToGameMainPage();

                }
            } else if(response.status_code == 403){
                goToGameMainPage();
            } else if(response.timeout) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.httpConnectionError), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

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
        setContentView(R.layout.activity_play_round);
        init();

        // load ads
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
                             finish();
                         }
                     });
                 }
            }
        });
    }

    private void init() {
        quizzesViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                quizButtons.get(position).callOnClick();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        loadQuizzes();
    }

    private void initQuizPanelButtons() {
        quizButtonsBackgrounds = new Drawable[quizButtons.size()];
        for(int position = 0; position < quizButtons.size(); position++) {
            Button b = quizButtons.get(position);
            b.setOnClickListener(this);
            Quiz quiz = quizzesAdapter.quizzesList.get(position);
            b.setText(String.valueOf(currentRound.number * 4 + position - 3)); // as (number - 1) * 4 + position + 1
            @DrawableRes int backgroundRes;
            if(quiz.my_answer == null) {
                backgroundRes = noAnswerRes;
            } else if(quiz.answered_correctly) {
                backgroundRes = greenRes;
            } else {
                backgroundRes = redRes;
            }
            quizButtonsBackgrounds[position] = ContextCompat.getDrawable(this, backgroundRes);
            b.setBackground(quizButtonsBackgrounds[position]);
        }
    }

    private void loadQuizzes() {
        gameSocketManager.get_questions(currentRound.game_id, currentRound.id, new SocketCallback<SuccessQuizzes>() {
            @Override
            public void response(SuccessQuizzes response) {
                if(response.success) {
                    final List<Quiz> quizzes = response.quizzes;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            quizzesAdapter = new QuizzesPagerAdapter(quizzes, currentRound, currentCategory, opponent);
                            quizzesViewPager.setAdapter(quizzesAdapter);
                            initQuizPanelButtons();
                            int position = TPPlayHandler.getNextCurrentItem(quizzesAdapter, 0);
                            quizButtons.get(position).callOnClick();
                            quizzesViewPager.setCurrentItem(position);
                            TPPlayHandler.setSelected(quizButtons.get(position));
                        }
                    });
                } else {
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadQuizzes();
                                }
                            })
                            .show();
                }
            }
        });
    }

    int lastSelectedButtonPosition = -1;
    @Override
    public void onClick(View v) {
        final int position;
        switch (v.getId()) {
            case R.id.firstQuizButton: position = 0; break;
            case R.id.secondQuizButton: position = 1; break;
            case R.id.thirdQuizButton: position = 2; break;
            case R.id.fourthQuizButton: position = 3; break;
            default: return;
        }
        if(lastSelectedButtonPosition == position) return;
        quizzesViewPager.setCurrentItem(position);
        if(lastSelectedButtonPosition != -1) {
            quizButtons.get(lastSelectedButtonPosition).setBackground(quizButtonsBackgrounds[lastSelectedButtonPosition]);
            TPPlayHandler.clearSelection(quizButtons.get(lastSelectedButtonPosition));
        }
        TPPlayHandler.setSelected(quizButtons.get(position));
        lastSelectedButtonPosition = position;
    }

    private void setButtonColorFromAnswer(final int position, final boolean isCorrect) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                @DrawableRes int backgroundRes, backgroundSelectedRes;
                if(isCorrect) {
                    backgroundRes = greenRes;
                } else {
                    backgroundRes = redRes;
                }
                quizButtonsBackgrounds[position] = ContextCompat.getDrawable(
                        PlayRoundActivity.this, backgroundRes);
            }
        });
    }
    private ValueCallback<Boolean> responseCallback;

    public void sendAnswer(boolean answer, long quiz_id, ValueCallback<Boolean> responseCb) {
        responseCallback = responseCb;
        int position = quizzesViewPager.getCurrentItem();
        Quiz currentQuiz = quizzesAdapter.quizzesList.get(position);
        currentQuiz.my_answer = answer;
        quizzesAdapter.quizzesList.set(position, currentQuiz);
        gameSocketManager.answer(currentRound.game_id, currentRound.id, quiz_id, answer, answerSocketCallback);
    }

}

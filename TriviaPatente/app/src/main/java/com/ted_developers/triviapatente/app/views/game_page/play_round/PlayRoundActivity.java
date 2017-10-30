package com.ted_developers.triviapatente.app.views.game_page.play_round;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.views.game_page.GameMainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.responses.SuccessAnsweredCorrectly;
import com.ted_developers.triviapatente.models.responses.SuccessQuizzes;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;

public class PlayRoundActivity extends TPGameActivity implements View.OnClickListener {
    // quizzes
    @BindView(R.id.quizzes) ViewPager quizzesViewPager;
    @BindViews({R.id.firstQuizButton, R.id.secondQuizButton, R.id.thirdQuizButton, R.id.fourthQuizButton}) List<Button> quizButtons;
    QuizzesPagerAdapter quizzesAdapter;
    // quiz button background management
    @DrawableRes int noAnswerSelectedRes = R.drawable.button_play_round_no_answer_selected, noAnswerRes = R.drawable.button_play_round_no_answer,
                     redRes = R.drawable.button_play_round_red, redSelectedRes = R.drawable.button_play_round_red_selected,
                     greenRes = R.drawable.button_play_round_green, greenSelectedRes = R.drawable.button_play_round_green_selected;
    Pair<Drawable, Drawable>[] quizButtonsBackgrounds;
    // quiz send answer
    SocketCallback<SuccessAnsweredCorrectly> answerSocketCallback = new SocketCallback<SuccessAnsweredCorrectly>() {
        @Override
        public void response(SuccessAnsweredCorrectly response) {
            if(response.success) {
                final int position = quizzesViewPager.getCurrentItem();
                setButtonColorFromAnswer(position, response.correct_answer);
                Quiz currentQuiz = quizzesAdapter.quizzesList.get(position);
                currentQuiz.answered_correctly = response.correct_answer;
                quizzesAdapter.quizzesList.set(position, currentQuiz);
                final int nextPosition = getNextCurrentItem(position);
                if(nextPosition != position) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            quizzesViewPager.setCurrentItem(nextPosition);
                        }
                    });
                } else {
                    // my round ended
                    Intent intent = new Intent(PlayRoundActivity.this, GameMainPageActivity.class);
                    intent.putExtra(getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
                    intent.putExtra(getString(R.string.extra_long_game), gameID);
                    intent.putExtra(getString(R.string.extra_boolean_join_room), false);
                    startActivity(intent);
                    finish();
                }

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
        quizButtonsBackgrounds = new Pair[quizButtons.size()];
        for(int position = 0; position < quizButtons.size(); position++) {
            Button b = quizButtons.get(position);
            b.setOnClickListener(this);
            Quiz quiz = quizzesAdapter.quizzesList.get(position);
            b.setText(String.valueOf(currentRound.number * 4 + position - 3)); // as (number - 1) * 4 + position + 1
            @DrawableRes int backgroundRes, backgroundSelectedRes;
            if(quiz.my_answer == null) {
                backgroundRes = noAnswerRes;
                backgroundSelectedRes = noAnswerSelectedRes;
            } else if(quiz.answered_correctly == true) {
                backgroundRes = greenRes;
                backgroundSelectedRes = greenSelectedRes;
            } else {
                backgroundRes = redRes;
                backgroundSelectedRes = redSelectedRes;
            }
            Pair<Drawable, Drawable> backgroundPair = new Pair<>(ContextCompat.getDrawable(this, backgroundRes), ContextCompat.getDrawable(this, backgroundSelectedRes));
            quizButtonsBackgrounds[position] = backgroundPair;
            b.setBackground(backgroundPair.first);
        }
    }

    private void loadQuizzes() {
        gameSocketManager.get_questions(currentRound.game_id, currentRound.id, new SocketCallback<SuccessQuizzes>() {
            @Override
            public void response(SuccessQuizzes response) {
                final List<Quiz> quizzes = response.quizzes;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        quizzesAdapter = new QuizzesPagerAdapter(quizzes);
                        quizzesViewPager.setAdapter(quizzesAdapter);
                        initQuizPanelButtons();
                        int position = getNextCurrentItem(0);
                        quizButtons.get(position).callOnClick();
                        quizzesViewPager.setCurrentItem(position);
                    }
                });
            }
        });
    }

    Integer lastSelectedButtonPosition = null;
    @Override
    public void onClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.firstQuizButton: position = 0; break;
            case R.id.secondQuizButton: position = 1; break;
            case R.id.thirdQuizButton: position = 2; break;
            case R.id.fourthQuizButton: position = 3; break;
            default: return;
        }
        quizzesViewPager.setCurrentItem(position);
        if(lastSelectedButtonPosition != null) {
            quizButtons.get(lastSelectedButtonPosition).setBackground(quizButtonsBackgrounds[lastSelectedButtonPosition].first);
        }
        lastSelectedButtonPosition = position;
        quizButtons.get(position).setBackground(quizButtonsBackgrounds[position].second);
    }

    private void setButtonColorFromAnswer(final int position, final boolean isCorrect) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                @DrawableRes int backgroundRes, backgroundSelectedRes;
                if(isCorrect) {
                    backgroundRes = greenRes;
                    backgroundSelectedRes = greenSelectedRes;
                } else {
                    backgroundRes = redRes;
                    backgroundSelectedRes = redSelectedRes;
                }
                Pair<Drawable, Drawable> backgroundPair = new Pair<>(ContextCompat.getDrawable(
                        PlayRoundActivity.this, backgroundRes), ContextCompat.getDrawable(PlayRoundActivity.this, backgroundSelectedRes)
                );
                quizButtonsBackgrounds[position] = backgroundPair;
                quizButtons.get(position).setBackground(backgroundPair.second);
            }
        });
    }

    public void sendAnswer(boolean answer, long quiz_id) {
        int position = quizzesViewPager.getCurrentItem();
        Quiz currentQuiz = quizzesAdapter.quizzesList.get(position);
        currentQuiz.my_answer = answer;
        quizzesAdapter.quizzesList.set(position, currentQuiz);
        gameSocketManager.answer(currentRound.game_id, currentRound.id, quiz_id, answer, answerSocketCallback);
    }

    private int getNextCurrentItem(int position) {
        int counter = 0, numberOfItems = quizzesAdapter.quizzesList.size();
        for(int nextPos = position; counter < numberOfItems; counter++) {
            int nextPosition = (nextPos + counter) % numberOfItems;
            if(quizzesAdapter.quizzesList.get(nextPosition).my_answer == null) {
                return nextPosition;
            }
        }
        return position;
    }
}

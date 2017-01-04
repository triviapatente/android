package com.ted_developers.triviapatente.app.views.game_page.play_round;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPActionBar;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPDialog;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessAnsweredCorrectly;
import com.ted_developers.triviapatente.models.responses.SuccessQuizzes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class PlayRoundActivity extends TPActivity implements View.OnClickListener {
    // game data
    User opponent;
    Round currentRound;
    Category currentCategory;
    // action bar
    @BindView(R.id.toolbar) BackPictureTPActionBar toolbar;
    // game header
    @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    @BindView(R.id.subtitleImage) ImageView gameHeaderSubtitleImage;
    // quizzes
    @BindView(R.id.quizzes) ViewPager quizzesViewPager;
    @BindViews({R.id.firstQuizButton, R.id.secondQuizButton, R.id.thirdQuizButton, R.id.fourthQuizButton}) List<Button> quizButtons;
    QuizzesPagerAdapter quizzesAdapter;
    // quiz button background management
    @DrawableRes int noAnswerSelectedRes = R.drawable.button_play_round_no_answer_selected, noAnswerRes = R.drawable.button_play_round_no_answer,
                     redRes = R.drawable.button_play_round_red, redSelectedRes = R.drawable.button_play_round_red_selected,
                     greenRes = R.drawable.button_play_round_green, greenSelectedRes = R.drawable.button_play_round_green_selected;
    // leave game
    TPDialog leaveDialog;
    @BindView(R.id.fullBlurredView) ImageView blurredBackgroundView;
    @BindView(R.id.fullBlurredContainer) RelativeLayout blurredBackgroundContainer;
    @BindView(R.id.game_container) RelativeLayout gameContainer;
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        quizzesViewPager.setCurrentItem(getNextCurrentItem(position));
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);
        init();
    }

    private void initLeaveDialog() {
        leaveDialog = new TPDialog(this, R.layout.modal_view_logout, 0, false, null) {
            @Override
            public void onNegativeButtonClick() {

            }

            @Override
            public void onPositiveButtonClick() {
                blurredBackgroundContainer.setVisibility(View.GONE);
                dismiss();
            }
        };
    }

    private void init() {
        initLeaveDialog();
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_round)), Round.class);
        currentCategory = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_category)), Category.class);
        initActionbar();
        initGameHeader();
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

    private void initActionbar() {
        // title
        if(opponent != null && toolbar.getTitle().equals("")) {
            if(opponent.name == null || opponent.surname == null) {
                toolbar.setTitle(opponent.username);
            } else {
                toolbar.setTitle(opponent.name + " " + opponent.surname);
            }
        }
        // profile picture
        // todo do dinamically
        toolbar.setProfilePicture(ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture));
    }

    private void initGameHeader() {
        // game header title
        gameHeaderTitle.setText("Round " + currentRound.number);
        // game header subtitle
        gameHeaderSubtitle.setText(currentCategory.hint);
        // todo set game header subtitle image dinamically
        gameHeaderSubtitleImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture));
        gameHeaderSubtitleImage.setVisibility(View.VISIBLE);
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

    // option button panel
    @OnClick(R.id.gameChatButton)
    public void gameChatButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @OnClick(R.id.gameDetailsButton)
    public void gameDetailsButtonClick() {
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
    }
    @OnClick(R.id.gameLeaveButton)
    public void gameLeaveButtonClick() {
        TPUtils.blurContainerIntoImageView(PlayRoundActivity.this, gameContainer, blurredBackgroundView);
        blurredBackgroundContainer.setVisibility(View.VISIBLE);
        //showing modal
        leaveDialog.show();
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
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

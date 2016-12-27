package com.ted_developers.triviapatente.app.views.game_page.play_round;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPActionBar;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.SuccessQuizzes;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayRoundActivity extends TPActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_round)), Round.class);
        currentCategory = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_category)), Category.class);
        initActionbar();
        initGameHeader();
        initQuizPanelButtons();
        initBackgroundManager();
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
        toolbar.setProfilePicture(ContextCompat.getDrawable(this, R.drawable.no_image));
    }

    private void initGameHeader() {
        // game header title
        gameHeaderTitle.setText("Round " + currentRound.number);
        // game header subtitle
        gameHeaderSubtitle.setText(currentCategory.hint);
        // todo set game header subtitle image dinamically
        gameHeaderSubtitleImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.no_image));
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
                        quizzesViewPager.setAdapter(new QuizzesPagerAdapter(quizzes));
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
        Intent intent = new Intent(this, AlphaView.class);
        startActivity(intent);
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

    Button lastSelectedButton;
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
        if(lastSelectedButton != null) {
            lastSelectedButton.setBackground(quizButtonsBackgroundsManager.getOther(lastSelectedButton.getBackground()));
        }
        lastSelectedButton = quizButtons.get(position);
        lastSelectedButton.setBackground(quizButtonsBackgroundsManager.getOther(lastSelectedButton.getBackground()));
    }

    private class QuizButtonsBackgroundsManager {
        public List<Pair<Drawable, Drawable>> backgrounds = new ArrayList<>();
        public Drawable getOther(Drawable drawable) {
            for(Pair<Drawable, Drawable> pair : backgrounds) {
                if(pair.first.equals(drawable)) {
                    return pair.second;
                } else if(pair.second.equals(drawable)) {
                    return pair.first;
                }
            }
            return null;
        }
    }

    private void setButtonColorFromAnswer(Button button, boolean isCorrect) {
        Drawable backgroundDrawable = null;
        if(isCorrect) {
            backgroundDrawable = greenDrawableSelected;
        } else {
            backgroundDrawable = redDrawableSelected;
        }
        button.setBackground(backgroundDrawable);
    }

    public void sendAnswer(boolean answer) {

    }
}

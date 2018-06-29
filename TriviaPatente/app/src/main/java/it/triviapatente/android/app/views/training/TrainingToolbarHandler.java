package it.triviapatente.android.app.views.training;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.views.game_page.play_round.QuizzesPagerAdapter;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 28/06/18.
 */

public class TrainingToolbarHandler {
    @BindView(R.id.trainingProgress) ProgressBar progressBar;
    @BindView(R.id.trainingTimerTextView) TextView timerView;
    private View contentView;
    private Activity context;
    private SimpleCallback expirationHandler;
    private Timer timer;
    private Date startDate;
    private long time = 40 * 60 * 1000; //40 minutes
    private Boolean freezed = false;
    private Date lastFreezeDate;
    public TrainingToolbarHandler(View v, Activity context, SimpleCallback expirationHandler) {
        this.contentView = v;
        this.context = context;
        this.expirationHandler = expirationHandler;
        ButterKnife.bind(this, contentView);
    }
    public void freezeTimer() {
        freezed = true;
        lastFreezeDate = new Date();
    }
    public void resumeTimer() {
        if(lastFreezeDate != null) {
            long freezeTime = new Date().getTime() - lastFreezeDate.getTime();
            startDate.setTime(startDate.getTime() + freezeTime);
        }
        freezed = false;
    }
    public void cancel() {
        if(timer != null) timer.cancel();
    }
    private DateFormat getTimeDateFormat() {
        return new SimpleDateFormat("mm:ss", Locale.ITALIAN);
    }
    private void redrawTime(long remainingTime) {
        Date remaining = new Date(remainingTime);
        String time = getTimeDateFormat().format(remaining);
        timerView.setText(time);
    }
    public void startTraining() {
        timer = new Timer();
        startDate = new Date();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(freezed) return;
                long millisecs = new Date().getTime() - startDate.getTime();
                final long remaining = time - millisecs;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(remaining <= 0) {
                            expirationHandler.execute();
                            cancel();
                        } else {
                            redrawTime(remaining);
                        }
                    }
                });
            }
        }, 0, 1000);
    }
    public void updateProgress(QuizzesPagerAdapter adapter) {
        int size = adapter.quizzesList.size();
        int numberOfAnswers = adapter.numberOfAnswered();
        int progress = numberOfAnswers * 100 / size;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }
}

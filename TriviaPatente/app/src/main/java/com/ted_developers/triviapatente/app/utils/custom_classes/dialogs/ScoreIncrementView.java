package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 04/01/17.
 */
public class ScoreIncrementView extends LinearLayout {
    private TextView scoreIncTextView;
    private ImageView scoreIncImageView;

    public ScoreIncrementView(Context context) {
        super(context);
        init();
    }

    public ScoreIncrementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScoreIncrementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScoreIncrementView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_score_increment_view, this);
        scoreIncImageView = (ImageView) findViewById(R.id.scoreIncImageView);
        scoreIncTextView = (TextView) findViewById(R.id.scoreIncTextView);
        setScoreInc(20);
    }

    public void setScoreInc(int scoreInc) {
        Context context = getContext();
        if(scoreInc == 0) {
            setVisibility(GONE);
        } else if(scoreInc > 0) {
            scoreIncTextView.setText("+" + scoreInc);
            scoreIncImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_positive_score_increment));
        } else {
            scoreIncTextView.setText(String.valueOf(scoreInc));
            scoreIncImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_negative_score_increment));
        }
    }
}

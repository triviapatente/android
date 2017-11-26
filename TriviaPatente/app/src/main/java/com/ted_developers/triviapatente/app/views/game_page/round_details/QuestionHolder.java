package com.ted_developers.triviapatente.app.views.game_page.round_details;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.game.Quiz;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by donadev on 23/11/17.
 */

public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.quizImageView) ImageView quizImageView;
    @BindView(R.id.quizNameView) TextView quizNameView;
    @BindView(R.id.quizTrueView) TextView quizTrueView;
    @BindView(R.id.quizFalseView) TextView quizFalseView;
    @BindViews({R.id.quizFirstTrueUser, R.id.quizSecondTrueUser}) RoundedImageView[] quizTrueImages;
    @BindViews({R.id.quizFirstFalseUser, R.id.quizSecondFalseUser}) RoundedImageView[] quizFalseImages;

    @BindDimen(R.dimen.quiz_image_size_small) int quizDefaultImageDimen;
    @BindDimen(R.dimen.details_quiz_name_margin) int quizNameViewMargin;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;

    private Context context;
    private User currentUser = SharedTPPreferences.currentUser();
    private User opponent;

    private int cellHeight;

    public static QuestionHolder newHolder(Context context, int cellHeight) {
        View v = LayoutInflater.from(context).inflate(R.layout.gamedetails_quiz_view_holder, null, false);
        return new QuestionHolder(context, v, cellHeight);

    }
    public QuestionHolder(Context context, View itemView, int cellHeight) {
        super(itemView);
        this.context = context;
        this.cellHeight = cellHeight;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void setQuizImageWidth(int width) {
        ViewGroup.LayoutParams params = quizImageView.getLayoutParams();
        params.width = width;
        quizImageView.setLayoutParams(params);
    }

    public void bind(Quiz quiz, User opponent) {
        this.opponent = opponent;
        Picasso.with(context).cancelRequest(quizImageView);
        if(quiz.image_id != null) {
            setQuizImageWidth(quizDefaultImageDimen);
            String path = TPUtils.getQuizImageFromID(context, quiz.image_id);
            Picasso.with(context).load(path).into(quizImageView);
        } else {
            setQuizImageWidth(0);
        }
        quizNameView.setText(quiz.question);
        if(quiz.answer != null) {
            if(quiz.answer) {
                correctGUI(quizTrueView);
                incorrectGUI(quizFalseView);
            } else {
                incorrectGUI(quizTrueView);
                correctGUI(quizFalseView);
            }
        } else {
            incorrectGUI(quizTrueView);
            incorrectGUI(quizFalseView);
        }
        processAnswers(quiz.answers, quiz);
        setExpanded(false);
    }
    public void correctGUI(TextView view) {
        view.setBackgroundResource(R.drawable.details_circle_correct_background);
        view.setTextColor(mainColor);
    }
    public void incorrectGUI(TextView view) {
        view.setBackgroundResource(R.drawable.details_circle_incorrect_background);
        view.setTextColor(Color.WHITE);
    }
    public void processAnswers(List<Question> answers, Quiz quiz) {
        List<User> trueUsers = new ArrayList<>();
        List<User> falseUsers = new ArrayList<>();
        for(Question q : answers) {
            Boolean answer = q.answer != null ? q.answer : q.correct ? quiz.answer : !quiz.answer;
            if(answer) {
                if(q.user_id == currentUser.id) {
                    trueUsers.add(0, currentUser);
                } else {
                    trueUsers.add(opponent);
                }
            } else {
                if(q.user_id == currentUser.id) {
                    falseUsers.add(0, currentUser);
                } else {
                    falseUsers.add(opponent);
                }
            }
        }
        for(int n = 0; n < quizTrueImages.length; n++) {
            quizTrueImages[n].setBorder(Color.WHITE, 2);
            if(n < trueUsers.size()) {
                TPUtils.injectUserImage(context, trueUsers.get(n), quizTrueImages[n]);
            } else {
                quizTrueImages[n].setImageBitmap(null);
            }
        }
        for(int n = 0; n < quizFalseImages.length; n++) {
            quizFalseImages[n].setBorder(Color.WHITE, 2);
            if(n < falseUsers.size()) {
                TPUtils.injectUserImage(context, falseUsers.get(n), quizFalseImages[n]);
            } else {
                quizFalseImages[n].setImageBitmap(null);
            }
        }
    }

    private Boolean isExpanded = false;

    @Override
    public void onClick(View v) {
        setExpanded(!isExpanded);
    }
    public void setExpanded(Boolean expanded) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if(params == null) params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        if(expanded) {

            float height = quizNameView.getLineHeight() * quizNameView.getLineCount() + quizNameViewMargin * 2;
            if(height > this.cellHeight)
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            params.height = this.cellHeight;
        }
        itemView.setLayoutParams(params);
        isExpanded = expanded;
    }
}


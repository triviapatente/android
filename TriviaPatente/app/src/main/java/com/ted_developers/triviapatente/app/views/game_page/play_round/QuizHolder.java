package com.ted_developers.triviapatente.app.views.game_page.play_round;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.animation.ResizeAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.animation.TranslateAnimation;
import com.ted_developers.triviapatente.models.game.Quiz;

/**
 * Created by Antonio on 24/12/16.
 */
public class QuizHolder implements View.OnClickListener{
    private ImageView quizImage;
    private boolean isImageBig = false;
    private AnimationSet toSmall, toBig;
    private int animDuration = 200, imageSmallSize, imageBigSize;
    private TextView quizDescription;
    private Button trueButton, falseButton;
    private View itemView;
    private Context context;
    private Quiz element;
    private LinearLayout quizDescriptionBox;

    public QuizHolder(PlayRoundActivity context, Quiz quizElement) {
        itemView = LayoutInflater.from(context).inflate(R.layout.view_pager_element_quiz_holder, null, false);
        this.context = context;
        init();
        bind(quizElement);
    }

    private void init() {
        // binding elements
        quizImage = (ImageView) itemView.findViewById(R.id.quizImage);
        quizDescription = (TextView) itemView.findViewById(R.id.quizDescription);
        quizDescriptionBox = (LinearLayout) itemView.findViewById(R.id.quizDescriptionBox);
        trueButton = (Button) itemView.findViewById(R.id.trueButton);
        falseButton = (Button) itemView.findViewById(R.id.falseButton);
        // image resize and translate animation
        int dx = calculateXDelta();
        imageSmallSize = (int) context.getResources().getDimension(R.dimen.quiz_image_size_small);
        imageBigSize = (int) context.getResources().getDimension(R.dimen.quiz_image_size_big);
        toSmall = new AnimationSet(context, null);
        toSmall.addAnimation(new ResizeAnimation(quizImage, imageBigSize, imageBigSize, imageSmallSize, imageSmallSize));
        toSmall.addAnimation(new TranslateAnimation(quizImage, dx, 0, 0, 0));
        toSmall.setDuration(animDuration);
        toSmall.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) { quizDescription.setVisibility(View.VISIBLE); }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        toBig = new AnimationSet(context, null);
        toBig.addAnimation(new ResizeAnimation(quizImage, imageSmallSize, imageSmallSize, imageBigSize, imageBigSize));
        toBig.addAnimation(new TranslateAnimation(quizImage, 0, dx, 0, 0));
        toBig.setDuration(animDuration);
        toBig.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { quizDescription.setVisibility(View.GONE); }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        // setting elements
        quizDescription.setMovementMethod(new ScrollingMovementMethod()); // to allow scroll
        quizImage.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
    }

    private int calculateXDelta() {
        return (quizDescriptionBox.getMeasuredWidth() - imageBigSize) / 2;
    }

    public void bind(Quiz element) {
        this.element = element;
        quizDescription.setText(element.question);
        if(element.image_id == null) {
            quizImage.setVisibility(View.GONE);
        } else {
            quizImage.setVisibility(View.VISIBLE);
            // todo set dinamically
            quizImage.setImageDrawable(itemView.getResources().getDrawable(R.drawable.image_no_profile_picture));
        }
        // if already answered
        if(element.my_answer != null) {
            alreadyAnswered(element.my_answer);
        }
    }

    public void alreadyAnswered(boolean answer) {
        if(answer) {
            setButtonClicked(trueButton);
        } else {
            setButtonClicked(falseButton);
        }
        setButtonClickable(false);
    }

    @Override
    public void onClick(View v) {
        boolean answer;
        Button clicked;
        switch (v.getId()) {
            case R.id.trueButton: { answer = true; clicked = trueButton; } break;
            case R.id.falseButton: { answer = false; clicked = falseButton; } break;
            case R.id.quizImage: {
                changeImageSize();
            }
            default:return;
        }
        ((PlayRoundActivity) context).sendAnswer(answer, element.id);
        setButtonClicked(clicked);
        // disable clicks
        setButtonClickable(false);
    }

    private void changeImageSize() {
        if(isImageBig) {
            quizImage.startAnimation(toSmall);
            isImageBig = false;
        } else {
            quizImage.startAnimation(toBig);
            isImageBig = true;
        }
    }

    private void setButtonClicked(Button clicked) {
        clicked.setBackground(ContextCompat.getDrawable(context, R.drawable.button_true_or_false_clicked));
        clicked.setTextColor(Color.WHITE);
    }

    private void setButtonClickable(boolean clickable) {
        trueButton.setClickable(clickable);
        falseButton.setClickable(clickable);
    }

    public View getItemView() {
        return itemView;
    }
}

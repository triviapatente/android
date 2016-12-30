package com.ted_developers.triviapatente.app.views.game_page.play_round;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.models.game.Quiz;

/**
 * Created by Antonio on 24/12/16.
 */
public class QuizHolder implements View.OnClickListener{
    private ImageView quizImage;
    private TextView quizDescription;
    private Button trueButton, falseButton;
    private View itemView;
    private Context context;
    private Quiz element;

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
        trueButton = (Button) itemView.findViewById(R.id.trueButton);
        falseButton = (Button) itemView.findViewById(R.id.falseButton);
        // setting elements
        quizDescription.setMovementMethod(new ScrollingMovementMethod()); // to allow scroll
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        quizImage.setOnClickListener(this);
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
            case R.id.trueButton: answer = true; clicked = trueButton; break;
            case R.id.falseButton: answer = false; clicked = falseButton; break;
            case R.id.quizImage: {
                // todo do bigger
            }
            default:return;
        }
        ((PlayRoundActivity)context).sendAnswer(answer, element.id);
        setButtonClicked(clicked);
        // disable clicks
        setButtonClickable(false);
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

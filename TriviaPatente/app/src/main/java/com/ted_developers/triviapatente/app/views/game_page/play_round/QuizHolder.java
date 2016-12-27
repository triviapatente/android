package com.ted_developers.triviapatente.app.views.game_page.play_round;

import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.models.game.Quiz;

/**
 * Created by Antonio on 24/12/16.
 */
public class QuizHolder implements View.OnClickListener{
    private ImageView quizImage;
    private TextView quizDescription;
    private Button trueButton, falseButton;
    private View itemView;

    public QuizHolder(Context context, Quiz quizElement) {
        itemView = LayoutInflater.from(context).inflate(R.layout.quiz, null, false);
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
        quizDescription.setText(element.question);
        if(element.image_id != null) {
            quizImage.setVisibility(View.GONE);
        } else {
            quizImage.setVisibility(View.VISIBLE);
            // todo set dinamically
            quizImage.setImageDrawable(itemView.getResources().getDrawable(R.drawable.no_image));
        }
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
        // todo send answer
        // todo change color of selected button
        //clicked.setBackground();
        clicked.setTextColor(Color.WHITE);
        // disable clicks
        trueButton.setClickable(false);
        falseButton.setClickable(false);
        // todo go to next
    }

    public View getItemView() {
        return itemView;
    }
}

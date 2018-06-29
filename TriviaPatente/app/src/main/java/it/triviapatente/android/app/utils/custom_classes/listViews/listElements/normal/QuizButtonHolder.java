package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.custom_classes.callbacks.QuizSheetCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.ValuesCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.TPHolder;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 29/06/18.
 */

public class QuizButtonHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.quizButton) public Button quizButton;

    public static QuizButtonHolder newHolder(Context context, ValuesCallback<Integer, Button> onClickListener) {
        View v = LayoutInflater.from(context).inflate(R.layout.quiz_button_cell_item, null, false);
        return new QuizButtonHolder(v, onClickListener);

    }
    public void setAnsweredButton() {
        quizButton.setBackgroundResource(R.drawable.button_play_round_orange_selected);
    }
    public void setUnAnsweredButton() {
        quizButton.setBackgroundResource(R.drawable.button_play_round_no_answer_selected);
    }
    public QuizButtonHolder(View v, final ValuesCallback<Integer, Button> onClickListener) {
        super(v);
        ButterKnife.bind(this, v);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer element = (Integer)view.getTag();
                if(onClickListener != null) onClickListener.onReceiveValues(element, quizButton);
            }
        });
    }
    public void bind(Integer element, Quiz q) {
        quizButton.setText("" + ( 1 + element));
        quizButton.setTag(element);
        if(q.my_answer != null) setAnsweredButton();
        else setUnAnsweredButton();
    }
}

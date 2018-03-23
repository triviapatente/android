package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Question;
import it.triviapatente.android.models.game.Quiz;

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

public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.quizImageView) ImageView quizImageView;
    @BindView(R.id.quizNameView) TextView quizNameView;
    @BindView(R.id.quizTrueView) TextView quizTrueView;
    @BindView(R.id.quizFalseView) TextView quizFalseView;
    @BindView(R.id.answerImageLayout) LinearLayout userAnswersLayout;
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
        quizNameView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void setQuizImageWidth(int width) {
        ViewGroup.LayoutParams params = quizImageView.getLayoutParams();
        params.width = width;
        quizImageView.setLayoutParams(params);
    }
    public void noUserAnswersLayout() {
        ViewGroup.LayoutParams params = userAnswersLayout.getLayoutParams();
        params.width = 50; //bordo
        userAnswersLayout.setLayoutParams(params);
    }
    private void setMaxLines() {
        String text = quizNameView.getText().toString();
        int lines = TPUtils.getMaxLinesFor(text, quizNameView, this.cellHeight - quizNameViewMargin * 2);
        quizNameView.setMaxLines(lines);
    }
    private void unsetMaxLines() {
        quizNameView.setMaxLines(Integer.MAX_VALUE);
    }
    public int getBackgroundColorForTraining(Quiz quiz) {
        if (quiz.my_answer != quiz.answer) {
            return ContextCompat.getColor(context, R.color.wrongQuizTrainingBackgroundColor);
        }
        return ContextCompat.getColor(context, android.R.color.transparent);
    }
    public void bindForTraining(Quiz quiz) {
        noUserAnswersLayout();
        handleQuizImage(quiz);
        quizNameView.setText(quiz.question);
        handleAnswer(quiz.answer);
        itemView.setBackgroundColor(getBackgroundColorForTraining(quiz));
        setExpanded(true);
    }
    private void handleQuizImage(Quiz quiz) {
        TPUtils.picasso.cancelRequest(quizImageView);
        if(quiz.image_id != null) {
            setQuizImageWidth(quizDefaultImageDimen);
            String path = TPUtils.getQuizImageFromID(context, quiz.image_id);
            TPUtils.picasso.load(path).placeholder(R.drawable.placeholder).into(quizImageView);
        } else {
            setQuizImageWidth(0);
        }
    }
    private void handleAnswer(Boolean answer) {
        if(answer != null) {
            if(answer) {
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
    }
    public void bindForDetails(Quiz quiz, User opponent) {
        this.opponent = opponent;
        handleQuizImage(quiz);
        quizNameView.setText(quiz.question);
        handleAnswer(quiz.answer);
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
    private List<User> trueUsers = new ArrayList<>();
    private List<User> falseUsers = new ArrayList<>();
    private void answered(Quiz quiz, Question a) {
        if(quiz.answer == null) return;
        Boolean answer = a.correct ? quiz.answer : !quiz.answer;
        if(answer) {
            if(a.user_id.equals(currentUser.id)) {
                trueUsers.add(0, currentUser);
            } else {
                trueUsers.add(opponent);
            }
        } else {
            if(a.user_id.equals(currentUser.id)) {
                falseUsers.add(0, currentUser);
            } else {
                falseUsers.add(opponent);
            }
        }
    }
    public void processAnswers(List<Question> answers, Quiz quiz) {
        trueUsers = new ArrayList<>();
        falseUsers = new ArrayList<>();
        for(Question q : answers) {
            answered(quiz, q);
        }
        for(int n = 0; n < quizTrueImages.length; n++) {
            quizTrueImages[n].setBorder(Color.WHITE, 2);
            if(n < trueUsers.size()) {
                TPUtils.injectUserImage(context, trueUsers.get(n), quizTrueImages[n], false);
            } else {
                quizTrueImages[n].setImageBitmap(null);
            }
        }
        for(int n = 0; n < quizFalseImages.length; n++) {
            quizFalseImages[n].setBorder(Color.WHITE, 2);
            if(n < falseUsers.size()) {
                TPUtils.injectUserImage(context, falseUsers.get(n), quizFalseImages[n], false);
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

            float height = (quizNameView.getLineHeight() + TPUtils.getLineSpacing(quizNameView)) * quizNameView.getLineCount() + quizNameViewMargin * 2;
            if(height > this.cellHeight)
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            unsetMaxLines();
        } else {
            setMaxLines();
            params.height = this.cellHeight;
        }
        itemView.setLayoutParams(params);
        isExpanded = expanded;
    }

    @Override
    public void onGlobalLayout() {
        if(isExpanded) unsetMaxLines();
        else setMaxLines();
    }
}


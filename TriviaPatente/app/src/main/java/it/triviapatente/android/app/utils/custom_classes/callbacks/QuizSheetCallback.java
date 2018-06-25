package it.triviapatente.android.app.utils.custom_classes.callbacks;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 25/06/18.
 */

public class QuizSheetCallback implements ValueCallback<Quiz> {
    private View layout;
    @BindView(R.id.quizDetailImage) ImageView quizDetailImage;
    @BindView(R.id.quizDetailTextView) TextView quizDetailTextView;
    @BindView(R.id.quizDetailLayout) LinearLayout sheetLayout;

    public QuizSheetCallback(View source) {
        this.layout = source;
        ButterKnife.bind(this, layout);
    }
    private BottomSheetBehavior getBehavior() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) sheetLayout.getLayoutParams();
        return (BottomSheetBehavior) params.getBehavior();
    }
    private Context getContext() {
        return layout.getContext();
    }
    @Override
    public void onReceiveValue(Quiz quiz) {
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        quizDetailImage.setVisibility(quiz.image_id == null ? View.GONE : View.VISIBLE);
        if(quiz.image_id != null) {
            String imagePath = TPUtils.getQuizImageFromID(getContext(), quiz.image_id);
            TPUtils.picasso.load(imagePath).placeholder(R.drawable.placeholder).into(quizDetailImage);
        }
        quizDetailTextView.setText(quiz.question);
    }
}

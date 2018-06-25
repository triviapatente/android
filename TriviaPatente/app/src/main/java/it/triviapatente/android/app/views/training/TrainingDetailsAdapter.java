package it.triviapatente.android.app.views.training;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import it.triviapatente.android.app.utils.custom_classes.callbacks.QuizSheetCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuestionHolder;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.responses.SuccessQuizzes;

/**
 * Created by donadev on 23/03/18.
 */

public class TrainingDetailsAdapter extends RecyclerView.Adapter<QuestionHolder> {
    private Context context;
    private SuccessQuizzes response;
    private QuizSheetCallback cb;
    public TrainingDetailsAdapter(Context ctx, QuizSheetCallback cb) {
        this.context = ctx;
        this.cb = cb;
    }
    public void setResponse(SuccessQuizzes response) {
        this.response = response;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return QuestionHolder.newHolder(context, i, cb);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder questionHolder, int i) {
        Quiz q = response.quizzes.get(i);
        questionHolder.bindForTraining(q);
    }

    @Override
    public int getItemCount() {
        if(response == null) return 0;
        return response.quizzes.size();
    }
}

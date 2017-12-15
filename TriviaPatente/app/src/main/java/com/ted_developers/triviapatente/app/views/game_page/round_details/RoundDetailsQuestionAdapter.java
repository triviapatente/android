package com.ted_developers.triviapatente.app.views.game_page.round_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.GameEndedHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.QuestionHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.QuizHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.RoundHolder;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

import java.util.List;

/**
 * Created by donadev on 07/12/17.
 */

public class RoundDetailsQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_QUESTION = 1;
    private final int TYPE_GAME_ENDED = 2;

    private Context context;
    private RecyclerView answerList;
    public void setAnswerList(RecyclerView candidate) {
        this.answerList = candidate;
    }
    private SuccessRoundDetails response;
    private User opponent;

    public RoundDetailsQuestionAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(SuccessRoundDetails response, User opponent) {
        this.response = response;
        this.opponent = opponent;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_QUESTION)
            return QuestionHolder.newHolder(context, answerList.getHeight() / 4);
        else
            return GameEndedHolder.newHolder(context, answerList.getHeight());
    }

    @Override
    public int getItemViewType(int position) {
        if(position == response.quizzes.size()) return TYPE_GAME_ENDED;
        return TYPE_QUESTION;
    }


    public boolean up = false;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, up ? R.anim.down_from_top : R.anim.up_from_bottom);

        if(holder instanceof QuestionHolder) {
            Quiz quiz = response.quizzes.get(position);
            ((QuestionHolder)holder).bind(quiz, opponent);
        } else if(holder instanceof GameEndedHolder) {
            ((GameEndedHolder)holder).bind(response, opponent);
        }

        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        if(response != null && response.quizzes != null) {
            int increment = 0;
            if(response.game != null && response.game.ended) {
                increment += 1;
            }
            return response.quizzes.size() + increment;
        }
        return 0;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}

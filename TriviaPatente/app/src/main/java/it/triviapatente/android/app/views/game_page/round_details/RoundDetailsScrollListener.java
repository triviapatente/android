package it.triviapatente.android.app.views.game_page.round_details;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import it.triviapatente.android.app.utils.custom_classes.callbacks.RoundDetailsSectionCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuestionHolder;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizHolder;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.RoundHolder;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 16/02/18.
 */

public class RoundDetailsScrollListener extends RecyclerView.OnScrollListener {
    private int NUMBER_OF_QUESTIONS_PER_ROUND;
    private RoundDetailsQuestionAdapter answerAdapter;
    private RecyclerView sectionList;
    private LinearLayoutManager answerLayout;
    private RoundDetailsSectionCallback sectionListener;
    public RoundDetailsScrollListener(int questions_per_round, RecyclerView answerList, RecyclerView sectionList) {
        this.NUMBER_OF_QUESTIONS_PER_ROUND = questions_per_round;
        this.answerAdapter = (RoundDetailsQuestionAdapter) answerList.getAdapter();
        this.answerLayout = (LinearLayoutManager) answerList.getLayoutManager();
        this.sectionList = sectionList;
    }
    public void refreshAdapter(RoundDetailsQuestionAdapter adapter) {
        this.answerAdapter = adapter;
    }
    public void setRoundDetailsSectionCallback(RoundDetailsSectionCallback cb) {
        this.sectionListener = cb;
    }
    private int roundPosition;
    static final int scrollTollerance = 10;
    public void updateRoundPosition(int target) {
        this.roundPosition = target / NUMBER_OF_QUESTIONS_PER_ROUND;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(dy < scrollTollerance && dy > -scrollTollerance ) return;
        answerAdapter.up = dy < 0;
        int position = (dy < 0)? answerLayout.findFirstVisibleItemPosition() : answerLayout.findLastVisibleItemPosition();
        if(position != -1) {
            roundPosition = position / NUMBER_OF_QUESTIONS_PER_ROUND;
            RoundHolder holder = (RoundHolder) sectionList.findViewHolderForAdapterPosition(roundPosition);
            if (holder != null) holder.select(false);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if(newState == RecyclerView.SCROLL_STATE_IDLE)
            sectionListener.onSelected(roundPosition, true);
    }
}

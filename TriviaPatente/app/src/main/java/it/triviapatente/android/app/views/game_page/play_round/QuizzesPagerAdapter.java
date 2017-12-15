package it.triviapatente.android.app.views.game_page.play_round;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizHolder;
import it.triviapatente.android.models.game.Quiz;

import java.util.List;

/**
 * Created by Antonio on 27/12/16.
 */
public class QuizzesPagerAdapter extends PagerAdapter {

    public List<Quiz> quizzesList;

    public QuizzesPagerAdapter(List<Quiz> quizzesList) {
        this.quizzesList = quizzesList;
    }

    @Override
    public int getCount() {
        return quizzesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        QuizHolder quizHolder = new QuizHolder((PlayRoundActivity) container.getContext(), quizzesList.get(position));
        View itemView = quizHolder.getItemView();
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
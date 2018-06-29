package it.triviapatente.android.app.views.game_page.play_round;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizHolder;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.game.Round;

import java.util.List;

/**
 * Created by Antonio on 27/12/16.
 */
public class QuizzesPagerAdapter extends PagerAdapter {

    public List<Quiz> quizzesList;
    private Round round;
    private Category category;
    private User opponent;
    private Boolean trainMode;

    public QuizzesPagerAdapter(List<Quiz> quizzesList, Round round, Category category, User opponent) {
        this.quizzesList = quizzesList;
        this.round = round;
        this.category = category;
        this.opponent = opponent;
        this.trainMode = false;
    }

    public int numberOfAnswered() {
        int n = 0;
        for(Quiz q : quizzesList) if(q.my_answer != null) n++;
        return n;
    }
    public int find(Long id) {
        for(int n = 0; n <  quizzesList.size(); n++) {
            if(quizzesList.get(n).id.equals(id)) return n;
        }
        return -1;
    }
    public QuizzesPagerAdapter(List<Quiz> quizzesList) {
        this.quizzesList = quizzesList;
        this.trainMode = true;
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
        TPActivity activity = (TPActivity) container.getContext();
        Quiz quiz = quizzesList.get(position);
        QuizHolder quizHolder = new QuizHolder(activity, quiz, round, trainMode ? quiz.getCategory() : category, opponent, trainMode);
        View itemView = quizHolder.getItemView();
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

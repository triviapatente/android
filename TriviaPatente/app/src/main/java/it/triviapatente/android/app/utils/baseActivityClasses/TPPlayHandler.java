package it.triviapatente.android.app.utils.baseActivityClasses;

import android.support.v4.view.ViewCompat;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;

import it.triviapatente.android.app.views.game_page.play_round.QuizzesPagerAdapter;

/**
 * Created by donadev on 28/06/18.
 */

public class TPPlayHandler {
    QuizzesPagerAdapter quizzesAdapter = null;

    public static int getNextCurrentItem(QuizzesPagerAdapter adapter, int position) {
        int counter = 0, numberOfItems = adapter.quizzesList.size();
        for(int nextPos = position; counter < numberOfItems; counter++) {
            int nextPosition = (nextPos + counter) % numberOfItems;
            if(adapter.quizzesList.get(nextPosition).my_answer == null) {
                return nextPosition;
            }
        }
        return position;
    }

    private final static float BIG_SCALE = 1.3f;
    private final static float DEFAULT_SCALE = 1;
    private final static int BIG_ELEVATION = 2;
    private final static int DEFAULT_ELEVATION = 0;
    private final static int DURATION = 300;
    public static void setSelected(Button quizButton) {
        ViewCompat.setElevation(quizButton, BIG_ELEVATION);
        quizButton.animate().scaleX(BIG_SCALE).scaleY(BIG_SCALE).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(DURATION);
    }
    public static void clearSelection(Button quizButton) {
        ViewCompat.setElevation(quizButton, DEFAULT_ELEVATION);
        quizButton.animate().scaleX(DEFAULT_SCALE).scaleY(DEFAULT_SCALE).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(DURATION);
    }
}

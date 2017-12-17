package it.triviapatente.android.app.views.game_page.round_details;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.models.responses.SuccessRoundDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGameDetailsScore extends Fragment {
    @BindView(R.id.roundDetailsUser) public RoundedImageView userImageView;
    @BindView(R.id.roundDetailsOpponent) public RoundedImageView opponentImageView;
    @BindView(R.id.roundDetailsUserScore) public TextView userScoreView;
    @BindView(R.id.roundDetailsOpponentScore) public TextView opponentScoreView;

    private List<Question> answers = new ArrayList<>();
    private User opponent;
    private User currentUser = SharedTPPreferences.currentUser();
    private SuccessRoundDetails response;

    public void setResponse(SuccessRoundDetails response, Context context) {
        this.response = response;

        this.updateScore(context);

    }

    public FragmentGameDetailsScore() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_details_score, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    public void set(Context ctx, User opponent, List<Question> answers) {
        this.opponent = opponent;
        this.answers = answers;
        this.updateGUI(ctx);
    }
    public void set(Context ctx, List<Question> answers) {
        this.answers = answers;
        this.updateScore(ctx);
    }
    private void updateGUI(Context ctx) {
        TPUtils.injectUserImage(ctx, currentUser, userImageView, false);
        TPUtils.injectUserImage(ctx, opponent, opponentImageView, false);
        this.updateScore(ctx);
    }
    private void updateScore(Context ctx) {
        int myScore = this.scoreFor(currentUser);
        int opponentScore = this.scoreFor(opponent);
        userScoreView.setText(String.valueOf(myScore));
        userImageView.setBorder(colorFor(ctx, myScore, opponentScore, currentUser));
        opponentScoreView.setText(String.valueOf(opponentScore));
        opponentImageView.setBorder(colorFor(ctx, opponentScore, myScore, opponent));

    }
    private int colorFor(Context ctx, int score,  int opponentScore, User user) {
        if(response != null && response.game != null && response.game.winner_id != null) {
            if(response.game.winner_id.equals(user.id)) return ContextCompat.getColor(ctx, R.color.green_on_white);
            else return ContextCompat.getColor(ctx, R.color.red_on_white);
        }
        if(score > opponentScore) {
            return ContextCompat.getColor(ctx, R.color.green_on_white);
        } else if (score == opponentScore) {
            return ContextCompat.getColor(ctx, R.color.whiteLight);
        } else {
            return ContextCompat.getColor(ctx, R.color.red_on_white);
        }
    }
    private int scoreFor(User user) {
        int score = 0;
        for(Question answer : answers) {
            if(answer.user_id.equals(user.id) && answer.correct) {
                score += 1;
            }
        }
        return score;
    }

}

package com.ted_developers.triviapatente.app.views.game_page.round_details;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.PlayButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Partecipation;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by donadev on 26/11/17.
 */

public class GameEndedHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.titleView) TextView titleView;
    public @BindView(R.id.scoreIncrementView) TextView scoreIncrementView;
    public @BindView(R.id.scoreIncrementArrow) ImageView scoreIncrementArrow;
    public @BindView(R.id.winnerImage) RoundedImageView winnerImage;
    public @BindView(R.id.loserImage) RoundedImageView loserImage;
    public @BindView(R.id.playButton) PlayButton playButton;
    public @BindView(R.id.incitationView) TextView incitationView;


    public static GameEndedHolder newHolder(Context ctx, int cellHeight) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.game_ended_view, null, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight);
        v.setLayoutParams(params);
        return new GameEndedHolder(v, ctx);
    }
    private Context context;

    public GameEndedHolder(View itemView, Context ctx) {
        super(itemView);
        context = ctx;
        ButterKnife.bind(this, itemView);
        this.initView();
    }
    private void initView() {
        int mainColor = ContextCompat.getColor(context, R.color.mainColor);
        winnerImage.setBorder(mainColor);
        loserImage.setBorder(mainColor);
    }
    private Boolean isWinning(SuccessRoundDetails response) {
        if(getScoreIncrement(response) == 0) return false;
        return SharedTPPreferences.currentUser().id == getWinner(response).id;
    }
    private String titleFor(SuccessRoundDetails response) {
        if(getScoreIncrement(response) == 0) return "Pareggio!";
        return isWinning(response) ? "Hai vinto!" : "Hai perso!";
    }

    private int arrowResourceFor(Integer increment) {
        return increment >= 0 ? R.drawable.up_score_arrow : R.drawable.down_score_arrow;
    }

    private Partecipation getPartecipation(SuccessRoundDetails response, Boolean needsWinner) {
        if(response.game.winner_id == null) {
            if(needsWinner) return response.partecipations.get(0);
            else return response.partecipations.get(1);
        }
        for(Partecipation partecipation : response.partecipations) {
            if(needsWinner && partecipation.user_id.equals(response.game.winner_id) ||
                    !needsWinner && !partecipation.user_id.equals(response.game.winner_id)) return partecipation;
        }
        return null;
    }

    private User getUser(SuccessRoundDetails response, Boolean needsWinner) {
        Partecipation partecipation = getPartecipation(response, needsWinner);
        if(partecipation == null) return null;
        for(User user : response.users) {
            if(partecipation.user_id == user.id) return user;
        }
        return null;
    }
    private User getWinner(SuccessRoundDetails response) {
        return getUser(response, true);
    }
    private Integer getScoreIncrement(SuccessRoundDetails response) {
        for(Partecipation partecipation : response.partecipations) {
            if(partecipation.user_id == SharedTPPreferences.currentUser().id) return partecipation.score_increment;
        }
        return null;
    }

    private String formatIncrement(Integer increment) {
        if(increment > 0) return "+" + increment;
        return "" + increment;
    }

    public void setUser(User opponent, Integer scoreIncrement) {
        scoreIncrementView.setText(formatIncrement(scoreIncrement));
        scoreIncrementArrow.setImageResource(arrowResourceFor(scoreIncrement));
        TPUtils.injectUserImage(context, SharedTPPreferences.currentUser(), winnerImage);
        TPUtils.injectUserImage(context, opponent, loserImage);

        incitationView.setText("Forza " + SharedTPPreferences.currentUser().username + "!");

    }

    public void bind(SuccessRoundDetails response, User opponent) {
        titleView.setText(titleFor(response));
        Integer increment = getScoreIncrement(response);
        scoreIncrementView.setText(formatIncrement(increment));
        scoreIncrementView.setVisibility(View.VISIBLE);
        scoreIncrementArrow.setImageResource(arrowResourceFor(increment));
        scoreIncrementArrow.setVisibility(View.VISIBLE);
        TPUtils.injectUserImage(context, isWinning(response) ? SharedTPPreferences.currentUser() : opponent, winnerImage);
        TPUtils.injectUserImage(context, isWinning(response) ? opponent : SharedTPPreferences.currentUser(), loserImage);
        if (isWinning(response)) playButton.setReplayNow();
        else playButton.setNewGame();
        playButton.setVisibility(View.VISIBLE);
        incitationView.setVisibility(View.GONE);
        playButton.goToGame(null, opponent);
    }
}

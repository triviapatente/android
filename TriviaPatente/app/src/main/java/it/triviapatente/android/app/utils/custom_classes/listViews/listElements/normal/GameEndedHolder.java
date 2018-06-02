package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindInt;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.buttons.PlayButton;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Partecipation;
import it.triviapatente.android.models.game.Question;
import it.triviapatente.android.models.responses.SuccessRoundDetails;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by donadev on 26/11/17.
 */

public class GameEndedHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.scoreIncrementView) TextView scoreIncrementView;
    @BindView(R.id.winnerImage) RoundedImageView winnerImage;
    @BindView(R.id.loserImage) RoundedImageView loserImage;
    @BindView(R.id.loserEmojii) TextView loserEmojiiView;
    @BindView(R.id.winnerEmojii) TextView winnerEmojiiView;
    @BindView(R.id.gameExpiredLabel) TextView gameExpiredLabel;

    @BindDimen(R.dimen.modal_view_round_details_loser_size) int loserSize;
    @BindDimen(R.dimen.modal_view_round_details_winner_size) int winnerSize;
    @BindDimen(R.dimen.modal_view_round_details_loser_margin_top) int loserTopMargin;

    @BindString(R.string.activity_round_details_emojii_winner) String winnerEmojii;
    @BindString(R.string.activity_round_details_emojii_loser) String loserEmojii;
    @BindString(R.string.activity_round_details_emojii_draw) String drawEmojii;
    @BindView(R.id.playButton) PlayButton playButton;
    @BindView(R.id.incitationView) TextView incitationView;


    public static GameEndedHolder newHolder(Context ctx, int cellHeight) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.game_ended_view, null, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight);
        v.setLayoutParams(params);
        return new GameEndedHolder(v, ctx);
    }
    private Context context;


    private void initEmojii() {
        winnerEmojii = TPUtils.translateEmoticons(winnerEmojii);
        loserEmojii = TPUtils.translateEmoticons(loserEmojii);
        drawEmojii = TPUtils.translateEmoticons(drawEmojii);
    }
    private void resizeImages(SuccessRoundDetails response) {
        RelativeLayout.LayoutParams winnerParams = (RelativeLayout.LayoutParams) winnerImage.getLayoutParams();
        RelativeLayout.LayoutParams loserParams = (RelativeLayout.LayoutParams) loserImage.getLayoutParams();
        if (isDraw(response) || isAnnulled(response)) {
            winnerParams.height = loserSize;
            winnerParams.width = loserSize;
            loserParams.height = loserSize;
            loserParams.width = loserSize;
            loserParams.topMargin = 0;

        } else {
            winnerParams.height = winnerSize;
            winnerParams.width = winnerSize;
            loserParams.height = loserSize;
            loserParams.width = loserSize;
            loserParams.topMargin = loserTopMargin;
        }
        winnerImage.setLayoutParams(winnerParams);
        loserImage.setLayoutParams(loserParams);

    }
    private void setEmojii(SuccessRoundDetails response) {
        if(isDraw(response) || isAnnulled(response)) {
            winnerEmojiiView.setText(drawEmojii);
            loserEmojiiView.setText(drawEmojii);
        } else {
            winnerEmojiiView.setText(winnerEmojii);
            loserEmojiiView.setText(loserEmojii);
        }
    }

    public GameEndedHolder(View itemView, Context ctx) {
        super(itemView);
        context = ctx;
        ButterKnife.bind(this, itemView);
        this.initView();
        this.initEmojii();
    }
    private void initView() {
        int mainColor = ContextCompat.getColor(context, R.color.mainColor);
        winnerImage.setBorder(mainColor);
        loserImage.setBorder(mainColor);
    }
    private int getScore(SuccessRoundDetails response, Boolean mine) {
        int score = 0;
        User current = SharedTPPreferences.currentUser();
        for(Question answer : response.answers) {
            Boolean mineAnswer = answer.user_id.equals(current.id);
            if(answer.correct && (mine && mineAnswer || !mine && !mineAnswer)) {
                score += 1;
            }
        }
        return score;
    }
    private Boolean isWinning(SuccessRoundDetails response) {
        if(isAnnulled(response)) return false;
        if(response.game.winner_id != null) {
            return response.game.winner_id.equals(SharedTPPreferences.currentUser().id);
        }
        int myScore = getScore(response, true);
        int opponentScore = getScore(response, false);
        return myScore > opponentScore;
    }
    private Boolean isAnnulled(SuccessRoundDetails response) {
        return response.game.ended && !response.game.started;
    }
    private Boolean isDraw(SuccessRoundDetails response) {
        if(isAnnulled(response)) return false;
        if(response.game.winner_id != null) return false;
        int myScore = getScore(response, true);
        int opponentScore = getScore(response, false);
        return myScore == opponentScore;
    }
    private String titleFor(SuccessRoundDetails response) {
        if(isAnnulled(response)) return "Partita annullata.";
        if(isDraw(response)) return "Hai pareggiato!";
        return isWinning(response) ? "Hai vinto!" : "Hai perso!";
    }

    private int arrowResourceFor(Integer increment) {
        return increment >= 0 ? R.drawable.up_score_arrow : R.drawable.down_score_arrow;
    }

    private Integer getScoreIncrement(SuccessRoundDetails response) {
        for(Partecipation partecipation : response.partecipations) {
            if(partecipation.user_id.equals(SharedTPPreferences.currentUser().id)) return partecipation.score_increment;
        }
        return null;
    }

    private String formatIncrement(Integer increment) {
        if(increment > 0) return "+" + increment;
        return "" + increment;
    }

    private boolean currentUserIsLeft(SuccessRoundDetails response) {
        return isWinning(response) || isDraw(response) || isAnnulled(response);
    }

    public void bind(SuccessRoundDetails response, User opponent) {
        setEmojii(response);
        resizeImages(response);
        titleView.setText(titleFor(response));
        Integer increment = getScoreIncrement(response);
        scoreIncrementView.setText(formatIncrement(increment).concat(" km"));
        scoreIncrementView.setVisibility(View.VISIBLE);
        TPUtils.injectUserImage(context, currentUserIsLeft(response) ? SharedTPPreferences.currentUser() : opponent, winnerImage);
        TPUtils.injectUserImage(context, currentUserIsLeft(response) ? opponent : SharedTPPreferences.currentUser(), loserImage);
        if (isWinning(response) || isAnnulled(response)) playButton.setReplayNow();
        else playButton.setNewGame(true);
        playButton.setVisibility(View.VISIBLE);
        incitationView.setVisibility(View.GONE);
        playButton.sendInvite(opponent);
        gameExpiredLabel.setVisibility(View.VISIBLE); // game expired ? VISIBLE : GONE
    }
}

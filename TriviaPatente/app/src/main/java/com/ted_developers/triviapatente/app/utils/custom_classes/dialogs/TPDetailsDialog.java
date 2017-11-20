package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 23/01/17.
 */
public class TPDetailsDialog extends Dialog {
    private int userScore = 0, opponentScore = 0;
    private long userID, opponentID;
    private User user, opponent;
    private OnCancelListener cancelListener = null;
    private @LayoutRes int layout = R.layout.modal_view_round_details;
    private Integer scoreIncrement = null;

    public TPDetailsDialog(Context context, User user, int userScore, User opponent, int opponentScore, Integer scoreIncrement, OnCancelListener cancelListener) {
        super(context);
        this.layout = R.layout.modal_view_round_details_game_ended;
        this.scoreIncrement = scoreIncrement;
        this.user = user;
        this.userScore = userScore;
        this.opponent = opponent;
        this.opponentScore = opponentScore;
        this.userID = userID;
        this.opponentID = opponentID;
        this.cancelListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        ((TextView) findViewById(R.id.roundDetailsUserScore)).setText(String.valueOf(userScore));
        ((TextView) findViewById(R.id.roundDetailsOpponentScore)).setText(String.valueOf(opponentScore));
        Context context = getContext();
        /*TPUtils.picasso
                .load(TPUtils.getUserImageFromID(context, userID))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into((RoundedImageView) findViewById(R.id.roundDetailsUser));
        TPUtils.picasso
                .load(TPUtils.getUserImageFromID(context, opponentID))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into((RoundedImageView) findViewById(R.id.roundDetailsOpponent));*/
        TPUtils.injectUserImage(context, user, (RoundedImageView) findViewById(R.id.roundDetailsUser));
        TPUtils.injectUserImage(context, opponent, (RoundedImageView) findViewById(R.id.roundDetailsOpponent));
        // game ended
        if(scoreIncrement != null) {
            ((TextView) findViewById(R.id.modal_details_message)).setText(
                    (userScore > opponentScore)? context.getString(R.string.modal_view_round_details_message_win) :
                            ((userScore == opponentScore)? context.getString(R.string.modal_view_round_details_message_draw) : context.getString(R.string.modal_view_round_details_message_loss))
            );
            ((ScoreIncrementView) findViewById(R.id.incrementView)).setScoreInc(scoreIncrement);
        }
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(0.4f);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(cancelListener);
    }
}

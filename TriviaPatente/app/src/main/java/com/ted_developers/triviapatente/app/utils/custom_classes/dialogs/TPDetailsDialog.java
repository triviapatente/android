package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;

/**
 * Created by Antonio on 23/01/17.
 */
public class TPDetailsDialog extends Dialog {
    private int userScore = 0, opponentScore = 0;
    private long userID, opponentID;
    private OnCancelListener cancelListener = null;

    public TPDetailsDialog(Context context) {
        super(context);
    }

    public TPDetailsDialog(Context context, int userScore, int opponentScore, long userID, long opponentID, OnCancelListener cancelListener) {
        super(context);
        this.userScore = userScore;
        this.opponentScore = opponentScore;
        this.userID = userID;
        this.opponentID = opponentID;
        this.cancelListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_view_round_details);
        ((TextView) findViewById(R.id.roundDetailsUserScore)).setText(String.valueOf(userScore));
        ((TextView) findViewById(R.id.roundDetailsOpponentScore)).setText(String.valueOf(opponentScore));
        Context context = getContext();
        TPUtils.picasso
                .load(TPUtils.getUserImageFromID(context, userID))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into((RoundedImageView) findViewById(R.id.roundDetailsUser));
        TPUtils.picasso
                .load(TPUtils.getUserImageFromID(context, opponentID))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into((RoundedImageView) findViewById(R.id.roundDetailsOpponent));
        setCanceledOnTouchOutside(true);
        setOnCancelListener(cancelListener);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(0.4f);
    }
}

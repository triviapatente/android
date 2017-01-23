package com.ted_developers.triviapatente.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 23/01/17.
 */
public class TPDetailsDialog extends Dialog {
    private int userScore = 0, opponentScore = 0;
    private OnCancelListener cancelListener = null;

    public TPDetailsDialog(Context context) {
        super(context);
    }

    public TPDetailsDialog(Context context, int userScore, int opponentScore, String userPicPath, String opponentPicPath, OnCancelListener cancelListener) {
        super(context);
        this.userScore = userScore;
        this.opponentScore = opponentScore;
        this.cancelListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_view_round_details);
        ((TextView) findViewById(R.id.roundDetailsUserScore)).setText(String.valueOf(userScore));
        ((TextView) findViewById(R.id.roundDetailsOpponentScore)).setText(String.valueOf(opponentScore));
        setCanceledOnTouchOutside(true);
        setOnCancelListener(cancelListener);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(0.4f);
    }
}

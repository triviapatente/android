package it.triviapatente.android.app.utils.custom_classes.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import it.triviapatente.android.R;

/**
 * Created by Antonio on 04/01/17.
 */
public abstract class TPLeaveDialog extends TPDialog {
    private int scoreInc;

    @Deprecated
    public TPLeaveDialog(Context context) {
        super(context);
    }

    @Deprecated
    public TPLeaveDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Deprecated
    public TPLeaveDialog(Context context, int themeResId, float dimAmount) {
        super(context, themeResId, dimAmount);
    }

    @Deprecated
    public TPLeaveDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Deprecated
    public TPLeaveDialog(Context context, int themeResId, float dimAmount, boolean cancelable, OnCancelListener cancelListener) {
        super(context, themeResId, dimAmount, cancelable, cancelListener);
    }

    public TPLeaveDialog(Context context, int scoreInc, OnCancelListener cancelListener) {
        super(context, R.layout.modal_view_leave_game, 0, false, cancelListener);
        this.scoreInc = scoreInc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ScoreIncrementView) findViewById(R.id.scoreIncView)).setScoreInc(scoreInc);
        TextView leaveMessageTextView = ((TextView) findViewById(R.id.leaveMessage));
        if(scoreInc == 0) {
            leaveMessageTextView.setText(getContext().getString(R.string.modal_view_leave_game_message_no_decrement));
        } else {
            leaveMessageTextView.setText(R.string.modal_view_leave_game_message_decrement);
        }
    }

}

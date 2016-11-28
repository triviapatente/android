package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 28/11/16.
 */
public class TPTellFriendFooter extends TPFooter {
    public TPTellFriendFooter(View itemView) {
        super(itemView);
        buildButton(itemView.getContext());
    }

    private void buildButton(Context context) {
        itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor));
        Button tellAFriendButton = new Button(context);
        tellAFriendButton.setBackground(ContextCompat.getDrawable(context, R.drawable.tell_a_friend_button));
        TextViewCompat.setTextAppearance(tellAFriendButton, R.style.TPTextStyleSmall);
        tellAFriendButton.setTextColor(Color.WHITE);
        tellAFriendButton.setText(R.string.tell_a_friend_button_text);
        int padding = (int) context.getResources().getDimension(R.dimen.tell_a_friend_button_padding);
        tellAFriendButton.setPadding(padding, 0, padding, 0);
        tellAFriendButton.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (int)context.getResources().getDimension(R.dimen.tell_a_friend_button_height)
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tellAFriendButton.setLayoutParams(params);
        ((RelativeLayout) itemView).addView(tellAFriendButton);
    }
}

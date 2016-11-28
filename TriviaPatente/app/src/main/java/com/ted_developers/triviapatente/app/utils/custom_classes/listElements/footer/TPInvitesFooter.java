package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 28/11/16.
 */
public class TPInvitesFooter extends TPFooter {
    public TPInvitesFooter(View itemView) {
        super(itemView);
        Context c = itemView.getContext();
        TextView view = new TextView(c);
        TextViewCompat.setTextAppearance(view, R.style.TPTextStyleSmall);
        view.setTextColor(Color.WHITE);
        view.setText(c.getResources().getText(R.string.no_more_games));
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((LinearLayout)itemView).addView(view);
    }
}

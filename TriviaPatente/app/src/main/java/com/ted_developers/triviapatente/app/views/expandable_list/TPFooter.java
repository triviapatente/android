package com.ted_developers.triviapatente.app.views.expandable_list;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;

/**
 * Created by Antonio on 13/11/16.
 */
public class TPFooter extends TPHolder<Pair<String, Integer>> {
    public TPFooter(View itemView) {
        super(itemView);
        TextViewCompat.setTextAppearance(((TextView) itemView), R.style.TPTextStyleMedium);
        ((TextView) itemView).setTextColor(Color.WHITE);
        ((TextView) itemView).setGravity(Gravity.CENTER);
    }
    @Override
    public void bind(Pair<String, Integer> element) {
        if(element.second > 10) {
            itemView.setVisibility(View.GONE);
        } else {
            ((TextView) itemView).setText(element.first);
            itemView.setVisibility(View.VISIBLE);
        }
    }
}

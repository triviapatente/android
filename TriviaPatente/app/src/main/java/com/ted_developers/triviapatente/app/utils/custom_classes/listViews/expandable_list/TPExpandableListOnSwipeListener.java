package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list;

import android.content.Context;

import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;

/**
 * Created by Antonio on 13/11/16.
 */
public class TPExpandableListOnSwipeListener<T> extends OnSwipeTouchListener {
    private final TPExpandableList<T> expandableList;
    public boolean needScrollWithOffset = false;

    public TPExpandableListOnSwipeListener(Context ctx, TPExpandableList<T> expandableList) {
        super(ctx);
        this.expandableList = expandableList;
    }

    public void onSwipeTop() {
        expandableList.setMaximizedHeightMode();
    }

    public void onSwipeBottom() {
        if(expandableList.listView.computeVerticalScrollOffset() == 0 || needScrollWithOffset) {
            expandableList.setMinimizedHeightMode();
        }
    }
}

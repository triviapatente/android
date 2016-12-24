package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Antonio on 13/11/16.
 */
public class mLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public mLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
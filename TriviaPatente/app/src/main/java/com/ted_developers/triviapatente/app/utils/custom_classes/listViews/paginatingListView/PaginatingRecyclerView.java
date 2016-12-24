package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.paginatingListView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Antonio on 24/12/16.
 */
public class PaginatingRecyclerView extends RecyclerView {

    private int orientation = LinearLayoutManager.HORIZONTAL;

    public PaginatingRecyclerView(Context context) {
        super(context);
        init();
    }

    public PaginatingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PaginatingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        setOnTouchListener(new PaginatingOnTouchListener(getContext(), orientation, this));
    }

    private void init(AttributeSet attributeSet) {
        // todo parse arguments
        init();
    }
}

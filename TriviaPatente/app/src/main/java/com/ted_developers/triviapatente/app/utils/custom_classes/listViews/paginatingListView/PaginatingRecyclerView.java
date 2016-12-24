package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.paginatingListView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.ted_developers.triviapatente.app.utils.custom_classes.adapters.TPListAdapter;

/**
 * Created by Antonio on 24/12/16.
 */
public class PaginatingRecyclerView extends RecyclerView {

    private int orientation = LinearLayoutManager.HORIZONTAL;
    public int elementWidth, elementHeight;

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
        addOnScrollListener(new PaginatingOnSwipeListener(orientation, this));
    }

    private void init(AttributeSet attributeSet) {
        // todo parse arguments
        init();
    }

    public void goToElement(int elementPosition) {
        
    }

    public void setAdapter(TPListAdapter adapter, int elementWidth) {
        setAdapter(adapter);
        this.elementWidth = elementWidth;
    }

    public void setAdapter(TPListAdapter adapter) {
        super.setAdapter(adapter);
        elementHeight = adapter.elementHeight;
        this.post(new Runnable() {
            @Override
            public void run() {
                elementWidth = getMeasuredWidth();
            }
        });
    }
}

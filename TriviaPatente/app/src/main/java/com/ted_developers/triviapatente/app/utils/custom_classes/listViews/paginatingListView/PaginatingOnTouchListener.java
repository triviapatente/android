package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.paginatingListView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;

/**
 * Created by Antonio on 24/12/16.
 */
public class PaginatingOnTouchListener extends OnSwipeTouchListener {
    private int orientation = LinearLayoutManager.HORIZONTAL;
    private PaginatingRecyclerView paginatingRecyclerView;
    public PaginatingOnTouchListener(Context context) {
        super(context);
    }

    public PaginatingOnTouchListener(Context context, int orientation, PaginatingRecyclerView paginatingRecyclerView) {
        super(context);
        this.orientation = orientation;
        this.paginatingRecyclerView = paginatingRecyclerView;
    }

    public void onSwipeRight() {
        if(LinearLayoutManager.HORIZONTAL == orientation) {
            // todo paginate right
        }
    }

    public void onSwipeLeft() {
        if(LinearLayoutManager.HORIZONTAL == orientation) {
            // todo paginate left
        }
    }

    public void onSwipeTop() {
        if(LinearLayoutManager.VERTICAL == orientation) {
            // todo paginate top
        }
    }

    public void onSwipeBottom() {
        if(LinearLayoutManager.VERTICAL == orientation) {
            // todo paginate bottom
        }
    }
}

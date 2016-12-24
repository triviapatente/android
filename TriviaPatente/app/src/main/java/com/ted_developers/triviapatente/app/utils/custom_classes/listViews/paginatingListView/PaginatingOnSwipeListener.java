package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.paginatingListView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ted_developers.triviapatente.app.utils.OnSwipeTouchListener;

/**
 * Created by Antonio on 24/12/16.
 */
public class PaginatingOnSwipeListener extends RecyclerView.OnScrollListener {
    private int orientation = LinearLayoutManager.HORIZONTAL;
    private PaginatingRecyclerView paginatingRecyclerView;

    public PaginatingOnSwipeListener(int orientation, PaginatingRecyclerView paginatingRecyclerView) {
        this.orientation = orientation;
        this.paginatingRecyclerView = paginatingRecyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        if(dy > 0)
        {
            onSwipeBottom();
        } else if(dy < 0) {
            onSwipeTop();
        }
        if(dx > 0) {
            onSwipeRight();
        } else if(dx < 0) {
            onSwipeLeft();
        }
    }

    public void onSwipeRight() {
        Log.i("TEST", "right");
        if(LinearLayoutManager.HORIZONTAL == orientation) {
            int position = calculatePositionHorizontal(), lastPosition = paginatingRecyclerView.getAdapter().getItemCount()-1;
            paginatingRecyclerView.goToElement((position == lastPosition) ? lastPosition : position + 1);
        }
    }

    public void onSwipeLeft() {
        Log.i("TEST", "left");
        if(LinearLayoutManager.HORIZONTAL == orientation) {
            int position = calculatePositionHorizontal();
            paginatingRecyclerView.goToElement((position == 0) ? 0 : position - 1);
        }
    }

    public void onSwipeTop() {
        Log.i("TEST", "top");
        if(LinearLayoutManager.VERTICAL == orientation) {
            paginatingRecyclerView.goToElement(calculatePositionVertical());
        }
    }

    public void onSwipeBottom() {
        Log.i("TEST", "bottom");
        if(LinearLayoutManager.VERTICAL == orientation) {
            paginatingRecyclerView.goToElement(calculatePositionVertical());
        }
    }

    public int calculatePositionVertical() {
        return paginatingRecyclerView.computeVerticalScrollOffset() / paginatingRecyclerView.elementHeight;
    }
    public int calculatePositionHorizontal() {
        return paginatingRecyclerView.computeHorizontalScrollOffset() / paginatingRecyclerView.elementWidth;
    }
}

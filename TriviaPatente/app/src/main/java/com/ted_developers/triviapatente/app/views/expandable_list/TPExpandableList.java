package com.ted_developers.triviapatente.app.views.expandable_list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.adapters.ResizeAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.adapters.TPExpandableListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TPExpandableList<T> extends Fragment {
    // list elements
    public TPExpandableListAdapter<T> adapter;
    @BindView(R.id.listTitle) TextView listTitle;
    @BindView(R.id.listCounter) TextView listCounter;
    @BindView(R.id.listView) public RecyclerView listView;
    @BindView(R.id.listHeader) RelativeLayout listHeader;
    // list dimens
    @BindDimen(R.dimen.list_title_height) public int titleHeight;
    // expandable list utils
    public int maximizedHeight, minimizedHeight, oldMinimizedHeight, duration = 300, elementHeight, add_remove_time = 350, moveTime = 200;
    ResizeAnimation maximize, minimize, forcedMinimize;
    @BindDimen(R.dimen.tp_toolbar_height) int toolBarHeight;
    public boolean maximized = false;
    public mLinearLayoutManager listLayoutManager;
    // to do operation on touch down
    public Callable<Void> touchDownHandler;
    // separator color
    @BindColor(R.color.mainColor) @ColorInt int mainColor;

    public TPExpandableList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tpexpandable_list, container, false);
        ButterKnife.bind(this, v);
        listTitle.setTextColor(Color.WHITE);
        listCounter.setTextColor(Color.WHITE);
        listView.addItemDecoration(new DividerItemDecoration(mainColor, v.getWidth()));
        listLayoutManager = new mLinearLayoutManager(getContext());
        listView.setLayoutManager(listLayoutManager);
        TPExpandableListOnSwipeListener swipeListener = new TPExpandableListOnSwipeListener(getContext(), this);
        listHeader.setOnTouchListener(swipeListener);
        listView.setOnTouchListener(swipeListener);
        listView.getItemAnimator().setRemoveDuration(add_remove_time);
        listView.getItemAnimator().setMoveDuration(moveTime);
        listView.getItemAnimator().setAddDuration(add_remove_time);
        return v;
    }

    public void setItems(List<T> list,
                         @LayoutRes int holderLayout, Class<? extends TPHolder<T>> holderClass,
                         @LayoutRes int footerLayout, Class<? extends TPFooter> footerClass,
                         int elementHeight) {
        this.elementHeight = elementHeight;
        adapter = new TPExpandableListAdapter<>(getContext(), list, holderLayout, holderClass, footerLayout, footerClass, elementHeight, this);
        listView.setAdapter(adapter);
        listHeader.setVisibility(View.VISIBLE);
    }

    public void setListTitle(String text) {
        listTitle.setText(text);
    }

    public void setListCounter(int counter) {
        setListCounter(counter, true);
    }

    public void setListCounter(int counter, boolean firstTime) {
        listCounter.setText(String.valueOf(counter));
        int numberOfShownItems = (getView().getHeight() - titleHeight) / elementHeight;
        numberOfShownItems = (numberOfShownItems < counter)? numberOfShownItems : counter;
        oldMinimizedHeight = minimizedHeight;
        minimizedHeight = ((numberOfShownItems > 3)? 3 : numberOfShownItems) * elementHeight + titleHeight;
        if(oldMinimizedHeight == 0) { oldMinimizedHeight = minimizedHeight; }
        maximizedHeight = getResources().getDisplayMetrics().heightPixels - toolBarHeight;
        maximize = new ResizeAnimation(getView(), getView().getWidth(), minimizedHeight, getView().getWidth(), maximizedHeight);
        maximize.setDuration(duration);
        minimize = new ResizeAnimation(getView(), getView().getWidth(), maximizedHeight, getView().getWidth(), minimizedHeight);
        minimize.setDuration(duration);
        forcedMinimize = new ResizeAnimation(getView(), getView().getWidth(), oldMinimizedHeight, getView().getWidth(), minimizedHeight);
        forcedMinimize.setStartOffset(add_remove_time);
        if(firstTime) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minimizedHeight);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.getView().setLayoutParams(params);
        }
    }

    public void updateMinimized() {
        if(!maximized) {
            getView().startAnimation(forcedMinimize);
        }
    }

    public void setMinimizedHeightMode() {
        if(maximized) {
            getView().startAnimation(minimize);
            listLayoutManager.scrollToPositionWithOffset(0, 0);
            listLayoutManager.setScrollEnabled(false);
            maximized = false;
        }
    }

    public void setMaximizedHeightMode() {
        if(!maximized) {
            adapter.notifyItemChanged(adapter.getItemCount() - 1); // update footer to eventually fill the screen
            getView().startAnimation(maximize);
            listLayoutManager.setScrollEnabled(true);
            maximized = true;
        }
    }

}

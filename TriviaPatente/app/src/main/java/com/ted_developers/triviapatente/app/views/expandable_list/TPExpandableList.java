package com.ted_developers.triviapatente.app.views.expandable_list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TPExpandableList<T> extends Fragment {
    // list elements
    @BindView(R.id.listTitle) TextView listTitle;
    @BindView(R.id.listCounter) TextView listCounter;
    @BindView(R.id.listView) RecyclerView listView;
    @BindView(R.id.listHeader) RelativeLayout listHeader;
    // list dimens
    @BindDimen(R.dimen.list_title_height) int titleHeight;
    // expandable list utils
    int maximizedHeight, minimizedHeight, duration = 300, elementHeight;
    ResizeAnimation maximize, minimize;
    @BindDimen(R.dimen.tp_toolbar_height) int toolBarHeight;
    private boolean maximized = false;
    mLinearLayoutManager listLayoutManager;
    // to do operation on touch down
    public Callable<Void> touchDownHandler;

    public TPExpandableList() {}

    public static TPExpandableList newInstance() {
        TPExpandableList fragment = new TPExpandableList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        TextViewCompat.setTextAppearance(listTitle, R.style.TPTextStyleMedium);
        TextViewCompat.setTextAppearance(listCounter, R.style.TPTextStyleMedium);
        listTitle.setTextColor(Color.WHITE);
        listCounter.setTextColor(Color.WHITE);
        listView.addItemDecoration(new DividerItemDecoration(getActivity()));
        listLayoutManager = new mLinearLayoutManager(getContext());
        listView.setLayoutManager(listLayoutManager);
        TPExpandableListOnSwipeListener swipeListener = new TPExpandableListOnSwipeListener(getContext(), this);
        listHeader.setOnTouchListener(swipeListener);
        listView.setOnTouchListener(swipeListener);
        return v;
    }

    public void setItems(List<T> list, int layout, Class<? extends TPHolder<T>> holderClass, String footer) {
        TPExpandableListAdapter<T> adapter = new TPExpandableListAdapter<T>(getContext(), list, layout, holderClass, footer, this);
        listView.setAdapter(adapter);
    }

    public void setListTitle(String text) {
        listTitle.setText(text);
    }

    public void setListCounter(int counter, int elementHeight) {
        this.elementHeight = elementHeight;
        listCounter.setText(String.valueOf(counter));
        int numberOfShownItems = (getView().getHeight() - titleHeight) / elementHeight;
        numberOfShownItems = (numberOfShownItems < counter)? numberOfShownItems : counter;
        minimizedHeight = ((numberOfShownItems > 3)? 3 : numberOfShownItems) * elementHeight + titleHeight;
        maximizedHeight = getResources().getDisplayMetrics().heightPixels - toolBarHeight;
        maximize = new ResizeAnimation(getView(), getView().getWidth(), minimizedHeight, getView().getWidth(), maximizedHeight);
        maximize.setDuration(duration);
        minimize = new ResizeAnimation(getView(), getView().getWidth(), maximizedHeight, getView().getWidth(), minimizedHeight);
        minimize.setDuration(duration);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minimizedHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.getView().setLayoutParams(params);
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
            getView().startAnimation(maximize);
            listLayoutManager.setScrollEnabled(true);
            maximized = true;
        }
    }

}

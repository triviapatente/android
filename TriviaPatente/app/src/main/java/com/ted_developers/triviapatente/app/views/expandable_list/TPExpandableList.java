package com.ted_developers.triviapatente.app.views.expandable_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class TPExpandableList<T> extends Fragment {
    // list elements
    @BindView(R.id.listTitle) TextView listTitle;
    @BindView(R.id.listCounter) TextView listCounter;
    @BindView(R.id.listView) RecyclerView listView;
    // list dimens
    @BindDimen(R.dimen.title_heigth) int titleHeight;
    // expandable list utils
    int maximizedHeight, minimizedHeight, duration = 300;
    ResizeAnimation maximize, minimize;
    @BindDimen(R.dimen.tp_toolbar_height) int toolBarHeight;
    private boolean scrolling = false, up = false, maximized = false;


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
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public void setGames(List<T> list, int layout, Class<? extends TPHolder<T>> holderClass) {
        TPExpandableListAdapter<T> adapter = new TPExpandableListAdapter<T>(getContext(), list, layout, holderClass);
        listView.setAdapter(adapter);
    }

    public void setListTitle(String text) {
        listTitle.setText(text);
    }

    public void setListCounter(int counter, int elementHeight) {
        listCounter.setText(String.valueOf(counter));
        minimizedHeight = ((counter > 3)? 3: counter) * elementHeight + titleHeight;
        maximizedHeight = getResources().getDisplayMetrics().heightPixels - toolBarHeight;
        maximize = new ResizeAnimation(getView(), getView().getWidth(), minimizedHeight, getView().getWidth(), maximizedHeight);
        maximize.setDuration(duration);
        minimize = new ResizeAnimation(getView(), getView().getWidth(), maximizedHeight, getView().getWidth(), minimizedHeight);
        minimize.setDuration(duration);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minimizedHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.getView().setLayoutParams(params);
    }

    private void setMinimizedHeightMode() {
        getView().startAnimation(minimize);
    }

    private void setMaximizedHeightMode() {
        getView().startAnimation(maximize);
    }

    @OnTouch(R.id.listView)
    public boolean listTouched(View v, MotionEvent e) {
        return expandOnScroll(v, e);
    }

    @OnTouch(R.id.expandableList)
    public boolean titleTouched(View v, MotionEvent e) {
        return expandOnScroll(v, e);
    }

    public boolean expandOnScroll(View v, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP: {
                if(scrolling) {
                    if(up) {
                        if(!maximized) {
                            setMaximizedHeightMode();
                            maximized = true;
                        }
                    } else {
                        if(maximized) {
                            setMinimizedHeightMode();
                            maximized = false;
                        }
                    }
                }
            } break;
            case MotionEvent.ACTION_MOVE: {
                float dY = e.getY();
                scrolling = dY != 0;
                up = dY < 0;
            }
        }
        return true;
    }
}

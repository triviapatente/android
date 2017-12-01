package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.animation.ResizeAnimation;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.TPExpandableListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.DividerItemDecoration;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPFooter;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TPExpandableList<T> extends Fragment {
    // list elements
    public TPExpandableListAdapter<T> adapter;
    @BindView(R.id.listTitle) TextView listTitle;
    // @BindView(R.id.listCounter) TextView listCounter;
    @BindView(R.id.syncRecentGames) ImageButton syncRecentGamesButton;
    @BindView(R.id.listView) public RecyclerView listView;
    @BindView(R.id.listHeader) RelativeLayout listHeader;
    // list dimens
    @BindDimen(R.dimen.list_title_height) public int titleHeight;
    // expandable list utils
    public int maximizedHeight, minimizedHeight, oldMinimizedHeight, duration = 300, elementHeight = 1, add_remove_time = 350, moveTime = 200;
    ResizeAnimation maximize, minimize, forcedMinimize;
    ValueAnimator headerColorAnimator;
    @BindDimen(R.dimen.tp_toolbar_height) int toolBarHeight;
    public boolean maximized = false;
    private boolean firstTime = true;
    public mLinearLayoutManager listLayoutManager;
    private int maxNumberOfShownItems;
    private TPExpandableListOnSwipeListener headerSwipeListener;
    // separator color
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    @BindColor(R.color.mainColorGradTop) @ColorInt int gradientTopColor;
    @BindColor(R.color.mainColorDark2) @ColorInt int gradientBottomColor;
    // header titles
    String alternativeTitle, defaultTitle;


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
        // listCounter.setTextColor(Color.WHITE);
        listView.addItemDecoration(new DividerItemDecoration(mainColor, v.getWidth()));
        listLayoutManager = new mLinearLayoutManager(getContext());
        listView.setLayoutManager(listLayoutManager);
        TPExpandableListOnSwipeListener swipeListener = new TPExpandableListOnSwipeListener(getContext(), this);
        listView.setOnTouchListener(swipeListener);
        listView.getItemAnimator().setRemoveDuration(add_remove_time);
        listView.getItemAnimator().setMoveDuration(moveTime);
        listView.getItemAnimator().setAddDuration(add_remove_time);
        headerSwipeListener = new TPExpandableListOnSwipeListener(getContext(), this);
        headerSwipeListener.needScrollWithOffset = true;
        return v;
    }

    public void setSyncButtonOnClickListner(View.OnClickListener onClickListner) {
        syncRecentGamesButton.setOnClickListener(onClickListner);
        syncRecentGamesButton.setVisibility(View.VISIBLE);
    }

    public void setTitles(String defaultTitle, String alternativeTitle) {
        this.defaultTitle = defaultTitle;
        this.alternativeTitle = alternativeTitle;
    }

    public void setItems(List<T> list,
                         @LayoutRes int holderLayout, Class<? extends TPHolder<T>> holderClass,
                         @LayoutRes int footerLayout, Class<? extends TPFooter> footerClass,
                         int elementHeight) {
        this.elementHeight = elementHeight;
        adapter = new TPExpandableListAdapter<>(getContext(), list, holderLayout, holderClass, footerLayout, footerClass, elementHeight, this);
        listView.swapAdapter(adapter, false);
        listHeader.setVisibility(View.VISIBLE);
    }

    public void setListCounter(int counter) {
        setListCounter(counter, true);
    }
    
    public void setListCounter(final int counter, final boolean needUpdateLayoutParams) {
        if(counter == 0) { setCounterZero(); } else { setCounterNonZero(counter); }
        this.getView().post(new Runnable() {
            @Override
            public void run() {
                if(firstTime) {
                    calculateFinalSizes();
                    firstTime = false;
                }
                calculateMinimizedHeight(counter);
                calculateAnimations();
                if(needUpdateLayoutParams) {
                    updateLayoutParams();
                } else if(!maximized){
                    updateMinimized();
                } else if(counter == 0) {
                    setMinimizedHeightMode();
                }
            }
        });
    }

    private void setCounterNonZero(int counter) {
        listTitle.setText(defaultTitle);
        //listCounter.setVisibility(View.VISIBLE);
        listHeader.setOnTouchListener(headerSwipeListener);
        //listCounter.setText(String.valueOf(counter));
    }

    private void setCounterZero() {
        listTitle.setText(alternativeTitle);
        //listCounter.setVisibility(View.GONE);
        listHeader.setOnTouchListener(null);
    }

    private void calculateFinalSizes() {
        maxNumberOfShownItems = (getView().getMeasuredHeight() - titleHeight) / elementHeight;
        if(maxNumberOfShownItems > 3) {
            maxNumberOfShownItems = 3;
        }
        maximizedHeight = getResources().getDisplayMetrics().heightPixels;
    }

    private void calculateMinimizedHeight(int counter) {
        oldMinimizedHeight = minimizedHeight;
        minimizedHeight = ((maxNumberOfShownItems < counter)? maxNumberOfShownItems : counter) * elementHeight + titleHeight;
        if(oldMinimizedHeight == 0) { oldMinimizedHeight = minimizedHeight; }
    }

    private void calculateAnimations() {
        maximize = new ResizeAnimation(getView(), getView().getWidth(), minimizedHeight, getView().getWidth(), maximizedHeight);
        maximize.setDuration(duration);
        minimize = new ResizeAnimation(getView(), getView().getWidth(), maximizedHeight, getView().getWidth(), minimizedHeight);
        minimize.setDuration(duration);
        forcedMinimize = new ResizeAnimation(getView(), getView().getWidth(), oldMinimizedHeight, getView().getWidth(), minimizedHeight);
        forcedMinimize.setStartOffset(add_remove_time);

        headerColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), gradientBottomColor, gradientTopColor);
        headerColorAnimator.setDuration(duration); // milliseconds
        headerColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                listHeader.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
    }

    private void updateLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minimizedHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.getView().setLayoutParams(params);
    }

    public void updateMinimized() {
        if(!maximized) {
            getView().startAnimation(forcedMinimize);
            headerColorAnimator.reverse();
        }
    }

    public void setMinimizedHeightMode() {
        if(maximized) {
            getView().startAnimation(minimize);
            headerColorAnimator.reverse();
            listLayoutManager.scrollToPositionWithOffset(0, 0);
            listLayoutManager.setScrollEnabled(false);
            maximized = false;
        }
    }

    public void setMaximizedHeightMode() {
        if(!maximized) {
            adapter.notifyItemChanged(adapter.getItemCount() - 1); // update footer to eventually fill the screen
            getView().startAnimation(maximize);
            headerColorAnimator.start();
            listLayoutManager.setScrollEnabled(true);
            maximized = true;
        }
    }
}

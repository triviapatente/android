package it.triviapatente.android.app.utils.custom_classes.listViews.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.TPHolder;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.footer.TPFooter;

import java.util.List;

/**
 * Created by Antonio on 23/12/16.
 */
public class TPEnterAnimListAdapter<T> extends TPListAdapter<T> {
    public TPEnterAnimListAdapter(Context context, List<T> list, @LayoutRes int holderLayout, Class<? extends TPHolder<T>> holderClass, @LayoutRes int footerLayout, Class<? extends TPFooter> footerClass, int elementHeight, RecyclerView recyclerView) {
        super(context, list, holderLayout, holderClass, footerLayout, footerClass, elementHeight, recyclerView);
        decDuration = (int) (duration * 0.75 / ((list == null)? 4 : list.size() - 1));
        offsetDuration = decDuration / 4;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        setAnimation(holder.itemView, position);
    }

    public int duration = 950;
    private int decDuration, offsetDuration;

    private void setAnimation(View viewToAnimate, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setDuration(duration - decDuration*(items.size()-position));
        animation.setStartOffset(offsetDuration*position);
        viewToAnimate.startAnimation(animation);
    }
}

package com.ted_developers.triviapatente.app.utils.custom_classes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;

import java.util.List;

/**
 * Created by Antonio on 28/11/16.
 */
public class TPListFillWithFooterAdapter<T> extends TPListAdapter<T> {
    RecyclerView recyclerView;
    public TPListFillWithFooterAdapter(Context context, List<T> list, @LayoutRes int layout,
                                       Class<? extends TPHolder<T>> holderClass, Class<? extends TPFooter> footerClass,
                                       int elementHeight, RecyclerView recyclerView) {
        super(context, list, layout, holderClass, footerClass, elementHeight);
        this.recyclerView = recyclerView;
    }

    protected int computeFooterHeight() {
        int height = recyclerView.getHeight() - items.size() * elementHeight + recyclerView.computeVerticalScrollOffset();
        // always shows a footer
        if(height < min_footer_height) {
            height = min_footer_height;
        }
        return height;
    }
}

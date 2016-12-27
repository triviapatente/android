package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list.TPExpandableList;

import java.util.List;

/**
 * Created by Antonio on 12/11/16.
 */
public class TPExpandableListAdapter<T> extends TPListAdapter<T> {
    TPExpandableList<T> expandableList;

    public TPExpandableListAdapter(Context context, List<T> list,
                                   @LayoutRes int holderLayout, Class<? extends TPHolder<T>> holderClass,
                                   @LayoutRes int footerLayout, Class<? extends TPFooter> footerClass,
                                   int elementHeight, TPExpandableList<T> expandableList) {
        super(context, list, holderLayout, holderClass, footerLayout, footerClass, elementHeight, expandableList.listView);
        this.expandableList = expandableList;
    }

    protected int computeFooterHeight() {
        int height = expandableList.maximizedHeight - expandableList.titleHeight - elementHeight * items.size();
        // if it less then zero it is not displayed
        if(height < 0) {
            height = 0;
        } else if(height < min_footer_height){
            height = min_footer_height;
        }
        return height;
    }

    protected RecyclerView.ViewHolder createNormalHolderWithCustomConstructor(View v) {
        try {
            return holderClass.getConstructor(View.class, TPExpandableList.class).newInstance(v, expandableList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void doStuffBeforeItemRemoved() {
        TPFooter.expand((extraElements == 0) ? null : expandableList.listLayoutManager.getChildAt(getItemCount()), computeFooterHeight(),
                expandableList.add_remove_time, expandableList.moveTime);
    }

    protected void doStuffAfterItemRemoved() {
        expandableList.setListCounter(items.size(), false);
    }

    protected void doStuffAfterItemAdd() {
        expandableList.setListCounter(items.size(), false);
    }
}

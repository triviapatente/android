package com.ted_developers.triviapatente.app.utils.custom_classes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Antonio on 12/11/16.
 */
public class TPExpandableListAdapter<T> extends TPListAdapter<T> {
    TPExpandableList<T> expandableList;

    public TPExpandableListAdapter(Context context, List<T> list, @LayoutRes int layout, Class<? extends TPHolder<T>> holderClass, Class<? extends TPFooter> footerClass, int elementHeight, TPExpandableList<T> expandableList) {
        super(context, list, layout, holderClass, footerClass, elementHeight);
        this.expandableList = expandableList;
    }

    protected int computeFooterHeight() {
        int height = expandableList.maximizedHeight - expandableList.titleHeight - elementHeight * items.size();
        if(height > 0 && height < min_footer_height) {
            height = min_footer_height;
        }
        return height;
    }

    protected RecyclerView.ViewHolder createNormalHolderWithCustomConstructor(View v) {
        try {
            return holderClass.getConstructor(View.class, Context.class, TPExpandableList.class).newInstance(v, context, expandableList);
        } catch (Exception e) { return null; }
    }

    protected void doOtherStuffBeforeItemRemoved() {
        if(expandableList.maximized) {
            TPFooter.expand(expandableList.listLayoutManager.findViewByPosition(getItemCount()), computeFooterHeight());
        }
    }
}

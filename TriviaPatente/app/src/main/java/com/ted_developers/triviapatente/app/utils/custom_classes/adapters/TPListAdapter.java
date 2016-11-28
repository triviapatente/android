package com.ted_developers.triviapatente.app.utils.custom_classes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;

import java.util.List;

/**
 * Created by Antonio on 28/11/16.
 */
public class TPListAdapter<T> extends RecyclerView.Adapter {
    protected Context context;
    public List<T> items;
    @LayoutRes int layout;
    public int min_footer_height, elementHeight;
    Class<? extends TPFooter> footerClass;
    Class<? extends TPHolder<T>> holderClass;

    public TPListAdapter(Context context, List<T> list, @LayoutRes int layout, Class<? extends TPHolder<T>> holderClass, Class<? extends TPFooter> footerClass, int elementHeight) {
        this.context = context;
        this.items = list;
        this.layout = layout;
        this.holderClass = holderClass;
        this.min_footer_height = (int) context.getResources().getDimension(R.dimen.min_footer_height);
        this.elementHeight = elementHeight;
        this.footerClass = footerClass;
    }

    protected int computeFooterHeight() {
        return min_footer_height;
    }

    protected RecyclerView.ViewHolder createFooterHolder() {
        try {
            return footerClass.getConstructor(View.class).newInstance(new LinearLayout(context));
        } catch (Exception e){ return null; }
    }

    protected RecyclerView.ViewHolder createNormalHolder() {
        View v = LayoutInflater.from(context).inflate(layout, null);
        try {
            return holderClass.getConstructor(View.class, Context.class).newInstance(v, context);
        } catch (Exception e) { return createNormalHolderWithCustomConstructor(v); }
    }

    protected RecyclerView.ViewHolder createNormalHolderWithCustomConstructor(View v) {
        return null;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPES.Footer: return createFooterHolder();
            default: return createNormalHolder();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TableRow.LayoutParams params;
        switch (getItemViewType(position)) {
            case VIEW_TYPES.Footer: {
                ((TPFooter) holder).bind(null);
                params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, computeFooterHeight());
            } break;
            default: {
                ((TPHolder) holder).bind(getNormalItem(position));
                params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, elementHeight);
            }
        }
        holder.itemView.setLayoutParams(params);
    }

    public T getNormalItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size() + 1; // there is always a footer
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return VIEW_TYPES.Header;
        }else if (isPositionFooter(position)) {
            return VIEW_TYPES.Footer;
        }
        return VIEW_TYPES.Normal;
    }

    private boolean isPositionHeader(int position) {
        return false;
    } // not provided

    private boolean isPositionFooter(int position) {
        return position == items.size();
    }

    public class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    protected void doOtherStuffBeforeItemRemoved() {}

    public void removeItem(T element) {
        int position = items.indexOf(element);
        items.remove(position);
        doOtherStuffBeforeItemRemoved();
        notifyItemRemoved(position);
    }
}

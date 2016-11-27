package com.ted_developers.triviapatente.app.views.expandable_list;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import java.util.List;

/**
 * Created by Antonio on 12/11/16.
 */
public class TPExpandableListAdapter<T> extends RecyclerView.Adapter {
    Context context;
    List<T> items;
    int layout, min_footer_height;
    Class<? extends TPHolder<T>> holderClass;
    String footer;
    TPExpandableList<T> expandableList;

    public TPExpandableListAdapter(Context context, List<T> list, int layout, Class<? extends TPHolder<T>> holderClass, String footer, TPExpandableList<T> expandableList) {
        this.context = context;
        this.items = list;
        this.layout = layout;
        this.holderClass = holderClass;
        this.min_footer_height = (int) context.getResources().getDimension(R.dimen.min_footer_height);
        this.footer = footer;
        this.expandableList = expandableList;
    }

    private int computeFooterHeight() {
        int height = expandableList.maximizedHeight - ((expandableList.elementHeight * (getItemCount() - 1)) - expandableList.listView.computeVerticalScrollOffset()) - expandableList.titleHeight;
        if(height > 0 && height < min_footer_height) {
            height = min_footer_height;
        }
        return height;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case VIEW_TYPES.Footer: return new TPFooter(new TextView(context));
            default: {
                v = LayoutInflater.from(context).inflate(layout, null);
                try {
                    return holderClass.getConstructor(View.class, Context.class, TPExpandableList.class).newInstance(v, context, expandableList);
                } catch (Exception e) {}
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TableRow.LayoutParams params;
        switch (getItemViewType(position)) {
            case VIEW_TYPES.Footer: {
                ((TPFooter) holder).bind(new Pair<String, Integer>(footer, getItemCount()));
                params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, computeFooterHeight());
            } break;
            default: {
                ((TPHolder) holder).bind(getNormalItem(position));
                params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, expandableList.elementHeight);
            }
        }
        holder.itemView.setLayoutParams(params);
    }

    public T getNormalItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
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
    }

    private boolean isPositionFooter(int position) {
        return position == items.size();
    }

    public class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    public int removeItem(T element) {
        int position = items.indexOf(element);
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
        return items.size();
    }
}

package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPFooter;

import java.util.List;

/**
 * Created by Antonio on 28/11/16.
 */
// TODO CODE CLEAN UP AND LIBRARY ON GITHUB
public class TPListAdapter<T> extends RecyclerView.Adapter {
    protected Context context;
    public List<T> items;
    @LayoutRes int holderLayout, footerLayout;
    public int min_footer_height, elementHeight;
    protected Class<? extends TPFooter> footerClass;
    protected Class<? extends TPHolder<T>> holderClass;
    protected RecyclerView recyclerView;
    protected int extraElements;
    public ComputeFooterHeightManager computeFooterHeightManager = new ComputeFooterHeightManager();

    public TPListAdapter(Context context, List<T> list,
                         @LayoutRes int holderLayout, Class<? extends TPHolder<T>> holderClass,
                         @LayoutRes int footerLayout, Class<? extends TPFooter> footerClass,
                         int elementHeight, RecyclerView recyclerView) {
        this.context = context;
        this.items = list;
        this.holderLayout = holderLayout;
        this.holderClass = holderClass;
        this.footerLayout = footerLayout;
        extraElements = (footerLayout == 0)? 0 : 1;
        this.footerClass = (footerClass == null)? TPFooter.class : footerClass;
        this.min_footer_height = (int) context.getResources().getDimension(R.dimen.min_footer_height);
        this.elementHeight = elementHeight;
        this.recyclerView = recyclerView;
    }

    protected RecyclerView.ViewHolder createFooterHolder() {
        try {
            return footerClass.getConstructor(View.class).newInstance(LayoutInflater.from(context).inflate(footerLayout, null));
        } catch (Exception e){ return null; }
    }

    protected RecyclerView.ViewHolder createNormalHolder() {
        View v = LayoutInflater.from(context).inflate(holderLayout, null);
        try {
            return holderClass.getConstructor(View.class).newInstance(v);
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
                params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, (elementHeight==0)?ViewGroup.LayoutParams.WRAP_CONTENT:elementHeight);
            }
        }
        holder.itemView.setLayoutParams(params);
    }

    public T getNormalItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size() + extraElements; // there is always a footer
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
        return (footerLayout == 0)? false : position == items.size();
    }

    public class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    // REMOVE ITEM

    public void removeItem(T element) {
        int position = items.indexOf(element);
        items.remove(position);
        doStuffBeforeItemRemoved();
        notifyItemRemoved(position);
        doStuffAfterItemRemoved();
    }

    protected void doStuffBeforeItemRemoved() {}
    protected void doStuffAfterItemRemoved() {}

    // ADD ITEM

    public void addItem(int position) {
        doStuffBeforeItemAdd();
        notifyItemInserted(position);
        doStuffAfterItemAdd();
    }

    public void addItem(T element, int position) {
        items.add(position, element);
        doStuffBeforeItemAdd();
        notifyItemInserted(position);
        doStuffAfterItemAdd();
    }

    protected void doStuffBeforeItemAdd() {}
    protected void doStuffAfterItemAdd() {}

    // FOOTER HEIGHT MANAGEMENT

    protected int computeFooterHeight() {
        return computeFooterHeightManager.computeFooterHeight();
    }

    private int theoreticalFooterHeight() {
        return recyclerView.getHeight() - items.size() * elementHeight + recyclerView.computeVerticalScrollOffset();
    }

    public enum compute_footer_options {
        EXPANDABLE,
        ZERO_ITEMS,
        AT_LEAST_MIN_HEIGHT,
        SAME_HEIGHT
    }

    public class ComputeFooterHeightManager {

        private compute_footer_options selectedOption = compute_footer_options.AT_LEAST_MIN_HEIGHT;

        private final int expandableFooter() {
            int height = theoreticalFooterHeight();
            // if visible at least, show it with min height
            if(height > 0 && height < min_footer_height) {
                height = min_footer_height;
            } else if (height < 0) {
                height = 0;
            }
            return height;
        }

        private final int zeroElementsFooter() {
            return (items.size() == 0)? recyclerView.getHeight() : 0;
        }

        private final int atLeastMinHeight() {
            int height = theoreticalFooterHeight();
            // always shows a footer
            if(height < min_footer_height) {
                height = min_footer_height;
            }
            return height;
        }

        private final int sameHeightFooter() {
            int height = theoreticalFooterHeight();
            // always shows a footer
            if(height < min_footer_height) {
                height = min_footer_height;
            }
            return height;
        }

        public int computeFooterHeight() {
            switch(selectedOption) {
                case EXPANDABLE: return expandableFooter();
                case ZERO_ITEMS: return zeroElementsFooter();
                case AT_LEAST_MIN_HEIGHT: return atLeastMinHeight();
                case SAME_HEIGHT: return sameHeightFooter();
                default: return 0;
            }
        }

        public void setOption(compute_footer_options option) {
            selectedOption = option;
        }
    }
}

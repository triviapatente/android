package com.ted_developers.triviapatente.app.views.expandable_list;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.RecentGameHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.models.game.Game;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Antonio on 12/11/16.
 */
public class TPExpandableListAdapter<T> extends RecyclerView.Adapter {
    Context context;
    List<T> list;
    int layout;
    Class<? extends TPHolder<T>> holderClass;

    public TPExpandableListAdapter(Context context, List<T> list, int layout, Class<? extends TPHolder<T>> holderClass) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        this.holderClass = holderClass;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(layout, null);
        try {
            return holderClass.getConstructor(View.class, Context.class).newInstance(v, context);
        } catch (Exception e) {}
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TPHolder) holder).bind(getItem(position));
    }

    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

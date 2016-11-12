package com.ted_developers.triviapatente.app.utils.custom_classes.listElements;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Antonio on 12/11/16.
 */
public abstract class TPHolder<T> extends RecyclerView.ViewHolder {
    public TPHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element);

}

package it.triviapatente.android.app.utils.custom_classes.listViews.listElements;

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

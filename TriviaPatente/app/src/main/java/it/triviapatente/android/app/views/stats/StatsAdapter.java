package it.triviapatente.android.app.views.stats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.webkit.ValueCallback;

import java.util.List;

import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.StatHolder;
import it.triviapatente.android.models.game.Category;

/**
 * Created by donadev on 31/05/18.
 */

public class StatsAdapter extends RecyclerView.Adapter<StatHolder> {
    private List<Category> categories;
    private ValueCallback<Category> clickCallback;

    public StatsAdapter(List<Category> list, ValueCallback<Category> clickCallback) {
        this.categories = list;
        this.clickCallback = clickCallback;
    }
    public void setItems(List<Category> items) {
        this.categories = items;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return StatHolder.newHolder(viewGroup.getContext(), clickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull StatHolder statHolder, int i) {
        statHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }
}

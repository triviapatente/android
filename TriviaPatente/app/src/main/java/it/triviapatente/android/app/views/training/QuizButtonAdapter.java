package it.triviapatente.android.app.views.training;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import it.triviapatente.android.app.utils.custom_classes.callbacks.ValuesCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizButtonHolder;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 29/06/18.
 */

public class QuizButtonAdapter extends BaseAdapter {
    private int size = 0;
    private ValuesCallback<Integer, Button> onClickListener;
    private List<Quiz> quizzes;
    private Context context;
    public List<QuizButtonHolder> holders = new ArrayList<>();
    public QuizButtonAdapter(Context ctx, ValuesCallback<Integer, Button> onClickListener) {
        this.context = ctx;
        this.onClickListener = onClickListener;
    }
    public void initData(List<Quiz> quizzes) {
        this.quizzes = quizzes;
        for(Quiz q : quizzes) holders.add(null);
    }
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }
    public int getViewWidth() {
        return 50;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return this.quizzes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.quizzes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.quizzes.get(i).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        QuizButtonHolder holder = QuizButtonHolder.newHolder(context, onClickListener);
        holders.set(i, holder);
        holder.bind(i, (Quiz)getItem(i));
        return holder.itemView;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return this.quizzes.isEmpty();
    }
}

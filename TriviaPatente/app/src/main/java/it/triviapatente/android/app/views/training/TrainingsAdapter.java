package it.triviapatente.android.app.views.training;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleItemCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.TrainingHolder;
import it.triviapatente.android.models.training.Training;

/**
 * Created by donadev on 16/03/18.
 */

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingHolder> {

    private List<Training> trainings;
    private Context ctx;
    private SimpleItemCallback<Training> mItemClickListener;

    public void setOnItemClickListener(SimpleItemCallback<Training> callback) {
        this.mItemClickListener = callback;
    }

    public TrainingsAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TrainingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return TrainingHolder.newHolder(ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingHolder trainingHolder, int i) {
        Training t = trainings.get(i);
        trainingHolder.bind(t, ctx, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }
}

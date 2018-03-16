package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import it.triviapatente.android.models.training.Training;

/**
 * Created by donadev on 16/03/18.
 */

public class TrainingHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.trainingErrorsPreview) Button trainingErrorsPreview;

    public TrainingHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
    public static TrainingHolder newHolder(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.training_errors_preview_holder, null, false);
        return new TrainingHolder(v);
    }
    private Drawable getDrawableFor(Training training, Context ctx) {
        return ResourcesCompat.getDrawable(ctx.getResources(), getDrawableFor(training), null);
    }
    private int getDrawableFor(Training training) {
        if(training.numberOfErrors == 0) return R.drawable.training_noerrors;
        else if(training.numberOfErrors > 0 && training.numberOfErrors <= 2) return R.drawable.training_12_errors;
        else if(training.numberOfErrors > 2 && training.numberOfErrors < 4) return R.drawable.training_34_errors;
        else return R.drawable.training_more_errors;
    }
    public void bind(Training training, Context ctx) {
        trainingErrorsPreview.setBackground(getDrawableFor(training, ctx));
        if(training.numberOfErrors == 0) {
            trainingErrorsPreview.setText(null);
        } else {
            trainingErrorsPreview.setText("" + training.numberOfErrors);
        }
    }
}

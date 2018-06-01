package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import it.triviapatente.android.models.game.Category;

/**
 * Created by donadev on 31/05/18.
 */

public class StatHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.categoryNameView) TextView categoryNameView;
    @BindView(R.id.categoryPercentView) TextView categoryPercentView;
    @BindView(R.id.categoryInfoView) TextView categoryInfoView;
    @BindView(R.id.cellView)
    RelativeLayout cellView;
    private Context context;
    private ValueCallback<Category> onSelectItem;

    public static StatHolder newHolder(Context context, ValueCallback<Category> onSelectItem) {
        View v = LayoutInflater.from(context).inflate(R.layout.stats_item_layout, null, false);
        return new StatHolder(v, onSelectItem);

    }
    private int getBackground(Category category) {
        if(category.getProgress() < 75) return R.drawable.button_stats_bad;
        if(category.getProgress() < 90) return R.drawable.button_stats_medium;
        if(category.getProgress() < 95) return R.drawable.button_stats_good;
        return R.drawable.button_stats_perfect;
    }
    public StatHolder(View itemView, ValueCallback<Category> onSelectItem) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        this.onSelectItem = onSelectItem;
    }

    public void bind(final Category category) {
        categoryNameView.setText(category.hint);
        categoryPercentView.setText(category.getProgress() + "%");
        String infoText = context.getString(category.total_answers == 1 ? R.string.stats_answered_question : R.string.stats_answered_questions);
        categoryInfoView.setText(infoText.replace("%d", "" + category.total_answers));
        cellView.setBackgroundResource(getBackground(category));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectItem.onReceiveValue(category);
            }
        });
    }
}

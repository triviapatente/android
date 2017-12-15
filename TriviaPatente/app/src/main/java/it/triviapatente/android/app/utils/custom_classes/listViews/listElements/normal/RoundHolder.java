package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.custom_classes.callbacks.RoundDetailsSectionCallback;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by donadev on 23/11/17.
 */

public class RoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.round_number_view) TextView roundNumberView;
    @BindColor(R.color.mainColor) @ColorInt int mainColor;

    private RoundDetailsSectionCallback clickCallback;
    private RecyclerView.Adapter<RoundHolder> mAdapter;
    private static Integer selectedHolder = 0;

    public static RoundHolder newHolder(Context context, RecyclerView.Adapter<RoundHolder> adapter) {
        View v = LayoutInflater.from(context).inflate(R.layout.gamedetails_round_view_holder, null, false);
        return new RoundHolder(v, adapter);

    }
    public static void finalizeHolder() {
        selectedHolder = 0;
    }
    public RoundHolder(View itemView, RecyclerView.Adapter<RoundHolder> adapter) {
        super(itemView);
        mAdapter = adapter;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String content, RoundDetailsSectionCallback clickCallback) {
        setSelected(getAdapterPosition() == selectedHolder);
        this.clickCallback = clickCallback;
        roundNumberView.setText(content);
    }
    private void setSelected(Boolean selected) {
        if(selected) {
            roundNumberView.setBackgroundResource(R.drawable.circle_selected_background);
            roundNumberView.setTextColor(mainColor);
        } else {
            roundNumberView.setBackgroundResource(R.drawable.circle_unselected_background);
            roundNumberView.setTextColor(Color.WHITE);
        }
    }
    public void select(Boolean needsScroll) {
        if(getAdapterPosition() == selectedHolder) return;
        selectedHolder = getAdapterPosition();
        mAdapter.notifyDataSetChanged();
        clickCallback.onSelected(selectedHolder, needsScroll);
    }

    @Override
    public void onClick(View v) {
        select(true);
    }
}

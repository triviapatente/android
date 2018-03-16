package it.triviapatente.android.app.views.training;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;

public class TrainingStatViewer extends Fragment {
    @BindView(R.id.trainingStatsNumberViewer) TextView numberViewer;
    @BindView(R.id.trainingStatsCaptionViewer) TextView captionViewer;

    public TrainingStatViewer() {
        // Required empty public constructor
    }
    public void setValues(int number, String caption) {
        this.numberViewer.setText(String.format(Locale.getDefault(), "%d", number));
        this.captionViewer.setText(caption);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_training_stat_viewer, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

}

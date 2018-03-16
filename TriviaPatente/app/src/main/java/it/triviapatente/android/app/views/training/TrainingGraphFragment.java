package it.triviapatente.android.app.views.training;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.custom_classes.animation.ExpandAnimation;
import it.triviapatente.android.models.auth.TrainingStats;
import it.triviapatente.android.models.training.Training;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingGraphFragment extends Fragment {
    @BindViews({R.id.noErrorsSection, R.id.onetwoErrorsSection, R.id.threefourErrorsSection, R.id.moreErrorsSection}) FrameLayout[] sections;

    private final int ANIMATION_DURATION = 500;
    public TrainingGraphFragment() {
        // Required empty public constructor
    }

    private float getSectionWeight(TrainingStats stats, FrameLayout section) {
        if(stats.total == 0) return 0;
        switch (section.getId()) {
            case R.id.noErrorsSection: return (float)stats.no_errors / stats.total;
            case R.id.onetwoErrorsSection: return (float)stats.errors_12 / stats.total;
            case R.id.threefourErrorsSection: return (float)stats.errors_34 / stats.total;
            case R.id.moreErrorsSection: return (float)stats.more_errors / stats.total;
            default: return 0;
        }
    }
    public void initLayoutWeight(FrameLayout layout) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
        params.weight = 0;
        layout.setLayoutParams(params);
    }
    private void initWeights() {
        for(FrameLayout f : sections) {
            initLayoutWeight(f);
        }
    }
    public void setStats(TrainingStats stats) {
        initWeights();
        for(FrameLayout f : sections) {
            float weight = getSectionWeight(stats, f);
            Animation a = new ExpandAnimation(f, 0, weight);
            a.setDuration(ANIMATION_DURATION);
            f.startAnimation(a);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_training_graph, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

}

package it.triviapatente.android.app.utils.custom_classes.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by donadev on 16/03/18.
 */

public class ExpandAnimation extends Animation {

    private final float mStartWeight;
    private final float mDeltaWeight;
    private View mView;

    public ExpandAnimation(View view, float startWeight, float endWeight) {
        mView = view;
        mStartWeight = startWeight;
        mDeltaWeight = endWeight - startWeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mView.getLayoutParams();
        lp.weight = (mStartWeight + (mDeltaWeight * interpolatedTime));
        mView.setLayoutParams(lp);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}

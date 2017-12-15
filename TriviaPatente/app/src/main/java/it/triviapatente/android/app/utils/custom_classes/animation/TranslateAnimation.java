package it.triviapatente.android.app.utils.custom_classes.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by Antonio on 30/12/16.
 */
public class TranslateAnimation extends Animation {
    private View mView;
    private float fromMarginLeft, fromMarginTop, dx, dy;
    private LinearLayout.LayoutParams params;

    public TranslateAnimation(View v, float fromMarginLeft, float toMarginLeft, float fromMarginTop, float toMarginTop) {
        mView = v;
        this.fromMarginLeft = fromMarginLeft;
        this.fromMarginTop = toMarginTop;
        this.dx = toMarginLeft - fromMarginLeft;
        this.dy = toMarginTop - fromMarginTop;
        params = (LinearLayout.LayoutParams) mView.getLayoutParams();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        params.leftMargin = (int) (interpolatedTime * dx + fromMarginLeft);
        params.topMargin = (int) (interpolatedTime * dy + fromMarginTop);
        mView.requestLayout();
    }
}

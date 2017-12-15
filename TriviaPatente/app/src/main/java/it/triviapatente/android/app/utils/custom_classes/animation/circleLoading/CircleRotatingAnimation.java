package it.triviapatente.android.app.utils.custom_classes.animation.circleLoading;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.Random;

/**
 * Created by Antonio on 08/12/16.
 */
public class CircleRotatingAnimation extends Animation {
    private Circle circle;
    private static float oldStartOver = 0, multiplier = (float) 1.5;
    private Random random = new Random();

    private float inc = 0, possibleInc, oldInterpolatedTime;

    public CircleRotatingAnimation(Circle circle) {
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        possibleInc = (interpolatedTime - oldInterpolatedTime) * 360;
        oldInterpolatedTime = interpolatedTime;
        if(inc < possibleInc && possibleInc < 3) {
            inc = possibleInc;
        }
        if(circle.getSweepAngleOver() >= 360) {
            multiplier = - multiplier;
        } else if(circle.getSweepAngleOver() <= 0){
            multiplier = 1 + random.nextFloat();
        }
        circle.setSweepAngleOver(circle.getSweepAngleOver() + inc * multiplier);
        circle.setStartingAngleOver(inc + oldStartOver);
        oldStartOver = inc + oldStartOver;
        // refresh
        circle.requestLayout();
    }
}
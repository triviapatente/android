package com.ted_developers.triviapatente.app.utils.custom_classes.circleLoading;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.Random;

/**
 * Created by Antonio on 08/12/16.
 */
public class CircleRotatingAnimation extends Animation {
    private Circle circle;
    private static float inc = 0, oldStartOver = 0, multiplier = (float) 1.5;
    private Random random = new Random();

    public CircleRotatingAnimation(Circle circle) {
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        if(inc == 0) {
            inc = interpolatedTime * 360;
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
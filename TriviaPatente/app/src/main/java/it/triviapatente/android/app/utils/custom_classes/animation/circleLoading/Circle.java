package it.triviapatente.android.app.utils.custom_classes.animation.circleLoading;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import it.triviapatente.android.R;

/**
 * Created by Antonio on 08/12/16.
 */
public class Circle extends View {

    private float startingAngleOver = 0, sweepAngleOver = 0, stroke_width = 20;
    private Paint paintOver = null, paintUnder = null;
    private RectF rect = null;
    private int colorOver, colorUnder;

    public Circle(Context context) {
        super(context);
        init();
    }

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Circle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Circle,
                0, 0);
        try {
            stroke_width = a.getDimension(R.styleable.Circle_stroke_width, 20);
            colorOver = a.getColor(R.styleable.Circle_circle_colorOver, Color.GREEN);
            colorUnder = a.getColor(R.styleable.Circle_circle_colorUnder, Color.RED);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        // circle over
        paintOver = new Paint();
        paintOver.setAntiAlias(true);
        paintOver.setStyle(Paint.Style.STROKE);
        paintOver.setStrokeWidth(stroke_width);
        paintOver.setColor(colorOver);
        // circle under
        paintUnder = new Paint();
        paintUnder.setAntiAlias(true);
        paintUnder.setStyle(Paint.Style.STROKE);
        paintUnder.setStrokeWidth(stroke_width);
        paintUnder.setColor(colorUnder);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rect == null) {
            float s1 = stroke_width / 2, s2 = canvas.getHeight() - stroke_width / 2 + 1;
            rect = new RectF(s1, s1, s2, s2);
        }
        canvas.drawArc(rect, 0, 360, false, paintUnder);
        canvas.drawArc(rect, startingAngleOver, sweepAngleOver, false, paintOver);
    }

    public float getSweepAngleOver() {
        return sweepAngleOver;
    }

    public void setSweepAngleOver(float angle) {
        this.sweepAngleOver = angle;
    }
    public void setStartingAngleOver(float angle) {
        this.startingAngleOver = angle;
    }

    public void setColorOver(int color) {
        this.colorOver = color;
        paintOver.setColor(color);
    }
    public void setColorUnder(int color) {
        this.colorUnder = color;
        paintUnder.setColor(color);
    }
}

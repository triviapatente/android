package it.triviapatente.android.app.utils.custom_classes.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import it.triviapatente.android.app.utils.TPUtils;

/**
 * Created by Antonio on 12/11/16.
 */
public class RoundedImageView extends android.support.v7.widget.AppCompatImageView {

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Integer borderColor;
    private Integer borderWidth;
    private Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Integer DEFAULT_BORDER_WIDTH = 3;


    public void setBorder(Integer color, Integer width) {
        this.borderColor = color;
        this.borderWidth = width;
    }
    public void setBorder(Integer color) {
        setBorder(color, DEFAULT_BORDER_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        //Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap b = TPUtils.drawableToBitmap(drawable);
        if(b == null) return;
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        int dim = Math.min(getWidth(), getHeight()) - 1;

        Bitmap roundBitmap = getCroppedBitmap(bitmap, dim);

        fillPaint.setStyle(Paint.Style.FILL);

        canvas.drawBitmap(roundBitmap, 0, 0, fillPaint);
        if(borderColor != null) {
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setColor(borderColor);
            float radius = dim / 2;
            canvas.drawCircle(radius, radius, radius - (borderWidth / 2), borderPaint);
        }

    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth() / factor), (int)(bmp.getHeight() / factor), false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

}

package it.triviapatente.android.app.utils.custom_classes.images;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by donadev on 12/03/18.
 */

public class TPExpandableImageView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener{
    private void init() {
        setOnClickListener(this);
    }

    public TPExpandableImageView(Context context) {
        super(context);
        init();
    }

    public TPExpandableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TPExpandableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private Drawable mDrawable;

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        mDrawable = drawable;
    }

    @Override
    public void onClick(View view) {
        Bitmap bmp = ((BitmapDrawable)mDrawable).getBitmap();
        TPFullScreenImageView.mImage = bmp;
        Context ctx = getContext();
        if(ctx != null) {
            Intent i = new Intent(ctx, TPFullScreenImageView.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) ctx, this, "expand");
            ctx.startActivity(i, options.toBundle());
        }
    }
}

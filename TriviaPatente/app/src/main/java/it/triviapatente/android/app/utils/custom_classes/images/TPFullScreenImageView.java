package it.triviapatente.android.app.utils.custom_classes.images;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.triviapatente.android.R;
import ooo.oxo.library.widget.PullBackLayout;
import ooo.oxo.library.widget.TouchImageView;

/**
 * Created by donadev on 12/03/18.
 */

public class TPFullScreenImageView extends AppCompatActivity implements PullBackLayout.Callback {
    public static Bitmap mImage;
    @BindView(R.id.fullScreenImageView) TouchImageView imageView;
    @BindView(R.id.parent_layout) PullBackLayout parentLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_fullscreen_image_view);
        ButterKnife.bind(this);
        parentLayout.setCallback(this);
        imageView.setImageBitmap(mImage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isChangingConfigurations()) mImage = null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float v) {

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }
}
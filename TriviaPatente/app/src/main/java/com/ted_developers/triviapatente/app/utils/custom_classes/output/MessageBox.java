package com.ted_developers.triviapatente.app.utils.custom_classes.output;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 30/10/16.
 */
public class MessageBox extends LinearLayout {
    private ImageView image;
    private TextView message;

    public MessageBox(Context context) {
        super(context);
        init(context);
    }

    public MessageBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MessageBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutParams params;
        int margin = (int) getResources().getDimension(R.dimen.alertbox_inner_padding);
        // icon of message box
        image = new ImageView(context);
        // default image
        image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rounded_alertbox_image));
        // layout params
        int imageSize = (int) getResources().getDimension(R.dimen.alert_image_size);
        params = new LayoutParams(imageSize, imageSize);
        params.setMargins(2*margin, 0, margin, 0);
        params.gravity = Gravity.CENTER;
        image.setLayoutParams(params);
        this.addView(image);
        // message of message box
        message = new TextView(context);
        message.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.TPTextSizeSmall));
        message.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        // layout params
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(margin, 0, margin, 0);
        message.setLayoutParams(params);
        message.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(message);
    }

    public void setStyle(int ResId) {
        TextViewCompat.setTextAppearance(message, ResId);
    }

    public void setImage(Drawable img) {
        image.setImageDrawable(img);
    }

    public void setMessage(String text) {
        message.setText(text);
    }

    // show alert with given message
    public void showAlert(String alertMessage) {
        // show alert
        if (getVisibility() == View.GONE) {
            setMessage(alertMessage);
            setVisibility(View.VISIBLE);
        }
    }

    // hide alert
    public void hideAlert() {
        // hide alert
        if (getVisibility() == View.VISIBLE) {
            setVisibility(View.GONE);
        }
    }
}

package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.util.Pair;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.expandable_list.ResizeAnimation;

/**
 * Created by Antonio on 13/11/16.
 */
public class TPFooter extends TPHolder<Void> {
    public TPFooter(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Void element) {}

    public static void expand(View footer, int toHeight) {
        footer.startAnimation(new ResizeAnimation(footer, footer.getWidth(), footer.getHeight(), footer.getWidth(), toHeight));
    }
}

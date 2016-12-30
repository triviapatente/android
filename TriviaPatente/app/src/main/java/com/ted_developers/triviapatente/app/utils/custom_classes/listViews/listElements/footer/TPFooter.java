package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer;

import android.view.View;

import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.animation.ResizeAnimation;

/**
 * Created by Antonio on 13/11/16.
 */
public class TPFooter extends TPHolder<Void> {
    public TPFooter(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Void element) {}

    public static void expand(View footer, int toHeight, int waitTime, int animTime) {
        if(footer != null && footer.getVisibility() != View.GONE) {
            ResizeAnimation anim = new ResizeAnimation(footer, footer.getWidth(), footer.getHeight(), footer.getWidth(), toHeight);
            anim.setDuration(waitTime - 50); // 100 --> tollerance
            anim.setStartOffset(animTime);
            footer.startAnimation(anim);
        }
    }
}

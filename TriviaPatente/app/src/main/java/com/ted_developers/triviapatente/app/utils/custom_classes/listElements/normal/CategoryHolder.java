package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.models.game.Category;

/**
 * Created by Antonio on 11/12/16.
 */
public class CategoryHolder extends TPHolder<Category> {
    ImageView categoryImage;
    TextView categoryTitle;

    public CategoryHolder(View itemView) {
        super(itemView);
        bindElements();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo open next page
            }
        });
    }

    private void bindElements() {
        categoryImage = (ImageView) itemView.findViewById(R.id.image);
        categoryTitle = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void bind(Category element) {
        // todo do dinamically
        Drawable d = ContextCompat.getDrawable(itemView.getContext(), R.drawable.no_image);
        categoryImage.setImageDrawable(d);
        categoryTitle.setText(element.name);
    }
}

package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.models.game.Category;

/**
 * Created by Antonio on 11/12/16.
 */
public class CategoryHolder extends TPHolder<Category> {
    ImageView categoryImage;
    TextView categoryTitle;
    View separator;
    Integer numberOfProposedCategories;

    public CategoryHolder(final View itemView) {
        super(itemView);
        bindElements();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo open next page
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), AlphaView.class);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    private void bindElements() {
        categoryImage = (ImageView) itemView.findViewById(R.id.image);
        categoryTitle = (TextView) itemView.findViewById(R.id.title);
        separator = itemView.findViewById(R.id.separator);
        numberOfProposedCategories = itemView.getContext().getResources().getInteger(R.integer.number_of_proposed_categories);
    }

    @Override
    public void bind(Category element) {
        // todo do dinamically
        Drawable d = ContextCompat.getDrawable(itemView.getContext(), R.drawable.no_image);
        categoryImage.setImageDrawable(d);
        categoryTitle.setText(element.hint);
        if(getAdapterPosition() == numberOfProposedCategories - 1) {
            separator.setVisibility(View.GONE);
        }
    }
}

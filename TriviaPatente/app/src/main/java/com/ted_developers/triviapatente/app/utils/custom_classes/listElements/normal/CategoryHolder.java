package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.game_page.ChooseCategoryActivity;
import com.ted_developers.triviapatente.app.views.game_page.PlayRoundActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.game.Round;

/**
 * Created by Antonio on 11/12/16.
 */
public class CategoryHolder extends TPHolder<Category> {
    ImageView categoryImage;
    LinearLayout backgroundLayer;
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
        backgroundLayer = (LinearLayout) itemView.findViewById(R.id.backgroundLayer);
        categoryTitle = (TextView) itemView.findViewById(R.id.title);
        separator = itemView.findViewById(R.id.separator);
        numberOfProposedCategories = itemView.getContext().getResources().getInteger(R.integer.number_of_proposed_categories);
    }

    @Override
    public void bind(final Category element) {
        // todo do dinamically
        Drawable d = ContextCompat.getDrawable(itemView.getContext(), R.drawable.no_image);
        categoryImage.setImageDrawable(d);
        Drawable backgroundLayerDrawable = backgroundLayer.getBackground();
        if (backgroundLayerDrawable instanceof ShapeDrawable) {
            ((ShapeDrawable)backgroundLayerDrawable).getPaint().setColor(Color.parseColor(element.color));
        } else if (backgroundLayerDrawable instanceof GradientDrawable) {
            ((GradientDrawable)backgroundLayerDrawable).setColor(Color.parseColor(element.color));
        } else if (backgroundLayerDrawable instanceof ColorDrawable) {
            ((ColorDrawable)backgroundLayerDrawable).setColor(Color.parseColor(element.color));
        }
        //backgroundLayer.setBackground(backgroundLayerDrawable);
        categoryTitle.setText(element.hint);
        if(getAdapterPosition() == numberOfProposedCategories - 1) {
            separator.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChooseCategoryActivity) itemView.getContext()).chooseCategory(element);
            }
        });
    }
}

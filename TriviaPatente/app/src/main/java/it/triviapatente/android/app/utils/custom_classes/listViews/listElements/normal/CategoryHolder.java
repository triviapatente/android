package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.TPHolder;
import it.triviapatente.android.app.views.AlphaView;
import it.triviapatente.android.app.views.game_page.ChooseCategoryActivity;
import it.triviapatente.android.models.game.Category;

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
        Context context = itemView.getContext();
        TPUtils.picasso
                .load(TPUtils.getCategoryImageFromID(context, element.id))
                .error(R.drawable.image_no_image_found)
                .into(categoryImage);
        Drawable backgroundLayerDrawable = backgroundLayer.getBackground();
        if (backgroundLayerDrawable instanceof ShapeDrawable) {
            ((ShapeDrawable)backgroundLayerDrawable).getPaint().setColor(Color.parseColor(element.color));
        } else if (backgroundLayerDrawable instanceof GradientDrawable) {
            ((GradientDrawable)backgroundLayerDrawable).setColor(Color.parseColor(element.color));
        } else if (backgroundLayerDrawable instanceof ColorDrawable) {
            ((ColorDrawable)backgroundLayerDrawable).setColor(Color.parseColor(element.color));
        }
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

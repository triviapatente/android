package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.drawer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;

import java.util.List;

/**
 * Created by Antonio on 26/10/17.
 */

public class TPDrawerAdapter extends ArrayAdapter<DrawerOption> {

    public TPDrawerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TPDrawerAdapter(Context context, int resource, List<DrawerOption> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        DrawerOption option = getItem(position);

        if(option != null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            if(option.optionType == drawer_options_type.image) {
                v = vi.inflate(R.layout.drawer_picture_element, null);
                if(option.user.name != null || option.user.surname != null) {
                    ((TextView) v.findViewById(R.id.name)).setText(option.user.name + " " + option.user.surname);
                    v.findViewById(R.id.name).setVisibility(View.VISIBLE);
                }
                ((TextView) v.findViewById(R.id.username)).setText("@" + option.user.username);
                TPUtils.picasso
                        .load(TPUtils.getUserImageFromID(getContext(), option.user.id))
                        .placeholder(R.drawable.image_no_profile_picture)
                        .error(R.drawable.image_no_profile_picture)
                        .into((RoundedImageView) v.findViewById(R.id.bigProfilePicture));
            } else {
                v = vi.inflate(R.layout.drawer_list_item, null);
                if(position == getCount() - 1) v.findViewById(R.id.separator).setVisibility(View.GONE);
                ((ImageView) v.findViewById(R.id.optionImage)).setImageDrawable(ContextCompat.getDrawable(getContext(), option.drawableResource));
                ((TextView) v.findViewById(R.id.optionName)).setText(option.optionName);
            }
        }





        return v;
    }

}
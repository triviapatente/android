package it.triviapatente.android.app.utils.custom_classes.listViews.adapters.drawer;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.models.auth.User;

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
                User user = SharedTPPreferences.currentUser();
                if(user != null) {
                    v = vi.inflate(R.layout.drawer_picture_element, null);
                    if (user.name != null || user.surname != null) {
                        ((TextView) v.findViewById(R.id.name)).setText(user.name + " " + user.surname);
                        v.findViewById(R.id.name).setVisibility(View.VISIBLE);
                    }
                    ((TextView) v.findViewById(R.id.username)).setText(user.username);
                    TPUtils.injectUserImage(getContext(), user, (RoundedImageView) v.findViewById(R.id.bigProfilePicture));
                }
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
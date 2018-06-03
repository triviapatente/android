package it.triviapatente.android.app.views.game_page;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.triviapatente.android.R;

/**
 * Created by gabrielciulei on 02/06/2018.
 */

public class InstagramFeedItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.content_instagram_feed_item, container, false);

        return rootView;
    }
}

package it.triviapatente.android.app.views.game_page.instagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.models.base.IGImage;

/**
 * Created by gabrielciulei on 02/06/2018.
 */

public class InstagramFeedItemFragment extends Fragment {
    @BindView(R.id.imageView) ImageView imageView;
    private IGImage image;

    @OnClick(R.id.imageView)
    public void goToImage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(image.resourceLink));
        startActivity(browserIntent);
    }


    public static InstagramFeedItemFragment newInstance(IGImage image) {
        InstagramFeedItemFragment output = new InstagramFeedItemFragment();
        output.image = image;
        return output;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.content_instagram_feed_item, container, false);
        ButterKnife.bind(this, rootView);
        TPUtils.picasso.load(image.preview).into(imageView);
        return rootView;
    }
}

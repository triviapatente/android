package com.ted_developers.triviapatente.app.views.game_page;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentGameHeader extends Fragment {
    protected @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    protected @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    protected @BindView(R.id.subtitleImage) ImageView gameHeaderSubtitleImage;

    public FragmentGameHeader() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_header, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setHeader(Round round, Category category) {
        setHeader(round, category, true);
    }
    public void endedGameHeader() {
        gameHeaderTitle.setText("Fine");
        gameHeaderSubtitle.setText("Risultato partita");
        gameHeaderSubtitleImage.setVisibility(View.GONE);
    }

    public void setHeader(Round round, Category category, boolean waiting) {
        gameHeaderTitle.setText((round == null)? getString(R.string.game_header_waiting_title) : "Round " + round.number);
        gameHeaderSubtitle.setText((category == null)? (waiting)? getString(R.string.game_header_waiting_subtitle) : getString(R.string.choose_category_game_header_subtitle) : category.hint);
        if(category == null) {
            gameHeaderSubtitleImage.setVisibility(View.GONE);
        } else {
            TPUtils.picasso
                    .load(TPUtils.getCategoryImageFromID(getContext(), category.id))
                    .error(R.drawable.image_no_image_found)
                    .into(gameHeaderSubtitleImage);
            gameHeaderSubtitleImage.setVisibility(View.VISIBLE);
        }
        gameHeaderSubtitle.setSelected(true);
    }
}

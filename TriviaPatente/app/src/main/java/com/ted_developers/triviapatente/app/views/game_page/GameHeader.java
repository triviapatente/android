package com.ted_developers.triviapatente.app.views.game_page;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Round;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameHeader extends Fragment {
    protected @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    protected @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    protected @BindView(R.id.subtitleImage) ImageView gameHeaderSubtitleImage;

    public GameHeader() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_header, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setHeader(Round round, Category category) {
        gameHeaderTitle.setText((round == null)? getString(R.string.game_header_waiting_title) : "Round " + round.number);
        gameHeaderSubtitle.setText((category == null)? getString(R.string.game_header_waiting_subtitle) : category.hint);
        if(category == null || category.imagePath == null || "".equals(category.imagePath)) {
            gameHeaderSubtitleImage.setVisibility(View.GONE);
        } else {
            // todo get dinamically
            gameHeaderSubtitleImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.image_no_profile_picture));
            gameHeaderSubtitleImage.setVisibility(View.VISIBLE);
        }
        gameHeaderSubtitle.setSelected(true);
    }
}

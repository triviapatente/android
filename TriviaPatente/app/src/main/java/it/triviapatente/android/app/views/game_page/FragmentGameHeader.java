package it.triviapatente.android.app.views.game_page;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindInt;
import butterknife.BindString;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Round;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentGameHeader extends Fragment {
    protected @BindView(R.id.gameHeaderTitle) TextView gameHeaderTitle;
    protected @BindView(R.id.gameHeaderSubtitle) TextView gameHeaderSubtitle;
    protected @BindView(R.id.subtitleImage) ImageView gameHeaderSubtitleImage;

    protected @BindInt(R.integer.number_of_rounds) int numberOfRounds;

    protected @BindString(R.string.activity_wait_page_game_ended) String gameEndedTitle;
    protected @BindString(R.string.activity_wait_page_game_ended_subtitle) String gameEndedSubtitle;

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
        gameHeaderTitle.setText(gameEndedTitle);
        gameHeaderSubtitle.setText(gameEndedSubtitle);
        gameHeaderSubtitleImage.setVisibility(View.GONE);
    }

    public void setHeader(Round round, Category category, boolean waiting) {
        String gameHeaderString = "Round " + String.valueOf((round == null)? numberOfRounds : round.number);
        gameHeaderTitle.setText(gameHeaderString);
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

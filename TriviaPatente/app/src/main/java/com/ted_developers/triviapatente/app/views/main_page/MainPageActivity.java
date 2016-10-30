package com.ted_developers.triviapatente.app.views.main_page;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.button.main.MainButton;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity {
    // buttons name
    @BindString(R.string.button_new_game) String newGame;
    @BindString(R.string.button_rank) String rank;
    @BindString(R.string.button_stats) String stats;
    @BindString(R.string.button_shop) String shop;
    // options button
    @BindView(R.id.new_game) MainButton buttonNewGame;
    @BindView(R.id.stats) MainButton buttonShowStats;
    @BindView(R.id.shop) MainButton buttonShop;
    @BindView(R.id.rank) MainButton buttonShowRank;
    // buttons image
    @BindDrawable(R.drawable.chart_line) Drawable statsImage;
    @BindDrawable(R.drawable.trophy) Drawable rankImage;
    @BindDrawable(R.drawable.car) Drawable newGameImage;
    @BindDrawable(R.drawable.heart) Drawable shopImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        // new game
        buttonNewGame.setButtonText(newGame, Color.WHITE);
        buttonNewGame.setImage(newGameImage);
        // TODO get hints

        // rank
        buttonShowRank.setButtonText(rank, Color.WHITE);
        buttonShowRank.setImage(rankImage);
        // TODO get hints

        // stats
        buttonShowStats.setButtonText(stats, Color.WHITE);
        buttonShowStats.setImage(statsImage);
        // TODO get hints
        String[] prova = {
                "012",
                "0123",
                "01234"
        };
        buttonShowStats.setActivity(this);
        buttonShowStats.setHintText(prova, Color.WHITE);

        // shop
        buttonShop.setButtonText(shop, Color.WHITE);
        buttonShop.setImage(shopImage);
        // TODO get hints
    }
}

package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.PlayButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 12/11/16.
 */
public class RecentGameHolder extends TPHolder<Game> {
    // status text view
    private TextView statusText;
    // player name
    private TextView usernameText;
    // traffic lights
    private ImageView trafficLights;
    // profile picture
    private RoundedImageView profilePicture;
    // play button
    private PlayButton playButton;
    // context
    Context context;

    public RecentGameHolder(View itemView) {
        super(itemView);
        bind(itemView.getContext());
    }

    private void bind(Context context) {
        this.context = context;
        // other elements
        playButton = (PlayButton) itemView.findViewById(R.id.recentGameButton);
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        trafficLights = (ImageView) itemView.findViewById(R.id.trafficLightsimage);
        usernameText = (TextView) itemView.findViewById(R.id.username);
        usernameText.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
        statusText = (TextView) itemView.findViewById(R.id.status);
    }

    @Override
    public void bind(Game element) {
        User opponent = new User(element.opponent_id, element.opponent_username, element.opponent_name, element.opponent_surname, element.opponent_image);
        playButton.goToGame(element.id, opponent);
        if(element.ended) {
            setSummary();
        } else {
            if(!element.my_turn) {
                setDetails();
            } else {
                setPlayNow();
            }
        }
        if(element.opponent_name != null && element.opponent_surname != null) {
            setUsernameText(element.opponent_name + " " + element.opponent_surname);
        } else { setUsernameText(element.opponent_username); }
        /*TPUtils.picasso
                .load(TPUtils.getUserImageFromID(context, element.opponent_id))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into(profilePicture);*/
        TPUtils.injectUserImage(context, opponent, profilePicture);
        // set on click on profile picture
        /*profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlphaView.class);
                context.startActivity(intent);
            }
        });*/
    }

    private void setPlayNow() {
        playButton.setPlayNow();
        statusText.setText(context.getString(R.string.play_now_status));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_green));
    }

    private void setSummary() {
        playButton.setSummary();
        statusText.setText(context.getString(R.string.new_game_status));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_red));
    }

    private void setDetails() {
        playButton.setDetails();
        statusText.setText(context.getString(R.string.details_status));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_yellow));
    }

    private void setUsernameText(String text) {
        usernameText.setText(text);
    }
}

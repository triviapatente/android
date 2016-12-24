package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.PlayButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 12/11/16.
 */
public class RecentGameHolder extends TPHolder<Game> {
    // status strings
    private String playNowStatus, detailsStatus, summaryStatus, contactStatus;
    // status traffic lights
    private Drawable playNowTL, detailsTL, summaryTL, contactTL;
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
        // status
        playNowStatus = context.getResources().getString(R.string.play_now_status);
        detailsStatus = context.getResources().getString(R.string.details_status);
        summaryStatus = context.getResources().getString(R.string.new_game_status);
        contactStatus = context.getResources().getString(R.string.contact_status);
        // traffic lights
        playNowTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_green);
        detailsTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_yellow);
        summaryTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_red);
        contactTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_no_lights);
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
        playButton.goToGame(element.id, new User(element.opponent_id, element.opponent_username, element.opponent_name, element.opponent_surname, element.opponent_image));
        if(!element.started) {
            setContact();
        }
        else if(element.ended) {
            setSummary();
        } else {
            if(element.my_turn) {
                setPlayNow();
            } else {
                setDetails();
            }
        }
        if(element.opponent_name != null && element.opponent_surname != null) {
            setUsernameText(element.opponent_name + " " + element.opponent_surname);
        } else { setUsernameText(element.opponent_username); }
        if(element.opponent_image != null) {
            // TODO get image
        } else {
            setProfilePicture(ContextCompat.getDrawable(context, R.drawable.no_image));
        }
        // set on click on profile picture
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlphaView.class);
                context.startActivity(intent);
            }
        });
    }

    private void setPlayNow() {
        playButton.setPlayNow();
        statusText.setText(playNowStatus);
        trafficLights.setImageDrawable(playNowTL);
    }

    private void setSummary() {
        playButton.setSummary();
        statusText.setText(summaryStatus);
        trafficLights.setImageDrawable(summaryTL);
    }

    private void setDetails() {
        playButton.setDetails();
        statusText.setText(detailsStatus);
        trafficLights.setImageDrawable(detailsTL);
    }

    private void setContact() {
        playButton.setContact();
        statusText.setText(contactStatus);
        trafficLights.setImageDrawable(contactTL);
    }

    private void setUsernameText(String text) {
        usernameText.setText(text);
    }

    private void setProfilePicture(Drawable image) {
        profilePicture.setImageDrawable(image);
    }
}

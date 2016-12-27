package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.PlayButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 28/11/16.
 */
public class ProposedOpponentHolder extends TPHolder<User> {
    // player data
    RoundedImageView profilePicture;
    TextView usernameTextField, scoreTextField;
    // play button
    PlayButton playButton;
    // context
    Context context;

    ImageView imageView;

    public ProposedOpponentHolder(View itemView) {
        super(itemView);
        init(itemView.getContext());
    }

    private void init(final Context context) {
        this.context = context;
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        usernameTextField = (TextView) itemView.findViewById(R.id.username);
        scoreTextField = (TextView) itemView.findViewById(R.id.score);
        playButton = (PlayButton) itemView.findViewById(R.id.playButton);
        imageView = (ImageView) itemView.findViewById(R.id.blurredView);
    }

    @Override
    public void bind(User element) {
        // set image
        if(false) {
            // TODO get image
        } else {
            profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_image));
        }
        // set username or, if possible, name and surname
        if(element.name != null && element.surname != null) {
            usernameTextField.setText(element.name + " " + element.surname);
        } else { usernameTextField.setText(element.username); }
        // set score
        scoreTextField.setText(String.valueOf(element.score));
        // set on click on profile picture
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlphaView.class);
                context.startActivity(intent);
            }
        });
        // set appropriate button
        if(element.last_game_won != null && element.last_game_won) {
            playButton.setPlayNow();
        } else {
            playButton.setNewGame();
        }
        // send invite on click
        playButton.sendInvite(element);
    }
}

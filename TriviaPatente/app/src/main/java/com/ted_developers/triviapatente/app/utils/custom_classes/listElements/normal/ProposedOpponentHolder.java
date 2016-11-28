package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.PlayButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 28/11/16.
 */
public class ProposedOpponentHolder extends TPHolder<User> {
    // player data
    RoundedImageView profilePicture;
    TextView usernameTextField;
    // play button
    PlayButton playButton;
    // context
    Context context;

    public ProposedOpponentHolder(View itemView) {
        super(itemView);
    }

    public ProposedOpponentHolder(View itemView, Context context) {
        super(itemView);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        usernameTextField = (TextView) itemView.findViewById(R.id.username);
        playButton = (PlayButton) itemView.findViewById(R.id.playButton);
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
        // set appropriate button
        if(element.last_game_won) {
            playButton.setPlayNow();
        } else {
            playButton.setNewGame();
        }

    }
}

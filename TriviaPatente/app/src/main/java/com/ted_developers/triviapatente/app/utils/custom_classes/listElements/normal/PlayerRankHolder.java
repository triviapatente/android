package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.view.View;
import android.widget.TextView;

import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.models.auth.User;

/**
 * Created by Antonio on 28/11/16.
 */
public class PlayerRankHolder extends TPHolder<User> {
    // player data
    RoundedImageView profilePicture;
    TextView usernameTextField;
    // position field
    // todo finish implementing


    public PlayerRankHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(User element) {

    }
}

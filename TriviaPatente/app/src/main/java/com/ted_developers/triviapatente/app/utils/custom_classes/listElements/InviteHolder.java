package com.ted_developers.triviapatente.app.utils.custom_classes.listElements;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.models.game.Invite;

/**
 * Created by Antonio on 25/11/16.
 */
public class InviteHolder extends TPHolder<Invite> {
    // player name
    private TextView usernameText;
    // profile picture
    private RoundedImageView profilePicture;
    // context
    Context context;

    public InviteHolder(View itemView) {
        super(itemView);
    }

    public InviteHolder(View itemView, Context context) {
        super(itemView);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        usernameText = (TextView) itemView.findViewById(R.id.username);
        TextViewCompat.setTextAppearance(usernameText, R.style.TPTextStyleMedium);
    }

    @Override
    public void bind(Invite element) {
        // TODO get username
        usernameText.setText(String.valueOf(element.sender_id));
        if(false) {
            // TODO get image
        } else {
            setProfilePicture(ContextCompat.getDrawable(context, R.drawable.no_image));
        }
    }

    private void setProfilePicture(Drawable image) {
        profilePicture.setImageDrawable(image);
    }
}

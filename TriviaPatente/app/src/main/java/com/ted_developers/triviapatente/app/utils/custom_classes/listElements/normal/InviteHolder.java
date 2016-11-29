package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Invite;
import com.ted_developers.triviapatente.models.responses.SuccessInvite;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Antonio on 25/11/16.
 */
public class InviteHolder extends TPHolder<Invite> {
    // player name
    private TextView usernameText;
    // profile picture
    private RoundedImageView profilePicture;
    // accept and decline button
    private ImageButton acceptButton, declineButton;
    // adapter
    private TPExpandableList<Invite> expandableList;
    // context
    Context context;

    public InviteHolder(View itemView) {
        super(itemView);
    }

    public InviteHolder(View itemView, Context context, TPExpandableList<Invite> expandableList) {
        super(itemView);
        init(context);
        this.expandableList = expandableList;
    }

    private void init(Context context) {
        this.context = context;
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        usernameText = (TextView) itemView.findViewById(R.id.username);
        usernameText.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
        acceptButton = (ImageButton) itemView.findViewById(R.id.acceptButton);
        declineButton = (ImageButton) itemView.findViewById(R.id.declineButton);
    }

    @Override
    public void bind(final Invite element) {
        if(element.sender_name != null && element.sender_surname != null) {
            usernameText.setText(element.sender_name + " " + element.sender_surname);
        } else { usernameText.setText(element.sender_username); }
        if(false) {
            // TODO get image
        } else {
            setProfilePicture(ContextCompat.getDrawable(context, R.drawable.no_image));
        }
        // add on click event
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InviteHolder.this.processInvite(element, true);
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InviteHolder.this.processInvite(element, false);
            }
        });
    }

    private void setProfilePicture(Drawable image) {
        profilePicture.setImageDrawable(image);
    }

    private void processInvite(final Invite element, boolean accepted) {
        Call<SuccessInvite> call = RetrofitManager.getHTTPGameEndpoint().processInvite(element.game_id, accepted);
        call.enqueue(new TPCallback<SuccessInvite>() {
            @Override
            public void mOnResponse(Call<SuccessInvite> call, Response<SuccessInvite> response) {
                if(response.code() == 200 && response.body().success) {
                    // remove element
                    expandableList.adapter.removeItem(element);
                    // update counter
                    expandableList.setListCounter(expandableList.adapter.getItemCount() - 1, false);
                    // eventually change height
                    expandableList.updateMinimized();
                }
            }

            @Override
            public void mOnFailure(Call<SuccessInvite> call, Throwable t) {}

            @Override
            public void then() {}
        });
    }
}

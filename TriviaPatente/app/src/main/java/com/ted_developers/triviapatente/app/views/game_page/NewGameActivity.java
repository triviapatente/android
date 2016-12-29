package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.ReceivedData;
import com.ted_developers.triviapatente.app.utils.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.InviteHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.BackPictureTPActionBar;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.find_opponent.FindOpponentActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Invite;
import com.ted_developers.triviapatente.models.responses.InviteUser;
import com.ted_developers.triviapatente.models.responses.SuccessInvites;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class NewGameActivity extends TPActivity {
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // action_bar
    @BindView(R.id.toolbar)
    BackPictureTPActionBar toolbar;
    @BindString(R.string.new_game_title) String toolbarTitle;
    @BindString(R.string.main_page_title) String backTitle;
    // options
    @BindView(R.id.option_panel) LinearLayout optionPanel;
    @BindView(R.id.findOpponent) Button findOpponent;
    @BindView(R.id.findRandomOpponent) Button findRandomOpponent;
    @BindView(R.id.option_panel_title) TextView optionPanelTitle;
    // invites
    TPExpandableList<Invite> invites;
    @BindString(R.string.invites_title) String invitesTitle;
    @BindString(R.string.no_invites) String invitesAlternativeTitle;
    @BindDimen(R.dimen.invite_height) int inviteHeight;
    // socket
    @BindString(R.string.socket_event_invite_created) String eventInviteCreated;
    SocketCallback<InviteUser> inviteCreatedCallback = new SocketCallback<InviteUser>() {
        @Override
        public void response(InviteUser response) {
            if(visible) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invites.adapter.addItem(0);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        invites = (TPExpandableList<Invite>) getSupportFragmentManager().findFragmentById(R.id.invites);
        // init
        init();
        // listening to events
        listen(eventInviteCreated, InviteUser.class, inviteCreatedCallback);
    }

    private void init() {
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        // hide other elements
        bulkVisibilitySetting(View.GONE);
        initToolbar();
        //loadInvites();
        // show other elements
        bulkVisibilitySetting(View.VISIBLE);
        // stop loading
        loadingView.setVisibility(View.GONE);
    }

    private void initToolbar() {
        // set profile picture
        // TODO get dinamically
        toolbar.setProfilePicture(getResources().getDrawable(R.drawable.image_no_profile_picture));
    }

    private void loadInvites() {
        invites.setTitles(invitesTitle, invitesAlternativeTitle);
        // request invites
        if(ReceivedData.pendingInvites == null) {
            Call<SuccessInvites> call = RetrofitManager.getHTTPGameEndpoint().getPendingInvites();
            call.enqueue(new TPCallback<SuccessInvites>() {
                @Override
                public void mOnResponse(Call<SuccessInvites> call, Response<SuccessInvites> response) {
                    if(response.code() == 200 && response.body().success) {
                        if(response.body().invites != null) {
                            ReceivedData.pendingInvites = response.body().invites;
                            invites.setItems(ReceivedData.pendingInvites,
                                    R.layout.list_element_invite_holder, InviteHolder.class,
                                    R.layout.list_element_invite_footer, TPFooter.class,
                                    inviteHeight);
                            ReceivedData.numberOfInvites = ReceivedData.pendingInvites.size();
                        }
                        invites.setListCounter(ReceivedData.numberOfInvites);
                    }
                }

                @Override
                public void mOnFailure(Call<SuccessInvites> call, Throwable t) {}

                @Override
                public void then() {
                }
            });
        } else {
            invites.setItems(ReceivedData.pendingInvites,
                    R.layout.list_element_invite_holder, InviteHolder.class,
                    R.layout.list_element_invite_footer, TPFooter.class,
                    inviteHeight);
            invites.setListCounter(ReceivedData.numberOfInvites);
        }
    }

    @OnClick(R.id.findOpponent)
    public void findOpponent() {
        Intent intent = new Intent(this, FindOpponentActivity.class);
        intent.putExtra("random", false);
        this.startActivity(intent);
    }

    @OnClick(R.id.findRandomOpponent)
    public void findRandomOpponent() {
        Intent intent = new Intent(this, FindOpponentActivity.class);
        intent.putExtra("random", true);
        this.startActivity(intent);
    }

    private void bulkVisibilitySetting(int visibility) {
        toolbar.setVisibility(visibility);
        invites.getView().setVisibility(visibility);
        optionPanel.setVisibility(visibility);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadingView.setVisibility(View.VISIBLE);
        this.invites.getView().post(new Runnable() {
            @Override
            public void run() {
                loadInvites();
            }
        });
        loadingView.setVisibility(View.GONE);
    }
}

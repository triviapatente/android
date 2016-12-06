package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal.InviteHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.top_bar.BackPictureTPToolbar;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.app.views.find_opponent.FindOpponentActivity;
import com.ted_developers.triviapatente.app.views.main_page.MainPageActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Invite;
import com.ted_developers.triviapatente.models.responses.SuccessInvites;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class NewGameActivity extends AppCompatActivity {
    // loading
    @BindView(R.id.loadingView) RelativeLayout loadingView;
    // toolbar
    @BindView(R.id.toolbar) BackPictureTPToolbar toolbar;
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
    @BindDimen(R.dimen.invite_height) int inviteHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        ButterKnife.bind(this);
        invites = (TPExpandableList<Invite>) getSupportFragmentManager().findFragmentById(R.id.invites);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // init
        init();
    }

    private void init() {
        // start loading
        loadingView.setVisibility(View.VISIBLE);
        // hide other elements
        bulkVisibilitySetting(View.GONE);
        initToolbar();
        loadInvites();
        // show other elements
        bulkVisibilitySetting(View.VISIBLE);
        // stop loading
        loadingView.setVisibility(View.GONE);
    }

    private void initToolbar() {
        // set title
        toolbar.setTitle(toolbarTitle);
        // set profile picture
        // TODO get dinamically
        toolbar.setProfilePicture(getResources().getDrawable(R.drawable.no_image));
        // set back button
        toolbar.setBackButtonText(backTitle);
        toolbar.setBackButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewGameActivity.this, MainPageActivity.class);
                NewGameActivity.this.startActivity(myIntent);
            }
        });
    }

    private void loadInvites() {
        invites.setListTitle(invitesTitle);
        // request recent games
        Call<SuccessInvites> call = RetrofitManager.getHTTPGameEndpoint().getPendingInvites();
        call.enqueue(new TPCallback<SuccessInvites>() {
            @Override
            public void mOnResponse(Call<SuccessInvites> call, Response<SuccessInvites> response) {
                if(response.code() == 200 && response.body().success) {
                    int counter = 0;
                    if(response.body().invites != null) {
                        invites.setItems(response.body().invites,
                                R.layout.invite, InviteHolder.class,
                                R.layout.invites_footer, TPFooter.class,
                                inviteHeight);
                        counter = response.body().invites.size();
                    }
                    invites.setListCounter(counter);
                }
            }

            @Override
            public void mOnFailure(Call<SuccessInvites> call, Throwable t) {}

            @Override
            public void then() {
            }
        });
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
}

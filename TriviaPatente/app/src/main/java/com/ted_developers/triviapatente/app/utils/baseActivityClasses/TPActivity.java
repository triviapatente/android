package com.ted_developers.triviapatente.app.utils.baseActivityClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.ReceivedData;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPDialog;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPHeartDialog;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPRateDialog;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.drawer.DrawerOption;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.drawer.TPDrawerAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.adapters.drawer.drawer_options_type;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;
import com.ted_developers.triviapatente.app.views.find_opponent.FindOpponentActivity;
import com.ted_developers.triviapatente.app.views.menu_activities.ChangePasswordActivity;
import com.ted_developers.triviapatente.app.views.menu_activities.ChangeUserDetailsActivity;
import com.ted_developers.triviapatente.app.views.menu_activities.ContactsActivity;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Antonio on 08/12/16.
 */
public class TPActivity extends AppCompatActivity {
    // http connection error
    public @BindString(R.string.httpConnectionError) String httpConnectionError;
    public @BindString(R.string.httpConnectionErrorRetryButton) String httpConnectionErrorRetryButton;
    // hide keyboard utils
    protected @Nullable @BindView(R.id.dummy_layout) LinearLayout dummyLayout;
    public User currentUser;
    // used for toolbar configuration utils
    protected @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    protected @Nullable @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    protected @Nullable @BindView(R.id.heartImageButton) ImageButton heartCounter;
    protected @Nullable @BindView(R.id.settingsButton) ImageButton settingsButton;
    @BindString(R.string.drawerProfileOption) String profileOptionString;
    @BindString(R.string.drawerContactsOption) String contactsOptionString;
    @BindString(R.string.drawerNewGameOption) String drawerNewGameOptionString;
    @BindString(R.string.drawerLogoutOption) String logoutOptionString;
    // side drawer
    protected  @Nullable @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    protected  @Nullable @BindView(R.id.left_drawer) ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected TPDrawerAdapter drawerAdapter;
    // menu options
    public @Nullable @BindView(R.id.activityContainer) RelativeLayout activityContainer;
    TPDialog logoutDialog;
    public @Nullable @BindView(R.id.fullBlurredView) ImageView blurredBackgroundView;
    public @Nullable @BindView(R.id.fullBlurredContainer) RelativeLayout blurredBackgroundContainer;
    // sockets
    protected AuthSocketManager authSocketManager = new AuthSocketManager();
    protected BaseSocketManager baseSocketManager = new BaseSocketManager();
    //public List<String> pathListened = new ArrayList<>();
    protected boolean visible;
    public GameSocketManager gameSocketManager = new GameSocketManager();
    // popovers
    @BindInt(R.integer.days_delay_life_popover) int lifePopoverDelay;
    @BindInt(R.integer.days_delay_rate_popover) int ratePopoverDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        currentUser = SharedTPPreferences.currentUser();
    }

    // touch handler
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        TPUtils.hideKeyboard(this, dummyLayout);
        return super.dispatchTouchEvent(ev);
    }


    @SuppressWarnings("ResourceType")
    private void initUI() {
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle(null);
                getSupportActionBar().setElevation(0);
            }
            if(mDrawerLayout != null) initDrawer();
            if(getBackButtonVisibility() == View.VISIBLE) {
                toolbar.setNavigationIcon(R.drawable.image_back_chevron);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {onBackPressed();
                    }
                });
            }
            if(heartCounter != null) {
                heartCounter.setVisibility(getHeartCounterVisibility());
            }
            if(settingsButton != null) {
                settingsButton.setVisibility(getSettingsVisibility());
            }
            initLogoutDialog();
        }
        if(activityContainer != null) activityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TPUtils.hideKeyboard(TPActivity.this);
            }
        });

        setToolbarTitle(getToolbarTitle());

    }

    protected void setToolbarTitle(String title) {
        if(toolbarTitle != null) toolbarTitle.setText(title);
    }

    private void initDrawer() {
        List<DrawerOption> options = new ArrayList<DrawerOption>();
        options.add(new DrawerOption(R.layout.drawer_list_item, drawer_options_type.image));
        options.add(new DrawerOption(R.layout.drawer_list_item, R.drawable.image_profile, drawer_options_type.profile, profileOptionString));
        options.add(new DrawerOption(R.layout.drawer_list_item, R.drawable.image_car, drawer_options_type.new_game, drawerNewGameOptionString));
        options.add(new DrawerOption(R.layout.drawer_list_item, R.drawable.image_contacts, drawer_options_type.contacts, contactsOptionString));
        options.add(new DrawerOption(R.layout.drawer_list_item, R.drawable.image_logout, drawer_options_type.logout, logoutOptionString));
        // Set the adapter for the list view
        drawerAdapter = new TPDrawerAdapter(this, R.layout.drawer_list_item, options);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new TPDrawerItemClickListener(drawerAdapter));
        // Set the list's click listener
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_opened, R.string.drawer_closed) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.button_shop, R.string.button_stats
        );
        mDrawerToggle.syncState();
    }

    private class TPDrawerItemClickListener implements ListView.OnItemClickListener {
        TPDrawerAdapter adapter;
        public TPDrawerItemClickListener(TPDrawerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerLayout.closeDrawers();
            DrawerOption option = adapter.getItem(position);
            switch (option.optionType) {
                case image:
                case profile: settings(); break;
                case contacts: contacts(); break;
                case new_game: randomGame(); break;
                case logout: logout(); break;
            }
        }
    }

    protected String getToolbarTitle(){
        return "";
    }

    protected int getSettingsVisibility(){
        return View.GONE;
    }
    protected int getBackButtonVisibility(){
        return View.GONE;
    }
    protected int getHeartCounterVisibility() { return View.VISIBLE; }

    @Optional
    @OnClick(R.id.settingsButton)
    public void settingsButtonClick() {
        Intent i = new Intent(this, ChangePasswordActivity.class);
        startActivity(i);
    }

    @Optional
    @OnClick(R.id.heartImageButton)
    public void heartImageButtonClick() {
        showHeartPopup(false);
    }

    protected void showHeartPopup(boolean automatic) {
        new TPHeartDialog(this, automatic).show();
    }

    protected void showRatePopup(boolean automatic) {
        new TPRateDialog(this, automatic).show();
    }

    protected void showAutomaticPopup() {
        long todayMillis = System.currentTimeMillis(), days = 24 * 60 * 60 * 1000;
        boolean hasBeenUpdated = false;
        int currentVersion;
        try {
            currentVersion = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            if(currentUser.lastAppVersionCode == null) {
                hasBeenUpdated = true;
                currentUser.lastAppVersionCode = currentVersion;
            } else {
                hasBeenUpdated = currentVersion != currentUser.lastAppVersionCode;
            }
        }
        catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        // First of all, check rate popup
        if((currentUser.showRatePopup || hasBeenUpdated) && currentUser.lastRatePopupShowedDateMillis != null && todayMillis - currentUser.lastRatePopupShowedDateMillis > ratePopoverDelay * days) {
            // show rate popup
            showRatePopup(true);
            currentUser.lastRatePopupShowedDateMillis = todayMillis;
        } else if(currentUser.showLifePopup && currentUser.lastLifePopupShowedDateMillis != null && todayMillis - currentUser.lastLifePopupShowedDateMillis > lifePopoverDelay * days) { // try show life popup
            // show life popup
            showHeartPopup(true);
            currentUser.lastLifePopupShowedDateMillis = todayMillis;
        }
        else {
            // Check if popups have never been shown
            if(currentUser.lastLifePopupShowedDateMillis == null) currentUser.lastLifePopupShowedDateMillis = todayMillis;
            if(currentUser.lastRatePopupShowedDateMillis == null) currentUser.lastRatePopupShowedDateMillis = todayMillis;
        }
        // Save new data
        SharedTPPreferences.saveUser(currentUser);
    }

    protected void listen(String path, final Class outputClass, final SocketCallback cb) {
        //pathListened.add(path);
        baseSocketManager.listen(path, outputClass, cb);
    }

    @Override
    public void onPause() {
        super.onPause();

        /*for (String path : pathListened) {
            baseSocketManager.stopListen(path);
        }*/

        visible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(needsLeaveRoom()) {
            gameSocketManager.leave(new SocketCallback<Success>() {
                @Override
                public void response(Success response) {}
            });
        }
        visible = true;
        if(drawerAdapter != null) drawerAdapter.notifyDataSetChanged(); // user data can be modified, take it again from shared preferences
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        if(needsFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initUI();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
    }

    protected boolean needsLeaveRoom() {
        return true;
    }

    protected boolean needsFullScreen() {
        return true;
    }

    protected void backToFirstAccess() {
        // open first access page
        Intent myIntent = new Intent(this, FirstAccessActivity.class);
        startActivity(myIntent);
        finish();
    }

    // contacts
    public void contacts() {
        Intent intent = new Intent(this, ContactsActivity.class);
        TPActivity.this.startActivity(intent);
    }

    // new random game
    public void randomGame() {
        Intent intent = new Intent(this, FindOpponentActivity.class);
        intent.putExtra("random", true);
        TPActivity.this.startActivity(intent);
    }

    // settings
    public void settings() {
        Intent intent = new Intent(this, ChangeUserDetailsActivity.class);
        startActivity(intent);
    }

    // logout
    public void logout() {
        TPUtils.blurContainerIntoImageView(TPActivity.this, activityContainer, blurredBackgroundView);
        blurredBackgroundContainer.setVisibility(View.VISIBLE);
        // showing modal
        logoutDialog.show();
    }
    private void initLogoutDialog() {
        logoutDialog = new TPDialog(this, R.layout.modal_view_logout, 0, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                blurredBackgroundContainer.setVisibility(View.GONE);
            }
        }) {
            @Override
            public void onNegativeButtonClick() {
                SharedTPPreferences.deleteAll();
                BaseSocketManager.disconnect();
                backToFirstAccess();
            }

            @Override
            public void onPositiveButtonClick() {
                cancel();
            }
        };
    }
}

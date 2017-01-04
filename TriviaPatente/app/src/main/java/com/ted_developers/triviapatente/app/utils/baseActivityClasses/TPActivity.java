package com.ted_developers.triviapatente.app.utils.baseActivityClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.TPActionBar;
import com.ted_developers.triviapatente.app.utils.custom_classes.actionBar.TPActionBarType;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPDialog;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Antonio on 08/12/16.
 */
public class TPActivity extends AppCompatActivity implements Button.OnClickListener {
    // used for toolbar configuration utils
    protected @Nullable @BindView(R.id.action_bar) TPActionBar actionBar;
    // menu options
    protected @Nullable @BindView(R.id.activityContainer) RelativeLayout activityContainer;
    TPDialog logoutDialog;
    @Nullable @BindView(R.id.fullBlurredView) ImageView blurredBackgroundView;
    @Nullable @BindView(R.id.fullBlurredContainer) RelativeLayout blurredBackgroundContainer;
    // sockets
    protected AuthSocketManager authSocketManager = new AuthSocketManager();
    protected BaseSocketManager baseSocketManager = new BaseSocketManager();
    //public List<String> pathListened = new ArrayList<>();
    protected boolean visible;
    protected GameSocketManager gameSocketManager = new GameSocketManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    private void initUI() {
        if(actionBar != null) {
            if(actionBar.id != TPActionBarType.backPicture.id) {
                setMenu();
                initLogoutDialog();
            }
            initActionBar();
        }
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
            gameSocketManager.leave_room(new SocketCallback<Success>() {
                @Override
                public void response(Success response) {
                    if(!response.success) {
                        // todo vedere come avvisare
                        Log.i("TEST", "ERRORE NEL LEAVE ROOM!!");
                    }
                }
            });
        }
        visible = true;
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

    // action bar management
    protected void initActionBar() {
        actionBar.setProfilePicture(getActionBarProfilePicture());
    }

    // override this to take a particular profile picture
    protected Drawable getActionBarProfilePicture() {
        return ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture);
    }

    // to automatically hide menu
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(MotionEvent.ACTION_UP == ev.getAction()
                && actionBar != null && actionBar.getMenuVisibility() == View.VISIBLE
                && !TPUtils.isPointInsideView((int) ev.getX(), (int) ev.getY(), actionBar.menu)) {
            actionBar.hideMenu();
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    // menu options management
    private void setMenu() {
        // set menu
        actionBar.setMenu();
        // set menu on click listener
        actionBar.menuLogoutOptionButton.setOnClickListener(this);
        actionBar.menuAboutOptionButton.setOnClickListener(this);
        actionBar.menuSettingsOptionButton.setOnClickListener(this);
        actionBar.menuProfileOptionButton.setOnClickListener(this);
    }
    // on click listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuLogoutOption: logout(); break;
            default: Intent intent = new Intent(this, AlphaView.class); startActivity(intent);
        }
    }
    // logout
    public void logout() {
        // setting blurry background
        actionBar.hideMenu(new SimpleCallback() {
            @Override
            public void execute() {
                TPUtils.blurContainerIntoImageView(TPActivity.this, activityContainer, blurredBackgroundView);
                blurredBackgroundContainer.setVisibility(View.VISIBLE);
                // showing modal
                logoutDialog.show();
            }
        });
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
                authSocketManager.logout(new SocketCallback<Success>() {
                    @Override
                    public void response(Success response) {
                        if(response.success) {
                            SharedTPPreferences.deleteAll();
                            backToFirstAccess();
                        } else {
                            Toast.makeText(TPActivity.this, getResources().getString(R.string.menu_logot_unable_to_logout),
                                    Toast.LENGTH_LONG).show();
                            onPositiveButtonClick();
                        }
                    }
                });
            }

            @Override
            public void onPositiveButtonClick() {
                cancel();
            }
        };
    }
}

package it.triviapatente.android.app.views.access;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.views.main_page.MainPageActivity;
import it.triviapatente.android.http.modules.base.HTTPBaseEndpoint;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.models.responses.SuccessUserToken;
import it.triviapatente.android.socket.modules.auth.AuthSocketManager;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstAccessActivity extends TPActivity {
    @BindView(R.id.pager) public ViewPager mViewPager;
    @BindView(R.id.pagerIndicator) CirclePageIndicator mIndicator;
    // error messages
    @BindString(R.string.field_required) public String field_required_error;
    @BindString(R.string.blank_not_allowed) public String blank_not_allowed_error;
    @BindString(R.string.not_matching_pwd) public String not_matching_pwd_error;
    @BindString(R.string.not_valid_email) public String not_valid_email_error;
    @BindString(R.string.not_valid_username) public String not_valid_username_error;
    @BindString(R.string.not_valid_password) public String not_valid_password_error;
    // intent
    @BindString(R.string.is_old_session) String isOldSession;

    @BindInt(R.integer.min_username_length) public int minUsernameLength;
    @BindInt(R.integer.min_password_length) public int minPasswordLength;

    private FirstAccessAdapter mAdapter;

    private void unsubscribeToPush() {
        HTTPBaseEndpoint endpoint = RetrofitManager.getHTTPBaseEndpoint();
        Call<Success> call = endpoint.unregisterToPush("Android", SharedTPPreferences.deviceId());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                Log.i("Push", "User unsubscribed to push, success? " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                if(t != null) Log.i("Push", "User unsubscribed to push, failure " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        // if loaded due to old session, show toast
        if(getIntent().getBooleanExtra(isOldSession, false)) {
            Toast.makeText(this, getResources().getString(R.string.old_session),
                    Toast.LENGTH_LONG).show();
        }
        // View pager
        mAdapter = new FirstAccessAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // ViewPager Indicator
        mIndicator.setViewPager(mViewPager);
        // Start from register page
        mViewPager.setCurrentItem(0);
    }

    // go to main page and save data
    public static void openMainPage(FirstAccessActivity a, SuccessUserToken data) {
        // save data
        SharedTPPreferences.saveToken(data.token);
        SharedTPPreferences.saveUser(data.user);
        // open main page
        Intent myIntent = new Intent(a, MainPageActivity.class);
        a.startActivity(myIntent);
        a.finish();
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        AuthSocketManager.disconnect();
        unsubscribeToPush();
    }

    // touch handler
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        TPUtils.hideKeyboard(this, dummyLayout);
        return super.dispatchTouchEvent(ev);
    }
}

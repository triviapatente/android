package com.ted_developers.triviapatente.app.views.first_access.Register;

import android.util.Log;

import com.ted_developers.triviapatente.app.utils.mViews.LoadingButton.ManageLoading;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.UserToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Antonio on 23/10/16.
 */
public class RegisterPresenter {
    // get from parameters to provide scalability
    public static void register(String username, String email,
                                String password, String repeatPassword,
                                final ManageLoading loadingManager) {
        // TODO manage problems
        loadingManager.startLoading();
        Call<UserToken> call = RetrofitManager.getHTTPAuthEndpoint().register(username, email, password);
        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                Log.i("TEST", response.body().user.username);
                Log.i("TEST", response.body().token);
                loadingManager.stopLoading();
                // TODO manage errors
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.i("TEST", "FAILURE");
                loadingManager.stopLoading();
            }
        });
    }
}

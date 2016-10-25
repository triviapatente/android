package com.ted_developers.triviapatente.app.views.first_access.Register;

import android.util.Log;

import com.ted_developers.triviapatente.app.utils.mViews.LoadingButton.ManageLoading;
import com.ted_developers.triviapatente.app.utils.mViews.TPCallback.TPCallback;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;

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
        Call<SuccessUserToken> call = RetrofitManager.getHTTPAuthEndpoint().register(username, email, password);
        call.enqueue(new TPCallback<SuccessUserToken>() {
            @Override
            public void mOnResponse(Call<SuccessUserToken> call, Response<SuccessUserToken> response) {
                Log.i("TEST", response.body().user.username);
                Log.i("TEST", response.body().token);
            }

            @Override
            public void mOnFailure(Call<SuccessUserToken> call, Throwable t) {
                Log.i("TEST", "FAILED");
            }

            @Override
            public void then() {
                loadingManager.stopLoading();
            }
        });
    }
}

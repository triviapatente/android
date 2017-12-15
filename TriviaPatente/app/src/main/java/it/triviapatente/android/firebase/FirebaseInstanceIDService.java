package it.triviapatente.android.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.http.modules.base.HTTPBaseEndpoint;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.models.responses.SuccessUserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by donadev on 15/12/17.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        if(SharedTPPreferences.currentUser() != null)
            sendRegistration();

    }
    public static void sendRegistration() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(refreshedToken != null) {
            sendRegistration(refreshedToken);
        }
    }
    private static void sendRegistration(String token) {
        String deviceId = SharedTPPreferences.deviceId();
        Call<Success> call = RetrofitManager.getHTTPBaseEndpoint().registerToPush("Android", token, deviceId);
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                Log.i("Registration token", "Success? " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                Log.i("Registration token", "Failure");
            }
        });
    }
}

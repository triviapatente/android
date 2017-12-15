package it.triviapatente.android.http.modules.preferences;

import it.triviapatente.android.models.preferences.Preferences;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPPreferencesEndpoint {
    @FormUrlEncoded
    @POST("preferences/notification/{notification_type}/edit")
    Call<Preferences> changeNotificationPreferences(@Path("notification_type") String notification_type, @Field("new_value") boolean new_value);

    @FormUrlEncoded
    @POST("preferences/stats/edit")
    Call<Preferences> changeStatsPreferences(@Field("new_value") String new_value);

    @FormUrlEncoded
    @POST("preferences/chat/edit")
    Call<Preferences> changeChatPreferences(@Field("new_value") String new_value);
}

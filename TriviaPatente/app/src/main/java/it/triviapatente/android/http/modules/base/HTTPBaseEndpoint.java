package it.triviapatente.android.http.modules.base;

import it.triviapatente.android.models.responses.InstagramImages;
import it.triviapatente.android.models.responses.Success;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPBaseEndpoint {
    @FormUrlEncoded
    @POST("ws/contact")
    Call<Success> contact(@Field("message") String message, @Field("scope") String scope);
    @FormUrlEncoded
    @POST("ws/registerForPush")
    Call<Success> registerToPush(@Field("os") String os, @Field("token") String token, @Field("deviceId") String deviceId);
    @FormUrlEncoded
    @POST("ws/unregisterForPush")
    Call<Success> unregisterToPush(@Field("os") String os, @Field("deviceId") String deviceId);

    @GET("ws/instagram")
    Call<InstagramImages> getInstagramPhotos();
}

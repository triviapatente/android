package it.triviapatente.android.http.modules.base;

import it.triviapatente.android.models.responses.Success;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPBaseEndpoint {
    @FormUrlEncoded
    @POST("ws/contact")
    Call<Success> contact(@Field("message") String message, @Field("scope") String scope);
}

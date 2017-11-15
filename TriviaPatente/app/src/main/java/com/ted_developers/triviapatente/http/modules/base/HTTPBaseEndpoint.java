package com.ted_developers.triviapatente.http.modules.base;

import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessUserToken;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPBaseEndpoint {
    @FormUrlEncoded
    @POST("ws/contact")
    Call<Success> contact(@Field("message") String message, @Field("scope") String scope);
}

package com.ted_developers.triviapatente.http.modules.auth;

import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.UserToken;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPAuthEndpoint {
    @FormUrlEncoded
    @POST("auth/login")
    Call<UserToken> login(@Field("user") String user, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<UserToken> register(@Field("username") String username, @Field("email") String email, @Field("password") String password);

    @POST("auth/logout")
    Call<User> logout();

    @FormUrlEncoded
    @POST("account/name/edit")
    Call<User> changeName(@Field("name") String name);

    @FormUrlEncoded
    @POST("account/surname/edit")
    Call<User> changeSurname(@Field("surname") String surname);

    @Multipart
    @POST("account/image/edit")
    Call<User> changeImage(@Part("image") RequestBody image);

    @GET("account/user")
    Call<User> getCurrentuser();

    @GET("info/rank/italy")
    Call<List<User>> getItalianRank();
}

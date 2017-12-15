package it.triviapatente.android.http.modules.auth;

import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.models.responses.SuccessUserToken;

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
public interface HTTPAuthEndpoint {
    @FormUrlEncoded
    @POST("auth/login")
    Call<SuccessUserToken> login(@Field("user") String user, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<SuccessUserToken> register(@Field("username") String username, @Field("email") String email, @Field("password") String password);

    @POST("auth/logout")
    Call<User> logout();

    @FormUrlEncoded
    @POST("account/name/edit")
    Call<User> changeName(@Field("name") String name);

    @FormUrlEncoded
    @POST("account/surname/edit")
    Call<User> changeSurname(@Field("surname") String surname);

    @FormUrlEncoded
    @POST("auth/password/edit")
    Call<SuccessUserToken> changePassword(@Field("old_value") String old_password, @Field("new_value") String new_password);

    @FormUrlEncoded
    @POST("auth/password/request")
    Call<Success> requestNewPassword(@Field("usernameOrEmail") String usernameOrEmail);

    @Multipart
    @POST("account/image/edit")
    Call<User> changeImage(@Part MultipartBody.Part image);

    @GET("account/user")
    Call<User> getCurrentuser();

    @GET("info/rank/italy")
    Call<List<User>> getItalianRank();
}

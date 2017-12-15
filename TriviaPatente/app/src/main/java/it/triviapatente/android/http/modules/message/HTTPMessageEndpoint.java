package it.triviapatente.android.http.modules.message;

import it.triviapatente.android.models.message.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPMessageEndpoint {
    @FormUrlEncoded
    @GET("message/list/{game_id}")
    Call<List<Message>> list(@Path("game_id") long game_id, @Field("datetime") String datetime);
}

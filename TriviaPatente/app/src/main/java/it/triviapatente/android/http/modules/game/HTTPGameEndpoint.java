package it.triviapatente.android.http.modules.game;

import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.models.responses.SuccessDecrement;
import it.triviapatente.android.models.responses.SuccessGameUser;
import it.triviapatente.android.models.responses.SuccessGames;
import it.triviapatente.android.models.responses.SuccessUsers;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPGameEndpoint {
    @FormUrlEncoded
    @POST("game/new")
    Call<SuccessGameUser> newGame(@Field("opponent") long opponent_id);

    @POST("game/new/random")
    Call<SuccessGameUser> newRandomGame();

    @GET("game/recents")
    Call<SuccessGames> getRecentsGames();

    @GET("game/users/suggested")
    Call<SuccessUsers> getSuggestedUsers();

    @GET("game/users/search")
    Call<SuccessUsers> getSearchResult(@Query("query") String query);

    @GET("game/leave/decrement")
    Call<SuccessDecrement> getLeaveDecrement(@Query("game_id") Long game_id);

    @FormUrlEncoded
    @POST("game/leave")
    Call<Success> leaveGame(@Field("game_id") Long game_id);
}

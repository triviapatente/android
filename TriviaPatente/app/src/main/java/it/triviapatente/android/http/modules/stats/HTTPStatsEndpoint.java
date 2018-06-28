package it.triviapatente.android.http.modules.stats;

import it.triviapatente.android.models.responses.CategoryDetail;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luigi on 22/6/18.
 */
public interface HTTPStatsEndpoint {
    @GET("stats/detail/{id}")
    Call<CategoryDetail> getCategory(@Path("id") long category_id);
    @GET("stats/detail")
    Call<CategoryDetail> getGlobalCategory();
}

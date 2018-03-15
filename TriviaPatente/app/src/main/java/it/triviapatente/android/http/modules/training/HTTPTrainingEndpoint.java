package it.triviapatente.android.http.modules.training;

import java.util.Map;

import it.triviapatente.android.models.responses.RankPosition;
import it.triviapatente.android.models.responses.SuccessQuizzes;
import it.triviapatente.android.models.responses.SuccessTraining;
import it.triviapatente.android.models.responses.SuccessTrainings;
import it.triviapatente.android.models.responses.SuccessUsers;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by donadev on 15/03/18.
 */

public interface HTTPTrainingEndpoint {


    @GET("training/all")
    Call<SuccessTrainings> getTrainings();

    @GET("training/{id}")
    Call<SuccessQuizzes> getTraining(@Path("id") long training_id);

    @GET("training/new")
    Call<SuccessQuizzes> getQuestions(@Query("random") Boolean random);

    @POST("training/new")
    Call<SuccessTraining> answer(@Body Map<Long, Boolean> answers);
}

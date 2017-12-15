package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.game.Quiz;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 24/12/16.
 */
@Parcel
public class SuccessQuizzes extends Success {
    @SerializedName("questions") public List<Quiz> quizzes;
    public SuccessQuizzes() {}
}

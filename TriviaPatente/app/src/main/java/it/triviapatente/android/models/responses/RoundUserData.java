package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Question;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.game.Round;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class RoundUserData extends Success {
    @SerializedName("round") public Round round;
    @SerializedName("user") public User user;
    @SerializedName("globally") public Boolean globally;
    @SerializedName("quizzes") public List<Quiz> quizzes;
    @SerializedName("answers") public List<Question> answers;
    @SerializedName("category") public Category category;


    public RoundUserData() {}
}

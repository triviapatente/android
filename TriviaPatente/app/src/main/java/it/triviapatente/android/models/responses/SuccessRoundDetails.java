package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Game;
import it.triviapatente.android.models.game.Partecipation;
import it.triviapatente.android.models.game.Question;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.game.Round;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 23/01/17.
 */
@Parcel
public class SuccessRoundDetails extends Success {
    @SerializedName("answers") public List<Question> answers;
    @SerializedName("categories") public List<Category> categories;
    @SerializedName("game") public Game game;
    @SerializedName("quizzes") public List<Quiz> quizzes;
    @SerializedName("rounds") public List<Round> rounds;
    @SerializedName("users") public List<User> users;
    @SerializedName("partecipations") public List<Partecipation> partecipations;
    @SerializedName("score_increment") public Integer scoreIncrement;

    public SuccessRoundDetails() {}
}

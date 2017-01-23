package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.game.Partecipation;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.game.Quiz;

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
    @SerializedName("users") public List<User> users;
    @SerializedName("partecipations") public List<Partecipation> partecipations;
    @SerializedName("score_increment") public Integer scoreIncrement;

    public SuccessRoundDetails() {}
}

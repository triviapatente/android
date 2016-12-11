package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.game.Round;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class RoundUserData {
    @SerializedName("round") public Round round;
    @SerializedName("user") public User user;
    @SerializedName("globally") public Boolean globally;
    @SerializedName("quizzes") public List<Quiz> quizzes;
    @SerializedName("answers") public List<Question> answers;
    @SerializedName("category") public Category category;


    public RoundUserData() {}
}

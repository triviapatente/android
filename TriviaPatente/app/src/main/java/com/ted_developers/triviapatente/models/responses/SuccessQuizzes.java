package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Quiz;

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

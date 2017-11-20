package com.ted_developers.triviapatente.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.responses.Success;

import org.parceler.Parcel;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class QuestionAnsweredEvent extends Success {
    @SerializedName("quiz_id")
    Integer quiz_id;
    @SerializedName("answer")
    Question answer;

    public QuestionAnsweredEvent() {}
}
package it.triviapatente.android.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.game.Question;
import it.triviapatente.android.models.responses.Success;

import org.parceler.Parcel;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class QuestionAnsweredEvent extends Success {
    @SerializedName("quiz_id")
    public Integer quiz_id;
    @SerializedName("answer")
    public Question answer;

    public QuestionAnsweredEvent() {}
}
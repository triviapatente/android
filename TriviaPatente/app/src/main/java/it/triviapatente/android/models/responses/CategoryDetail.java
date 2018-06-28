package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;
import java.util.Map;

import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.stats.Progress;

/**
 * Created by donadev on 26/06/18.
 */

@Parcel
public class CategoryDetail extends Success {
    @SerializedName("progress") public Map<String, Progress> progressMap;
    @SerializedName("wrong_answers") public List<Quiz> wrongAnswers;

    public CategoryDetail() {}
}

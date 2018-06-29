package it.triviapatente.android.models.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Quiz extends CommonPK {
    @SerializedName("image_id") public Long image_id;
    @SerializedName("question") public String question;
    @SerializedName("answer") public Boolean answer;
    @SerializedName("category_id") public Long category_id;
    @SerializedName("category_hint") public String category_hint;
    @SerializedName("round_id") public Long round_id;
    @SerializedName("my_answer") public Boolean my_answer;
    @SerializedName("order_index") public int order_index;
    @SerializedName("answered_correctly") public Boolean answered_correctly;
    public Category getCategory() {
        return new Category(category_id, category_hint);
    }
    public Quiz() {}
    public JsonObject getTrainingPayload(int index) {
        JsonObject object = new JsonObject();
        object.add("index", new JsonPrimitive(index));
        object.add("answer", my_answer == null ? null : new JsonPrimitive(my_answer));
        return object;
    }
    public List<Question> answers;
}

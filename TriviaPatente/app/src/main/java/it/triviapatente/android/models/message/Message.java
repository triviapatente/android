package it.triviapatente.android.models.message;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Message extends CommonPK {
    @SerializedName("content")
    public String content;
    @SerializedName("user_id")
    public Long user_id;
    @SerializedName("game_id")
    public Long game_id;

    public Message() {}
}
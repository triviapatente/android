package it.triviapatente.android.models.base;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by donadev on 25/06/18.
 */
@Parcel
public class IGImage extends Base {
    @SerializedName("type") String type;
    @SerializedName("url")
    public String preview;
    @SerializedName("link") public String resourceLink;
    public IGImage() {}
}

package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import it.triviapatente.android.models.base.IGImage;

/**
 * Created by donadev on 25/06/18.
 */

public class InstagramImages extends Success {
    @SerializedName("images") public List<IGImage> images;
    public InstagramImages() {}
}

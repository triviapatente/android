package it.triviapatente.android.models.base;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public abstract class Base {
    @SerializedName("createdAt") public String createdAt;
    @SerializedName("updatedAt") public String updatedAt;

    public long getUpdatedAtMillis() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss ZZZZ");
        long time = 0;
        try {
            time = inputFormat.parse(updatedAt).getTime();
        } catch (Exception e) { e.printStackTrace(); }
        return time;
    }

    protected Base() {}
}

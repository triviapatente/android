package com.ted_developers.triviapatente.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.TPImageView.TPGradientDrawable;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Question;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.blurry.Blurry;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;;

/**
 * Created by Antonio on 30/12/16.
 */
public class TPUtils {
    public static boolean isPointInsideView(int x, int y, View view) {
        Rect area = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

        //point is inside view bounds
        return area.contains(x, y);
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }
    private static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String translateEmoticons(String string) {
        Matcher matcher = Pattern.compile("0x[0-9A-Za-z]*").matcher(string);
        while(matcher.find()) {
            String unicode = matcher.group();
            string = string.replaceAll(unicode, getEmojiByUnicode(Integer.decode(unicode)));
        }
        return string;
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static void blurContainerIntoImageView(Context context, ViewGroup container, ImageView imageView) {
        Blurry.with(context).sampling(5).radius(15).capture(container).into(imageView);
    }
    // hide keyboard
    @Deprecated
    public static void hideKeyboard(Activity activity) { hideKeyboard(activity, null); }
    public static void hideKeyboard(Activity activity, View viewGetFocus) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if(viewGetFocus != null) {
                viewGetFocus.requestFocus();
            }
        }
    }


    // picasso utils
    // TODO can be built as singleton instance instead: Picasso.setSingletonInstance(picasso); this way can be called as Picasso.load(...)
    public static Picasso picasso;
    public static void initPicasso(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(context.getString(R.string.shared_token_key), SharedTPPreferences.getToken())
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        // Debug purposes
        // picasso.setIndicatorsEnabled(true);
        // picasso.setLoggingEnabled(true);
    }
    public static void injectUserImage(Context context, User user, RoundedImageView profilePicture) {
        injectUserImage(context, user, profilePicture, true);
    }
    public static void injectUserImage(final Context context, User user, final RoundedImageView profilePicture, final Boolean whiteBorder) {
        Drawable gradientDrawable = new TPGradientDrawable(context);
        TextDrawable textDrawable = TextDrawable.builder()
                                                .beginConfig()
                                                    .width(profilePicture.getLayoutParams().width)
                                                    .height(profilePicture.getLayoutParams().height)
                                                .endConfig()
                                                .buildRound(user.initialLetters(), Color.TRANSPARENT);
        Drawable placeholder = new LayerDrawable(new Drawable[] {gradientDrawable, textDrawable});
        profilePicture.setBorder(Color.WHITE);
        // set image
        TPUtils.picasso
                .load(TPUtils.getUserImageFromID(context, user.id))
                .placeholder(placeholder)
                .into(profilePicture);
    }

    public static String getUserImageFromID(Context context, long ID) {
        return context.getString(R.string.baseUrl)+ "account/image/" + ID;
    }

    public static String getQuizImageFromID(Context context, long ID) {
        return context.getString(R.string.baseUrl)+ "quiz/image/" + ID;
    }

    public static String getCategoryImageFromID(Context context, long ID) {
        return context.getString(R.string.baseUrl)+ "category/image/" + ID;
    }

    public static int getUserScoreFromID(List<Question> answers, Long userID) {
        int score = 0;
        for(Question question : answers) {
            if(question.user_id.equals(userID) && question.correct) {
                score++;
            }
        }
        return score;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

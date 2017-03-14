package com.ted_developers.triviapatente.app.views.preferences;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.loading.LoadingButton;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;

import java.io.File;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUserDetailsActivity extends TPActivity {

    // User data
    User user;

    // Name & Surname
    @BindView(R.id.inputName) LabeledInput nameInput;
    @BindView(R.id.inputSurname) LabeledInput surnameInput;
    @BindString(R.string.activity_change_user_details_name_hint) String nameHint;
    @BindString(R.string.activity_change_user_details_surname_hint) String surnameHint;
    @BindString(R.string.activity_change_user_details_wrong_string) String invalidInput;

    // Image
    @BindView(R.id.bigProfilePicture) RoundedImageView bigProfilePicture;
    Uri imageURI = null;
    Bitmap imageBitmap = null;

    // Overall
    @BindView(R.id.confirmButton) LoadingButton confirmButton;
    String msg, error = "Al momento non è possibile cambiare l'immagine..";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_details);

        // hide keyboard on touch outside
        activityContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        // get user data
        user = SharedTPPreferences.currentUser();

        // load current image
        TPUtils.picasso
                .load(TPUtils.getUserImageFromID(this, user.id))
                .placeholder(R.drawable.image_no_profile_picture)
                .error(R.drawable.image_no_profile_picture)
                .into(bigProfilePicture);

        // init labeled inputs
        initLabeledInputs();
    }

    // hide keyboard
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // init labeled inputs
    private void initLabeledInputs() {
        nameInput.setHint(/*(user.name != null && !user.name.equals(""))? user.name : */nameHint);
        surnameInput.setHint(/*(user.surname != null && !user.name.equals(""))? user.surname : */surnameHint);
    }

    /*
        Take picture CAMERA
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @OnClick(R.id.takeFromCameraButton)
    public void onCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /*
        Take picture STORAGE
     */
    static final int REQUEST_LOAD_IMAGE = 2;
    @OnClick(R.id.takeFromStorageButton)
    public void onStorage() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                case REQUEST_LOAD_IMAGE: {
                    imageURI = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(imageURI,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    imageBitmap = BitmapFactory.decodeFile(picturePath);
                    bigProfilePicture.setImageDrawable(new BitmapDrawable(getResources(), imageBitmap));
                } break;
            }
        }
    }

    // Confirm
    @OnClick(R.id.confirmButton)
    public void onConfirm() {
        // looking for errors..
        boolean valid = true;
        if(!checkNameSurname(nameInput.toString()) && !nameInput.toString().equals("")) {
            nameError();
            valid = false;
        }
        if(!checkNameSurname(surnameInput.toString()) && !surnameInput.toString().equals("")) {
            surnameError();
            valid = false;
        }
        if(valid) {
            confirmButton.startLoading();
            nameUpdate();
        }
    }

    // Name update
    private void nameUpdate() {
        if(!nameInput.toString().equals("")) {
            nameInput.hideLabel();
            Call<User> call = RetrofitManager.getHTTPAuthEndpoint().changeName(nameInput.toString());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    // TODO save data on local shared preferences
                    msg = "Nome modificato";
                    surnameUpdate();
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    nameError();
                }
            });
        } else {
            surnameUpdate();
        }
    }

    private void nameError() {
        nameInput.showLabel(invalidInput);
        confirmButton.stopLoading();
    }

    private void surnameError() {
        surnameInput.showLabel(invalidInput);
        confirmButton.stopLoading();
    }

    // Surname update
    private void surnameUpdate() {
        if(!surnameInput.toString().equals("")) {
            surnameInput.hideLabel();
            Call<User> call = RetrofitManager.getHTTPAuthEndpoint().changeSurname(surnameInput.toString());
            call.enqueue(new TPCallback<User>() {
                @Override
                public void mOnResponse(Call<User> call, Response<User> response) {
                    // TODO save data on local shared preferences
                    if(msg == null || msg.equals("")) {
                        msg = "Cognome modificato";
                    } else {
                        msg = "Nome e cognome modificati";
                    }
                    imageUpdate();
                }
                @Override
                public void mOnFailure(Call<User> call, Throwable t) {
                    surnameError();
                }
                @Override
                public void then() {

                }
            });
        } else {
            imageUpdate();
        }
    }

    // Image update
    private void imageUpdate() {
        // Update image
        if(imageURI != null) {
            File imageFile = new File(imageURI.getPath());
            //RequestBody imageField = RequestBody.create(MediaType.parse("image/*"), imageFile);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageURI)), imageFile);
            MultipartBody.Part imageField = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            Call<User> call = RetrofitManager.getHTTPAuthEndpoint().changeImage(imageField);
            call.enqueue(new TPCallback<User>() {
                @Override
                public void mOnResponse(Call<User> call, Response<User> response) {
                    // TODO save data on local shared preferences
                    Log.i("TEST", "response");
                }

                @Override
                public void mOnFailure(Call<User> call, Throwable t) {
                    Log.i("TEST", "failure");
                }

                @Override
                public void then() {
                    update(true);
                }
            });
        } else {
            update(false);
        }
    }

    private void update(boolean imageUpdated) {
        confirmButton.stopLoading();
        if(msg != null && !"".equals(msg)) Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        if(imageUpdated) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        // Back to previous page
        onBackPressed();
    }

    // Check name & surname input
    private boolean checkNameSurname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
    }
}

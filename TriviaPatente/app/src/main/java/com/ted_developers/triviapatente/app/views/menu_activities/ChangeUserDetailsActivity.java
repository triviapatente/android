package com.ted_developers.triviapatente.app.views.menu_activities;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.LoadingButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInputError;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    // TODO labeled input are probably not needed here
    // User data
    User user;
    @BindString(R.string.activity_change_user_details_title) String title;
    // Name & Surname
    @BindView(R.id.inputName) LabeledInput nameInput;
    @BindView(R.id.inputSurname) LabeledInput surnameInput;
    @BindString(R.string.activity_change_user_details_name_hint) String nameHint;
    @BindString(R.string.activity_change_user_details_surname_hint) String surnameHint;
    @BindString(R.string.activity_change_user_details_wrong_string) String invalidInput;
    @BindView(R.id.email) TextView emailLabel;

    // Image
    @BindView(R.id.bigProfilePicture) RoundedImageView bigProfilePicture;
    Uri imageURI = null;
    Bitmap imageBitmap = null;

    // Overall
    @BindView(R.id.confirmButton) LoadingButton confirmButton;
    @BindString(R.string.activity_change_user_details_error) String error;
    @BindString(R.string.activity_change_user_details_name_modified) String msg_name_modified;
    @BindString(R.string.activity_change_user_details_surname_modified) String msg_surname_modified;
    @BindString(R.string.activity_change_user_details_name_and_surname_modified) String msg_name_surname_modified;
    @BindString(R.string.activity_change_user_details_picture_upload_error) String pictureUploadError;
    @BindString(R.string.activity_change_user_details_picture_chooser_title) String pictureChooserTitle;

    @Override
    protected String getToolbarTitle(){ return title; }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getSettingsVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_details);

        // get user data
        user = SharedTPPreferences.currentUser();

        // load current image
        TPUtils.injectUserImage(this, user, bigProfilePicture);

        // init labeled inputs
        initLabeledInputs();
    }

    // init labeled inputs
    private void initLabeledInputs() {
        // init name surname
        emailLabel.setText(user.email);
        nameInput.setHint(nameHint);
        surnameInput.setHint(surnameHint);
        if(user.name != null && !"".equals(user.name)) nameInput.setText(user.name);
        if(user.surname != null && !"".equals(user.surname)) surnameInput.setText(user.surname);
    }
    private File createCameraPhotoFile() throws IOException {
        String timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
    // Take picture CAMERA
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @OnClick(R.id.takeFromCameraButton)
    public void onCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createCameraPhotoFile();
            imageURI = Uri.fromFile(photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Take picture STORAGE
    static final int REQUEST_LOAD_IMAGE = 2;
    @OnClick(R.id.takeFromStorageButton)
    public void onStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooser = Intent.createChooser(intent, pictureChooserTitle);
        startActivityForResult(chooser, REQUEST_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    imageBitmap = getBitmap(imageURI);
                    break;
                case REQUEST_LOAD_IMAGE:
                    imageURI = data.getData();
                    imageBitmap = getBitmap(imageURI);
            }
            if(imageBitmap != null)
                bigProfilePicture.setImageBitmap(imageBitmap);
        }
    }
    private Bitmap getBitmap(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                    user.name = nameInput.toString();
                    SharedTPPreferences.saveUser(user);
                    surnameUpdate();
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    confirmButton.stopLoading();
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmButton.startLoading();
                                    nameUpdate();
                                }
                            })
                            .show();
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
                    user.surname = surnameInput.toString();
                    SharedTPPreferences.saveUser(user);
                    imageUpdate();
                }
                @Override
                public void mOnFailure(Call<User> call, Throwable t) {
                    confirmButton.stopLoading();
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmButton.startLoading();
                                    surnameUpdate();
                                }
                            })
                            .show();
                }
                @Override
                public void then() {

                }
            });
        } else {
            imageUpdate();
        }
    }
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // Image update
    private void imageUpdate() {
        // Update image
        if(imageBitmap != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("application/image"), getBytes(imageBitmap));
            MultipartBody.Part imageField = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
            Call<User> call = RetrofitManager.getHTTPAuthEndpoint().changeImage(imageField);
            call.enqueue(new TPCallback<User>() {
                @Override
                public void mOnResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()) {
                        String profileImageURL = TPUtils.getUserImageFromID(ChangeUserDetailsActivity.this, currentUser.id);
                        TPUtils.picasso.invalidate(profileImageURL);
                        update();
                    } else {
                        mOnFailure(call, null);
                    }
                }

                @Override
                public void mOnFailure(Call<User> call, Throwable t) {
                    TPUtils.injectUserImage(getApplicationContext(), user, bigProfilePicture);
                    confirmButton.stopLoading();
                    Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                            .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmButton.startLoading();
                                    imageUpdate();
                                }
                            })
                            .show();
                }

                @Override
                public void then() {
                }
            });
        } else {
            update();
        }
    }

    private void update() {
        confirmButton.stopLoading();
        // Back to previous page
        onBackPressed();
    }

    // Check name & surname input
    private boolean checkNameSurname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
    }
}

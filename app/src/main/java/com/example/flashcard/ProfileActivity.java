package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.flashcard.model.user.UpdateResponse;
import com.example.flashcard.model.user.User;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.ResetPasswordConfirmListener;
import com.example.flashcard.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements ResetPasswordConfirmListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private SharedPreferences sharedPref;
//    private final ApiClient apiClient = ApiClient.getClient().create(ApiClient.class);
    private final int PICK_IMAGE_INTENT = 1;
    private User user;
    private ShapeableImageView profileImage;
    private EditText profileUser;
    private ImageButton pickImageButton;
    private MaterialButton changePassword;
    private Button saveProfileBtn;
    private ProgressBar updateProfileProgress;
    private LinearLayout profileContent;
    private ImageButton returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getUser();

        profileImage = findViewById(R.id.profileImage);
        profileUser = findViewById(R.id.profileUserEdt);
        pickImageButton = findViewById(R.id.pickImageButton);
        changePassword = findViewById(R.id.changePassword);
        saveProfileBtn = findViewById(R.id.saveProfileBtn);
        updateProfileProgress = findViewById(R.id.updateProfileProgress);
        profileContent = findViewById(R.id.profileContent);
        returnBtn = findViewById(R.id.returnBtn);

        if(user.getProfileImage() != null) {
            Picasso.get().load(Uri.parse(user.getProfileImage())).into(profileImage);
        }
        sharedPref = getSharedPreferences(Constant.SHARE_PREF, Context.MODE_PRIVATE);
        profileUser.setText(user.getUsername());

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent pickingImage = new Intent(Intent.ACTION_PICK);
//                pickingImage.setType("image/*");
//                pickingImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
//                startActivityForResult(pickingImage, PICK_IMAGE_INTENT);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showChangePasswordDialog(Gravity.CENTER, v.getContext(), ProfileActivity.this);
            }
        });

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = profileUser.getText().toString();
                if(username.isEmpty()){
                    Utils.showSnackBar(v, "Please fill all your information");
                    return;
                }

//                updateProfileProgress.setVisibility(View.VISIBLE);
//                profileContent.setVisibility(View.INVISIBLE);
                updateUserProfile(user.getId(), profileUser.getText().toString(), convertBitmapToString());
            }
        });
        /*saveProfileBtn.setOnClickListener(v -> {
            if (username.isEmpty() || almaMater.isEmpty()) {
                Utils.showSnackBar(binding.getRoot(), getString(R.string.please_fill));
                return;
            }
            binding.updateProfileProgress.setVisibility(View.VISIBLE);
            binding.profileContent.setVisibility(View.INVISIBLE);
            String token = sharedPref.getString(getString(R.string.token_key), null);
            dataRepo.updateUser(username, almaMater, user.getId(), token)
                    .thenAcceptAsync(it -> {
                        with(sharedPref.edit()) {
                            String newUserJson = new Gson().toJson(it.getUser());
                            putString(getString(R.string.user_data_key), newUserJson);
                            apply();
                            user = it.getUser();
                        }
                        runOnUiThread(() -> {
                            binding.profileContent.setVisibility(View.VISIBLE);
                            binding.updateProfileProgress.setVisibility(View.GONE);
                            Utils.showDialog(Gravity.CENTER, it.getMessage(), this);
                        });
                    })
                    .exceptionally(e -> {
                        runOnUiThread(() -> {
                            binding.profileContent.setVisibility(View.VISIBLE);
                            binding.updateProfileProgress.setVisibility(View.GONE);
                            Utils.showDialog(Gravity.CENTER, e.getMessage(), this);
                        });
                        return null;
                    });
        });*/

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent();
                goBack.putExtra(Constant.USER_DATA, user);
                setResult(Activity.RESULT_OK, goBack);
                finish();
            }
        });

    }

    private void getUser() {
        user = getIntent().getParcelableExtra(Constant.USER_DATA);
        if (user == null) {
            finish();
        }
    }

    @Override
    public void onConfirm(String email) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    private void updateUserProfile(int userID, String newUsername, String newProfileImage) {
        ApiService apiService = ApiClient.getClient();

        Call<UpdateResponse> call = apiService.updateUserProfile(userID, newUsername, newProfileImage);

        call.enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                if (response.isSuccessful()) {
                    UpdateResponse apiResponse = response.body();
                    if (apiResponse != null && "OK".equals(apiResponse.getStatus())) {
                        // Xử lý khi cập nhật thành công
                        // Ví dụ: Hiển thị thông báo, cập nhật UI, v.v.
                    } else {
                        // Xử lý khi cập nhật không thành công
                        // Ví dụ: Hiển thị thông báo lỗi
                    }
                } else {
                    // Xử lý khi có lỗi kết nối đến máy chủ
                    // Ví dụ: Hiển thị thông báo lỗi kết nối
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                // Xử lý khi có lỗi trong quá trình thực hiện request
                // Ví dụ: Hiển thị thông báo lỗi
            }
        });
    }
    private String convertBitmapToString() {
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_INTENT && resultCode == Activity.RESULT_OK && data != null) {
//            Log.d("ProfileActivity", "OK");
//        }
////            Uri selectedImageUri = data.getData();
////            InputStream inputStream = null;
////            try {
////                inputStream = getContentResolver().openInputStream(selectedImageUri);
////                File file = new File(getCacheDir(), "upload_image.jpg");
////                FileOutputStream outputStream = new FileOutputStream(file);
////                if (inputStream != null) {
////                    byte[] buffer = new byte[1024];
////                    int bytesRead;
////                    while ((bytesRead = inputStream.read(buffer)) != -1) {
////                        outputStream.write(buffer, 0, bytesRead);
////                    }
////                    inputStream.close();
////                }
////                String token = sharedPref.getString(getString(R.string.token_key), null);
////                profileContent.setVisibility(View.INVISIBLE);
////                updateProfileProgress.setVisibility(View.VISIBLE);
////                apiClient.uploadImage(file, account.getId(), token).thenAcceptAsync(it -> {
////                    SharedPreferences.Editor editor = sharedPref.edit();
////                    String newUserJson = new Gson().toJson(acc.getUser());
////                    editor.putString(getString(R.string.user_data_key), newUserJson);
////                    editor.apply();
////                    user = it.getUser();
////                    Log.d("USER TAG", new Gson().toJson(user));
////                    runOnUiThread(() -> {
////                        profileImage.setImageURI(data.getData());
////                        profileContent.setVisibility(View.VISIBLE);
////                        updateProfileProgress.setVisibility(View.GONE);
////                        Utils.showDialog(Gravity.CENTER, it.getMessage(), ProfileActivity.this);
////                    });
////                }).exceptionally(e -> {
////                    runOnUiThread(() -> {
////                        profileContent.setVisibility(View.VISIBLE);
////                        updateProfileProgress.setVisibility(View.GONE);
////                        Utils.showDialog(Gravity.CENTER, e, ProfileActivity.this);
////                    });
////                    return null;
////                });
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
//    }
}
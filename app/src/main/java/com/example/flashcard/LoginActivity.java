package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.flashcard.model.Accounts;
import com.example.flashcard.model.user.User;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdt, passEdt;
    private Button loginBtn;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        emailEdt = findViewById(R.id.edtEmail);
        passEdt = findViewById(R.id.edtPassword);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginBtn = findViewById(R.id.loginBtn);

        linkSetup();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<User> call = apiService.getAccount(emailEdt.getText().toString(), passEdt.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("API", "Raw JSON response: " + new Gson().toJson(response.body()));
                            User user = response.body();
                            if ((emailEdt.getText().toString().equals(user.getUsername()) || emailEdt.getText().toString().equals(user.getEmail())) && passEdt.getText().toString().equals(user.getPassword())) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            Log.e("LoginActivity", "API call failed. Error: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Log lỗi onFailure
                        Log.e("LoginActivity", "API call failed. Throwable: " + t.getMessage());
                    }
                });
            }
        });

    }

    private void linkSetup(){
        Link userLink = new Link("username")
                .setTextColor(Color.parseColor("#0000FF"))
                .setHighlightAlpha(.4f)
                .setUnderlined(false)
                .setBold(true)
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                    }
                });

        Link passLink = new Link("password")
                .setTextColor(Color.parseColor("#0000FF"))
                .setHighlightAlpha(.4f)
                .setUnderlined(false)
                .setBold(true)
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                    }
                });

        Link termsLink = new Link("Terms of service")
                .setTextColor(Color.parseColor("#0000FF"))
                .setHighlightAlpha(.4f)
                .setUnderlined(false)
                .setBold(true)
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                    }
                });

        Link policyLink = new Link("Privacy policy")
                .setTextColor(Color.parseColor("#0000FF"))
                .setHighlightAlpha(.4f)
                .setUnderlined(false)
                .setBold(true)
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                    }
                });

        LinkBuilder.on(findViewById(R.id.tvLink))
                .addLink(termsLink)
                .addLink(policyLink)
                .build();
        LinkBuilder.on(findViewById(R.id.tvUserAndPass))
                .addLink(userLink)
                .addLink(passLink)
                .build();
    }

}
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

import com.example.flashcard.model.account.Account;
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

    private EditText userEdt, passEdt;
    private Button loginBtn;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        userEdt = findViewById(R.id.edtUser);
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
                Call<Account> call = apiService.getAccount(userEdt.getText().toString(), passEdt.getText().toString());
                call.enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("API", "Raw JSON response: " + new Gson().toJson(response.body()));
                            Account account = response.body();
                            Log.d("LoginActivity", "Received account data: " +
                                    "ID: " + account.getId() +
                                    ", Username: " + account.getUsername() +
                                    ", Password: " + account.getPassword() +
                                    ", Email: " + account.getEmail() +
                                    ", Name: " + account.getName() +
                                    ", Age: " + account.getAge() +
                                    ", Avatar: " + account.getAvatar());

                            Gson gson = new GsonBuilder().setLenient().create();
                            String json = gson.toJson(account);
                            Log.d("API", "API call successful. Received account data: " + json);

                            if (userEdt.getText().toString().equals(account.getUsername()) && passEdt.getText().toString().equals(account.getPassword())) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else{

                            }
                        } else {
                            Log.e("LoginActivity", "API call failed. Error: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
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
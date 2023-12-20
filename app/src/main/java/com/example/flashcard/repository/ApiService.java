package com.example.flashcard.repository;

import com.example.flashcard.model.account.Account;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
public interface ApiService {
    @FormUrlEncoded
    @POST("get_Account.php")
    Call<Account> getAccount(@Field("username") String username, @Field("password") String password);
}

package com.example.flashcard.repository;

import com.example.flashcard.model.Accounts;
import com.example.flashcard.model.user.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface ApiService {
    @FormUrlEncoded
    @POST("get_Account.php")
    Call<User> getAccount(@Field("username") String username, @Field("password") String password);
}

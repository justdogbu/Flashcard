package com.example.flashcard.repository;

import com.example.flashcard.model.user.LoginResponse;
import com.example.flashcard.model.user.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
public interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(@Field("username") String username, @Field("password") String password, @Field("email") String email, @Field("age") int age);
}

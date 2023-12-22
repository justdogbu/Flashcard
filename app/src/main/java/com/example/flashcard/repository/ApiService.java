package com.example.flashcard.repository;

import com.example.flashcard.model.user.LoginResponse;
import com.example.flashcard.model.user.RegisterResponse;
import com.example.flashcard.model.user.UpdateResponse;
import com.example.flashcard.model.user.User;

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

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<UpdateResponse> updateUserProfile(
            @Field("userID") int userID,
            @Field("newUsername") String newUsername,
            @Field("newProfileImage") String newProfileImage
    );
}

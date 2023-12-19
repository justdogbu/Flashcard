package com.example.flashcard.model;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface ApiService {
    @FormUrlEncoded
    @POST("get_Account.php")
    Call<Accounts> getAccount(@Field("username") String username, @Field("password") String password);
}

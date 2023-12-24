package com.example.flashcard.repository;

import com.example.flashcard.model.topic.TopicDetailResponse;
import com.example.flashcard.model.topic.TopicResponse;
import com.example.flashcard.model.user.LoginResponse;
import com.example.flashcard.model.user.RegisterResponse;
import com.example.flashcard.model.user.UpdateResponse;
import com.example.flashcard.model.user.User;
import com.example.flashcard.model.vocabulary.VocabuResponse;

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
            @Field("newEmail") String newEmail,
            @Field("newProfileImage") String newProfileImage,
            @Field("newPassword") String newPassword,
            @Field("newAge") Integer newAge
    );

    @FormUrlEncoded
    @POST("createTopic.php")
    Call<TopicResponse> CreateTopic(
            @Field("topicName") String topicName,
            @Field("description") String description,
            @Field("isPublic") int isPublic,
            @Field("ownerID") int ownerID
    );

    @FormUrlEncoded
    @POST("insertTopicDetail.php")
    Call<TopicDetailResponse> insertTopicDetail(
            @Field("topicID") int topicID,
            @Field("userID") int userID
    );

    @FormUrlEncoded
    @POST("createVocabulary.php")
    Call<VocabuResponse> createVocabulary(
            @Field("vocabulary") String vocabulary,
            @Field("meaning") String meaning,
            @Field("topicID") int topicID
    );
}

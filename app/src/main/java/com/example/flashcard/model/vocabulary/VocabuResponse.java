package com.example.flashcard.model.vocabulary;

import com.example.flashcard.model.user.User;
import com.google.gson.annotations.SerializedName;

public class VocabuResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private Vocabulary data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public Vocabulary getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

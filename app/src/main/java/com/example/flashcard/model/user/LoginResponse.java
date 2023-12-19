package com.example.flashcard.model.user;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginResponse implements Parcelable {
    private User user;
    private String token;
    private String error;

    public LoginResponse(User user, String token, String error) {
        this.user = user;
        this.token = token;
        this.error = error;
    }

    protected LoginResponse(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        token = in.readString();
        error = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(token);
        dest.writeString(error);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }
}

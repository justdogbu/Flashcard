package com.example.flashcard.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.flashcard.model.account.Account;
import com.google.gson.annotations.SerializedName;

public class LoginResponse implements Parcelable {
    @SerializedName("data")

    private Account account;
    @SerializedName("status")

    private String status;
    @SerializedName("message")

    private String message;

    public LoginResponse(Account account, String status, String message) {
        this.account = account;
        this.status = status;
        this.message = message;
    }

    protected LoginResponse(Parcel in) {
        account = in.readParcelable(Account.class.getClassLoader());
        status = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(account, flags);
        dest.writeString(status);
        dest.writeString(message);
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

    public Account getAccount() {
        return account;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

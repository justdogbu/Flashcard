package com.example.flashcard.model.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {
    @SerializedName("_id")
    private String id;
    private List<String> achievementId;
    private String email;
    private String username;
    private String profileImage;
    private List<Object> bookmarkVocabularyId;
    private List<Object> vocabularyStatisticId;
    private List<Object> folderId;
    private List<Object> learningStatisticsId;
    private List<Object> topicId;
    @SerializedName("__v")
    private long v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(List<String> achievementId) {
        this.achievementId = achievementId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<Object> getBookmarkVocabularyId() {
        return bookmarkVocabularyId;
    }

    public void setBookmarkVocabularyId(List<Object> bookmarkVocabularyId) {
        this.bookmarkVocabularyId = bookmarkVocabularyId;
    }

    public List<Object> getVocabularyStatisticId() {
        return vocabularyStatisticId;
    }

    public void setVocabularyStatisticId(List<Object> vocabularyStatisticId) {
        this.vocabularyStatisticId = vocabularyStatisticId;
    }

    public List<Object> getFolderId() {
        return folderId;
    }

    public void setFolderId(List<Object> folderId) {
        this.folderId = folderId;
    }

    public List<Object> getLearningStatisticsId() {
        return learningStatisticsId;
    }

    public void setLearningStatisticsId(List<Object> learningStatisticsId) {
        this.learningStatisticsId = learningStatisticsId;
    }

    public List<Object> getTopicId() {
        return topicId;
    }

    public void setTopicId(List<Object> topicId) {
        this.topicId = topicId;
    }

    public long getV() {
        return v;
    }

    public void setV(long v) {
        this.v = v;
    }

    public User(String id, List<String> achievementId, String email, String username,
                String profileImage, List<Object> bookmarkVocabularyId,
                List<Object> vocabularyStatisticId, List<Object> folderId, List<Object> learningStatisticsId,
                List<Object> topicId, long v) {
        this.id = id;
        this.achievementId = achievementId;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
        this.bookmarkVocabularyId = bookmarkVocabularyId;
        this.vocabularyStatisticId = vocabularyStatisticId;
        this.folderId = folderId;
        this.learningStatisticsId = learningStatisticsId;
        this.topicId = topicId;
        this.v = v;
    }

    protected User(Parcel in) {
        id = in.readString();
        achievementId = in.createStringArrayList();
        email = in.readString();
        username = in.readString();
        profileImage = in.readString();
        bookmarkVocabularyId = in.readArrayList(Object.class.getClassLoader());
        vocabularyStatisticId = in.readArrayList(Object.class.getClassLoader());
        folderId = in.readArrayList(Object.class.getClassLoader());
        learningStatisticsId = in.readArrayList(Object.class.getClassLoader());
        topicId = in.readArrayList(Object.class.getClassLoader());
        v = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringList(achievementId);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(profileImage);
        dest.writeList(bookmarkVocabularyId);
        dest.writeList(vocabularyStatisticId);
        dest.writeList(folderId);
        dest.writeList(learningStatisticsId);
        dest.writeList(topicId);
        dest.writeLong(v);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getter methods...
}

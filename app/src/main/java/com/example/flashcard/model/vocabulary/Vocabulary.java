package com.example.flashcard.model.vocabulary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Vocabulary implements Parcelable {
    private String id;
    private String vocabulary;
    private String meaning;
    private String topicId;
    private List<String> vocabularyStatisticId;
    private List<String> bookmarkVocabularyId;
    private boolean isFront;

    public Vocabulary(
            String id,
            String vocabulary,
            String meaning,
            String topicId,
            List<String> vocabularyStatisticId,
            List<String> bookmarkVocabularyId,
            boolean isFront) {
        this.id = id;
        this.vocabulary = vocabulary;
        this.meaning = meaning;
        this.topicId = topicId;
        this.vocabularyStatisticId = vocabularyStatisticId;
        this.bookmarkVocabularyId = bookmarkVocabularyId;
        this.isFront = isFront;
    }

    protected Vocabulary(Parcel in) {
        id = in.readString();
        vocabulary = in.readString();
        meaning = in.readString();
        topicId = in.readString();
        vocabularyStatisticId = in.createStringArrayList();
        bookmarkVocabularyId = in.createStringArrayList();
        isFront = in.readByte() != 0;
    }

    public static final Creator<Vocabulary> CREATOR = new Creator<Vocabulary>() {
        @Override
        public Vocabulary createFromParcel(Parcel in) {
            return new Vocabulary(in);
        }

        @Override
        public Vocabulary[] newArray(int size) {
            return new Vocabulary[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getTopicId() {
        return topicId;
    }

    public List<String> getVocabularyStatisticId() {
        return vocabularyStatisticId;
    }

    public List<String> getBookmarkVocabularyId() {
        return bookmarkVocabularyId;
    }

    public boolean isFront() {
        return isFront;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(vocabulary);
        dest.writeString(meaning);
        dest.writeString(topicId);
        dest.writeStringList(vocabularyStatisticId);
        dest.writeStringList(bookmarkVocabularyId);
        dest.writeByte((byte) (isFront ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vocabulary other = (Vocabulary) obj;

        return id != null ? id.equals(other.id) : other.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
package com.example.flashcard.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flashcard.model.vocabulary.Vocabulary;

import java.util.List;

public class TopicViewModel extends ViewModel {
    private MutableLiveData<List<Vocabulary>> vocabularyList = new MutableLiveData<>();
    //private MutableLiveData<List<StatisticResponse>> statisticList = new MutableLiveData<>();
    public MutableLiveData<List<Vocabulary>> getVocabularies() {
        return vocabularyList;
    }

    public void setVocabulariesList(List<Vocabulary> vocabularies) {
        vocabularyList.setValue(vocabularies);
    }

    /*
    public MutableLiveData<List<StatisticResponse>> getStatistics() {
        return statisticList;
    }

    public void setStatisticsList(List<StatisticResponse> statistics) {
        statisticList.setValue(statistics);
    }

     */
}

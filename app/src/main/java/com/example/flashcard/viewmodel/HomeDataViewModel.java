package com.example.flashcard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flashcard.model.account.Account;
import com.example.flashcard.model.folder.Folder;
import com.example.flashcard.model.topic.Topic;

import java.util.List;

public class HomeDataViewModel extends ViewModel {
    private MutableLiveData<Account> user = new MutableLiveData<>();
    private MutableLiveData<List<Topic>> topicsList = new MutableLiveData<>();
    private MutableLiveData<List<Folder>> folderList = new MutableLiveData<>();
    private MutableLiveData<List<Topic>> publicTopicsList = new MutableLiveData<>();

    public LiveData<Account> getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user.setValue(user);
    }

    public LiveData<List<Topic>> getTopicsList() {
        return topicsList;
    }

    public void setTopicsList(List<Topic> topicsList) {
        this.topicsList.setValue(topicsList);
    }

    public LiveData<List<Folder>> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<Folder> folderList) {
        this.folderList.setValue(folderList);
    }

    public LiveData<List<Topic>> getPublicTopicsList() {
        return publicTopicsList;
    }

    public void setPublicTopicsList(List<Topic> publicTopicsList) {
        this.publicTopicsList.setValue(publicTopicsList);
    }
}

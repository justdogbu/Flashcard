package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcard.adapter.VocabularyInFlashcardAdapter;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.user.User;
import com.example.flashcard.model.vocabulary.Vocabulary;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.OnTopicDialogListener;
import com.example.flashcard.utils.Utils;
import com.example.flashcard.viewmodel.TopicViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, OnTopicDialogListener {
    private TopicViewModel topicViewModel;
    private ApiService apiService;
    private Topic topic;
    private List<Vocabulary> vocabulariesList;
    private List<Vocabulary> originalVocabulariesList;
    private List<Vocabulary> bookmarkedVocabulariesList;
    private SharedPreferences sharedPreferences;
    private VocabularyInFlashcardAdapter vocabularyInFlashcardAdapter;
    private TextToSpeech ttsEnglish;
    private TextToSpeech ttsVietnamese;
    private ActivityResultLauncher<Intent> addTopicToFolderResultLauncher;
    private ActivityResultLauncher<Intent> editTopicVocabulariesResultLauncher;
    private ImageButton optionMenuBtn;
    private ImageButton returnBtn;
    private ViewPager2 flashCardViewPager;
    private MaterialButton learnByFlashCardBtn;
    private MaterialButton learnByQuizBtn;
    private MaterialButton learnByTypingBtn;
    private MaterialButton rankingBtn;
    private ShapeableImageView topicUserImg;
    private TextView topicUserName;
    private TextView topicName;
    private TextView topicDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        Topic data = intent.getParcelableExtra("topic");
        ttsEnglish = new TextToSpeech(this, this);
        ttsVietnamese = new TextToSpeech(this, this);
        optionMenuBtn = findViewById(R.id.optionMenuBtn);
        returnBtn = findViewById(R.id.returnBtn);
        flashCardViewPager = findViewById(R.id.flashCardViewPager);
        topicUserImg = findViewById(R.id.topicUserImg);
        topicUserName = findViewById(R.id.topicUserName);
        learnByFlashCardBtn = findViewById(R.id.learnByFlashCardBtn);
        learnByQuizBtn = findViewById(R.id.learnByQuizBtn);
        learnByTypingBtn = findViewById(R.id.learnByTypingBtn);
        topicName = findViewById(R.id.topicName);
        topicDesc = findViewById(R.id.topicDesc);

        if (data == null) {
            finish();
        } else {
            topic = data;
        }

        apiService = ApiClient.getClient();
        sharedPreferences = getSharedPreferences(Constant.SHARE_PREF, MODE_PRIVATE);

        optionMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopicDetailBottom bottomSheet = new TopicDetailBottom();
                Bundle bundle = new Bundle();
                bundle.putParcelable("topic", topic);
                User user = Utils.getUserFromSharedPreferences(TopicActivity.this, sharedPreferences);
                boolean isYourTopic = topic.getOwnerId() == user.getId();
                bundle.putBoolean("isYourTopic", isYourTopic);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "topicBottomSheet");
            }
        });

        topicName.setText(topic.getTopicName());
        topicDesc.setText(topic.getDescription());

        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        topicViewModel.setVocabulariesList(new ArrayList<>());
        //initViewModel();

        returnBtn.setOnClickListener(v -> {
            finish();
        });

        learnByQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopicActivity.this, StudySetupActivity.class);
                intent.putExtra("topic", topic);
                intent.putExtra("studyMode", Constant.StudyMode.Quiz);
                //intent.putParcelableArrayListExtra("vocabularies", new ArrayList<>(vocabulariesList));
                //intent.putParcelableArrayListExtra("bookmarkedVocabularies", new ArrayList<>(bookmarkedVocabulariesList));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onSaveToFolder() {

    }

    @Override
    public void onDeleteTopic() {

    }

    @Override
    public void onEditTopic() {

    }
}
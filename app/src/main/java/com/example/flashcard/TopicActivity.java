package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.flashcard.adapter.VocabularyInFlashcardAdapter;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.topic.Topics;
import com.example.flashcard.model.topic.TopicsFormUserResponse;
import com.example.flashcard.model.user.User;
import com.example.flashcard.model.vocabulary.VocabulariesFromTopicResponse;
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
import java.util.Locale;
import java.util.stream.Collectors;

import kotlinx.coroutines.CoroutineScope;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class TopicActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, OnTopicDialogListener {
    private TopicViewModel topicViewModel;
    private ApiService apiService;
    private Topic topic;
    private List<Vocabulary> vocabulariesList;
    private List<Vocabulary> originalVocabulariesList;
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
    private ProgressBar fullScreenProgressBar;
    private ScrollingPagerIndicator scrollPagerIndicator;
    private ProgressBar progressBar;
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
        fullScreenProgressBar = findViewById(R.id.fullScreenProgressBar);
        scrollPagerIndicator = findViewById(R.id.scrollPagerIndicator);
        progressBar = findViewById(R.id.progressBar);
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
        initViewModel();

        returnBtn.setOnClickListener(v -> {
            finish();
        });

        learnByQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopicActivity.this, StudySetupActivity.class);
                intent.putExtra("topic", topic);
                intent.putExtra("studyMode", Constant.StudyMode.Quiz);
                intent.putParcelableArrayListExtra("vocabularies", new ArrayList<>(vocabulariesList));
                startActivity(intent);
            }
        });

        learnByTypingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopicActivity.this, StudySetupActivity.class);
                intent.putExtra("topic", topic);
                intent.putExtra("studyMode", Constant.StudyMode.Typing);
                intent.putParcelableArrayListExtra("vocabularies", new ArrayList<>(vocabulariesList));
                startActivity(intent);
            }
        });

        learnByFlashCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopicActivity.this, FlashcardActivity.class);
                intent.putExtra("topic", topic);
                intent.putParcelableArrayListExtra("vocabularies", new ArrayList<>(vocabulariesList));
                startActivity(intent);
            }
        });

        topicViewModel.getVocabularies().observe(this, new Observer<List<Vocabulary>>() {
            @Override
            public void onChanged(List<Vocabulary> items) {
                vocabulariesList = items;
                vocabularyInFlashcardAdapter = new VocabularyInFlashcardAdapter(true, TopicActivity.this, items, R.layout.flashcard_layout_item, ttsEnglish, ttsVietnamese);
                flashCardViewPager.setAdapter(vocabularyInFlashcardAdapter);
                flashCardViewPager.setOffscreenPageLimit(3);
                flashCardViewPager.setClipToPadding(false);
                scrollPagerIndicator.attachToPager(flashCardViewPager);

                if (items.size() < 2) {
                    learnByTypingBtn.setVisibility(View.GONE);
                    learnByQuizBtn.setVisibility(View.GONE);
                } else {
                    learnByTypingBtn.setVisibility(View.VISIBLE);
                    learnByQuizBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initViewModel() {
        progressBar.setVisibility(View.VISIBLE);
        apiService = ApiClient.getClient();
        Call<VocabulariesFromTopicResponse> call = apiService.getVocabulariesFromTopic(topic.getId());
        call.enqueue(new Callback<VocabulariesFromTopicResponse>() {
            @Override
            public void onResponse(Call<VocabulariesFromTopicResponse> call, Response<VocabulariesFromTopicResponse> response) {
                progressBar.setVisibility(View.GONE);
                VocabulariesFromTopicResponse vocabulariesFromTopicResponse = response.body();
                List<Vocabulary> listVocab = vocabulariesFromTopicResponse.getData();
                topicViewModel.setVocabulariesList(listVocab);
            }

            @Override
            public void onFailure(Call<VocabulariesFromTopicResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Utils.showDialog(Gravity.CENTER, "ERROR WHEN LOAD TOPIC", TopicActivity.this);
            }
        });
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = ttsEnglish.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                Utils.showDialog(Gravity.CENTER, "THIS LANGUAGE IS NOT SUPPORTED", TopicActivity.this);
            }

            int res = ttsVietnamese.setLanguage(new Locale("vi"));
            if (res == TextToSpeech.LANG_MISSING_DATA) {
                Utils.showDialog(Gravity.CENTER, "THIS LANGUAGE IS NOT SUPPORTED", TopicActivity.this);
            }
        } else {
            Utils.showDialog(Gravity.CENTER, "FAILED", TopicActivity.this);
        }
    }

    @Override
    public void onSaveToFolder() {
        Intent intent = new Intent(this, AddTopicFolderActivity.class);
        intent.putExtra("topic", topic);
        addTopicToFolderResultLauncher.launch(intent);
    }

    @Override
    public void onDeleteTopic() {
        fullScreenProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEditTopic() {
        Intent intent = new Intent(TopicActivity.this, CreateTopicActivity.class);
        intent.putExtra("topic", topic);
        intent.putExtra("isEdit", true);
        editTopicVocabulariesResultLauncher.launch(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initViewModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ttsEnglish != null){
            ttsEnglish.stop();
            ttsEnglish.shutdown();
        }
        if(ttsVietnamese != null){
            ttsVietnamese.stop();
            ttsVietnamese.shutdown();
        }
    }
}
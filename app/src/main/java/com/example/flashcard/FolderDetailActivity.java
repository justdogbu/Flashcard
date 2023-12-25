package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flashcard.adapter.TopicAdapter;
import com.example.flashcard.model.folder.Folder;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.user.User;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.CustomOnItemClickListener;
import com.example.flashcard.utils.OnDialogConfirmListener;
import com.example.flashcard.viewmodel.FolderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FolderDetailActivity extends AppCompatActivity implements CustomOnItemClickListener, OnDialogConfirmListener {
    private Folder folder;
    private SharedPreferences sharedPreferences;
    private User user;
    private ActivityResultLauncher<Intent> addTopicResultLauncher;
    private TopicAdapter adapter;
    private List<Topic> topicList = new ArrayList<>();
    private ApiService apiService;
    private FolderViewModel folderViewModel;
    private ImageButton returnBtn;
    private ImageButton optionMenuBtn;
    private ShapeableImageView folderUserImg;
    private TextView folderUserName;
    private TextView folderTopicCount;
    private TextView folderTitle;
    private LinearLayout noTopicsInFolderLayout;
    private RecyclerView folderDetailRecyclerView;
    private Button addTopicToFolderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);

        folder = getIntent().getParcelableExtra("folder");
        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        if (folder == null) {
            finish();
        }

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(v -> finish());

        sharedPreferences = getSharedPreferences(Constant.SHARE_PREF, MODE_PRIVATE);
        if (sharedPreferences.getString(Constant.USER_DATA, null) == null) {
            finish();
        } else {
            user = new Gson().fromJson(sharedPreferences.getString(Constant.USER_DATA, null), User.class);
        }

        optionMenuBtn = findViewById(R.id.optionMenuBtn);
        optionMenuBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("folder", folder);
            BottomSheetDialogFragment bottomSheet = new FolderDetailBottom();
            bottomSheet.setArguments(bundle);
            bottomSheet.showNow(getSupportFragmentManager(), "folder detail sheet");
        });

        apiService = ApiClient.getClient();
        folderUserImg = findViewById(R.id.folderUserImg);
        Picasso.get().load(user.getProfileImage()).into(folderUserImg);

        folderUserName = findViewById(R.id.folderUserName);
        folderUserName.setText(user.getUsername());

        folderTopicCount = findViewById(R.id.folderTopicCount);
        //folderTopicCount.setText(topicList.size() + " TOPIC");

        folderTitle = findViewById(R.id.folderTitle);
        folderTitle.setText(folder.getFolderName());

        noTopicsInFolderLayout = findViewById(R.id.noTopicsInFolderLayout);
        folderDetailRecyclerView = findViewById(R.id.folderDetailRecyclerView);
        if (topicList.size() == 0) {
            folderDetailRecyclerView.setVisibility(View.GONE);
            noTopicsInFolderLayout.setVisibility(View.VISIBLE);
        } else {
//            folderDetailRecyclerView.setVisibility(View.VISIBLE);
//            noTopicsInFolderLayout.setVisibility(View.GONE);
////            apiService.getTopicByFolderId(folder.getId());
////                    .thenAcceptAsync(topics -> runOnUiThread(() -> {
////                        List<Topic> mutableList = new ArrayList<>(topics);
////                        topicList = new ArrayList<>(mutableList);
////                        adapter = new TopicAdapter(FolderDetailActivity.this, mutableList, R.layout.topic_library_item, FolderDetailActivity.this);
////                        binding.folderDetailRecyclerView.setHasFixedSize(true);
////                        binding.folderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(FolderDetailActivity.this, LinearLayoutManager.VERTICAL, false));
////                        binding.folderDetailRecyclerView.setAdapter(adapter);
////                    }))
////                    .exceptionally(e -> {
////                        runOnUiThread(() -> Utils.showSnackBar(binding.getRoot(), e.getMessage()));
////                        return null;
////                    });
        }

        addTopicResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                ArrayList<Topic> topics = result.getData().getParcelableArrayListExtra("topics");
                if (topics != null) {
                    List<Topic> currentList = topicList;
                    List<Topic> addedTopic = topics.stream().filter(topic -> !currentList.contains(topic)).collect(Collectors.toList());
                    List<Topic> removedTopic = currentList.stream().filter(topic -> !topics.contains(topic)).collect(Collectors.toList());

                    folderViewModel.updateTopicForFolder(apiService, getCurrentFocus(), FolderDetailActivity.this, addedTopic, removedTopic, folder, sharedPreferences);
                    runOnUiThread(() -> {
                        topicList = new ArrayList<>(topics);
                        if (topics.isEmpty()) {
                            folderTopicCount.setText("0 Topic");
                            folderDetailRecyclerView.setVisibility(View.GONE);
                            noTopicsInFolderLayout.setVisibility(View.VISIBLE);
                        } else {
                            folderTopicCount.setText(topics.size() + " Topics");
                            folderDetailRecyclerView.setVisibility(View.VISIBLE);
                            noTopicsInFolderLayout.setVisibility(View.GONE);
                            adapter = new TopicAdapter(FolderDetailActivity.this, topics, R.layout.topic_library_item, FolderDetailActivity.this);
                            folderDetailRecyclerView.setHasFixedSize(true);
                            folderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(FolderDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                            folderDetailRecyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
        addTopicToFolderBtn = findViewById(R.id.addTopicToFolderBtn);
        addTopicToFolderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FolderDetailActivity.this, AddTopicToFolderActivity.class);
            intent.putParcelableArrayListExtra("currentTopics", new ArrayList<>());
            addTopicResultLauncher.launch(intent);
        });

    }

    @Override
    public void onTopicClick(Topic topic) {
        Intent intent = new Intent(FolderDetailActivity.this, TopicActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    @Override
    public void onFolderClick(Folder folder) {

    }

    @Override
    public void onCreateFolderDialogConfirm(String folderName, String description) {

    }

    @Override
    public void onAddTopicToFolderDialogConfirm() {

    }

    @Override
    public void onDeleteFolderDialogConfirm() {

    }
}

package com.example.flashcard;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.adapter.TopicAdapter;
import com.example.flashcard.model.folder.Folder;
import com.example.flashcard.model.folder.FolderResponse;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.topic.TopicFromFolderResponse;
import com.example.flashcard.model.user.User;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.CustomOnItemClickListener;
import com.example.flashcard.utils.OnDialogConfirmListener;
import com.example.flashcard.utils.Utils;
import com.example.flashcard.viewmodel.FolderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderDetailActivity extends AppCompatActivity implements CustomOnItemClickListener, OnDialogConfirmListener {
    private Folder folder;
    private SharedPreferences sharedPreferences;
    private User user;
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

        apiService = ApiClient.getClient();
        folder = getIntent().getParcelableExtra("folder");
        searchFolder();
        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        if (folder == null) {
            finish();
        }

        Log.d("test folder", folder.getId() + "");
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
            showBottomDialog();
        });


        folderUserImg = findViewById(R.id.folderUserImg);
        Picasso.get().load(user.getProfileImage()).into(folderUserImg);

        folderUserName = findViewById(R.id.folderUserName);
        folderUserName.setText(user.getUsername());

        folderTopicCount = findViewById(R.id.folderTopicCount);
        folderTopicCount.setText(topicList.size() + " TOPIC");

        folderTitle = findViewById(R.id.folderTitle);
        folderTitle.setText(folder.getFolderName());


        noTopicsInFolderLayout = findViewById(R.id.noTopicsInFolderLayout);
        folderDetailRecyclerView = findViewById(R.id.folderDetailRecyclerView);
        if (topicList.size() == 0) {
            folderDetailRecyclerView.setVisibility(View.GONE);
            noTopicsInFolderLayout.setVisibility(View.VISIBLE);
        } else {
            folderDetailRecyclerView.setVisibility(View.VISIBLE);
            noTopicsInFolderLayout.setVisibility(View.GONE);
        }

//        addTopicResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                ArrayList<Topic> topics = result.getData().getParcelableArrayListExtra("topics");
//                if (topics != null) {
//                    List<Topic> currentList = topicList;
//                    List<Topic> addedTopic = topics.stream().filter(topic -> !currentList.contains(topic)).collect(Collectors.toList());
//                    List<Topic> removedTopic = currentList.stream().filter(topic -> !topics.contains(topic)).collect(Collectors.toList());
//
//                    folderViewModel.updateTopicForFolder(apiService, getCurrentFocus(), FolderDetailActivity.this, addedTopic, removedTopic, folder, sharedPreferences);
//                    runOnUiThread(() -> {
//                        topicList = new ArrayList<>(topics);
//                        if (topics.isEmpty()) {
//                            folderTopicCount.setText("0 Topic");
//                            folderDetailRecyclerView.setVisibility(View.GONE);
//                            noTopicsInFolderLayout.setVisibility(View.VISIBLE);
//                        } else {
//                            folderTopicCount.setText(topics.size() + " Topics");
//                            folderDetailRecyclerView.setVisibility(View.VISIBLE);
//                            noTopicsInFolderLayout.setVisibility(View.GONE);
//                            adapter = new TopicAdapter(FolderDetailActivity.this, topics, R.layout.topic_library_item, FolderDetailActivity.this);
//                            folderDetailRecyclerView.setHasFixedSize(true);
//                            folderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(FolderDetailActivity.this, LinearLayoutManager.VERTICAL, false));
//                            folderDetailRecyclerView.setAdapter(adapter);
//                        }
//                    });
//                }
//            }
//        });
        ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                searchFolder();
            }
        });
        addTopicToFolderBtn = findViewById(R.id.addTopicToFolderBtn);
        addTopicToFolderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FolderDetailActivity.this, AddTopicToFolderActivity.class);
            intent.putExtra("folder", folder);
            intent.putExtra("folderID", folder.getId());
            intent.putParcelableArrayListExtra("currentTopics", new ArrayList<>());
//            startActivity(intent);
            mGetContent.launch(intent);
        });

    }
    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.folder_detail_bottom_sheet);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

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

    private void searchFolder(){
        Call<TopicFromFolderResponse> call = apiService.selectTopicFolder(folder.getId());
        call.enqueue(new Callback<TopicFromFolderResponse>() {
            @Override
            public void onResponse(Call<TopicFromFolderResponse> call, Response<TopicFromFolderResponse> response) {
                topicList = response.body().getData();
                adapter = new TopicAdapter(FolderDetailActivity.this, topicList, R.layout.topic_library_item, FolderDetailActivity.this);
                folderDetailRecyclerView.setHasFixedSize(true);
                folderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(FolderDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                folderDetailRecyclerView.setAdapter(adapter);
                if (topicList.size() == 0) {
                    folderDetailRecyclerView.setVisibility(View.GONE);
                    noTopicsInFolderLayout.setVisibility(View.VISIBLE);
                } else {
                    folderDetailRecyclerView.setVisibility(View.VISIBLE);
                    noTopicsInFolderLayout.setVisibility(View.GONE);
                }
                Log.d("Load Folder", topicList.size() + "");
            }

            @Override
            public void onFailure(Call<TopicFromFolderResponse> call, Throwable t) {
                Log.d("Load Folder", "Tach");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

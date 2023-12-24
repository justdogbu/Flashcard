package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.flashcard.adapter.VocabularyAdapter;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.topic.TopicDetailResponse;
import com.example.flashcard.model.topic.TopicResponse;
import com.example.flashcard.model.user.User;
import com.example.flashcard.model.vocabulary.VocabuResponse;
import com.example.flashcard.model.vocabulary.Vocabulary;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTopicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private User user;
    private Topic topic;
    private ImageView saveBtn;
    private EditText edtTitleName;
    private EditText edtDescription;
    private VocabularyAdapter adapter;
    private SwitchMaterial publicTopicSwitch;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        sharedPreferences = this.getSharedPreferences(Constant.SHARE_PREF, MODE_PRIVATE);
        String userDataJson = sharedPreferences.getString(Constant.USER_DATA, null);
        user = new Gson().fromJson(userDataJson, User.class);


        Toolbar toolbar = findViewById(R.id.toolbar);
        saveBtn = findViewById(R.id.topic_check);

        edtTitleName = findViewById(R.id.edtTitleName);

        recyclerView = findViewById(R.id.vocabularyListView);
        edtDescription = findViewById(R.id.topicDescriptionEdt);
        publicTopicSwitch = findViewById(R.id.publicTopicSwitch);
        progressBar = findViewById(R.id.progressLoading);
        fab = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new VocabularyAdapter();
        recyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new ItemDecoration(this, spacingInPixels));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addFlashcard();
                Log.d("Create topic", adapter.getItemCount() + "");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String title = edtTitleName.getText().toString();
                String desc = edtDescription.getText().toString();
                int isPublic = publicTopicSwitch.isChecked() ? 1 : 0;
                int itemCount = adapter.getItemCount();

                if(title.isEmpty() || desc.isEmpty() || itemCount < 1){
                    progressBar.setVisibility(View.GONE);
                    Utils.showDialog(Gravity.CENTER, "Please fill all your information", CreateTopicActivity.this );
                }
                else{
                    for(Vocabulary vocab : adapter.vocabs){
                        if(vocab.getVocabulary().isEmpty() || vocab.getMeaning().isEmpty()){
                            Log.d("Create topic activity", vocab.getVocabulary() + " and " + vocab.getMeaning());
                            progressBar.setVisibility(View.GONE);
                            Utils.showDialog(Gravity.CENTER, "Please fill all your vocabularies and meanings", CreateTopicActivity.this );
                            return;
                        }
                    }
                    CreateTopic(title, desc, isPublic, user.getId());
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void CreateTopic(String topicName, String description, int isPublic, int ownerID){
        ApiService apiService = ApiClient.getClient();
        Call<TopicResponse> call = apiService.CreateTopic(topicName, description, isPublic, ownerID);
        call.enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                if (response.isSuccessful()) {
                    TopicResponse topicResponse = response.body();
                    if (topicResponse != null && "OK".equals(topicResponse.getStatus())) {
                        Topic newTopic = topicResponse.getData();
                        for(Vocabulary vocabulary : adapter.vocabs){
                            CreateVocabulary(vocabulary.getVocabulary(), vocabulary.getMeaning(), newTopic.getId());
                        }
                        Utils.showDialog(Gravity.CENTER, "Topic created", CreateTopicActivity.this );
                        clearTopic();
                    } else {
                        Log.d("test create", topicResponse.toString() + " " + topicResponse.getStatus() + " " + topicResponse.getMessage());

                        progressBar.setVisibility(View.GONE);
                        Utils.showDialog(Gravity.CENTER, "The topic already exist", CreateTopicActivity.this );
                    }
                } else {
                    Toast.makeText(CreateTopicActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Utils.showDialog(Gravity.CENTER, "Something went wrong! Please try again!", CreateTopicActivity.this );
                    Log.e("CreateTopicActivity", "API call failed at Create topic. Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {
                Toast.makeText(CreateTopicActivity.this, "Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Utils.showDialog(Gravity.CENTER, "Something went wrong! Please try again!", CreateTopicActivity.this );
                Log.e("CreateTopicActivity", "API call failed at Create topic. Error: " + t);
            }
        });
    }

    private  void CreateTopicDetail( int topicID, int userID){
        ApiService apiService = ApiClient.getClient();
        Call<TopicDetailResponse> call = apiService.insertTopicDetail(topicID, userID);

        call.enqueue(new Callback<TopicDetailResponse>() {
            @Override
            public void onResponse(Call<TopicDetailResponse> call, Response<TopicDetailResponse> response) {

            }

            @Override
            public void onFailure(Call<TopicDetailResponse> call, Throwable t) {

            }
        });
    }

    private  void CreateVocabulary(String vocabulary, String meaning, int topicID){
        ApiService apiService = ApiClient.getClient();
        Call<VocabuResponse> call = apiService.createVocabulary(vocabulary, meaning, topicID);

        call.enqueue(new Callback<VocabuResponse>() {
            @Override
            public void onResponse(Call<VocabuResponse> call, Response<VocabuResponse> response) {
                if (response.isSuccessful()) {
                    VocabuResponse vocabuResponse = response.body();
                    if (vocabuResponse != null && "OK".equals(vocabuResponse.getStatus())) {
                        Log.d("CreateTopicActivity", "Create success");
                    } else {
                        Toast.makeText(CreateTopicActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Utils.showDialog(Gravity.CENTER, "Topic does not exist", CreateTopicActivity.this );
                        Log.e("CreateTopicActivity", "API call failed at create vocabulary. Error: " + response.message());
                    }
                } else {
                    Toast.makeText(CreateTopicActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Utils.showDialog(Gravity.CENTER, "Something went wrong! Please try again!", CreateTopicActivity.this );
                    Log.e("CreateTopicActivity", "API call failed at create vocabulary. Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VocabuResponse> call, Throwable t) {
                Toast.makeText(CreateTopicActivity.this, "Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Utils.showDialog(Gravity.CENTER, "Something went wrong! Please try again!", CreateTopicActivity.this );
                Log.e("CreateTopicActivity", "API call failed. Error: " + t);
            }
        });
    }
    private void clearTopic(){
        edtTitleName.setText("");
        edtDescription.setText("");
        adapter.vocabs.clear();
        adapter.notifyDataSetChanged();
    }
}
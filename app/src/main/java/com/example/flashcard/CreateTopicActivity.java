package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.adapter.VocabularyAdapter;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.topic.TopicDetailResponse;
import com.example.flashcard.model.topic.TopicResponse;
import com.example.flashcard.model.user.UpdateResponse;
import com.example.flashcard.model.user.User;
import com.example.flashcard.model.vocabulary.VocabuResponse;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        getUser();

        EditText edtTitleName = findViewById(R.id.edtTitleName);
        EditText topicDescriptionEdt = findViewById(R.id.topicDescriptionEdt);
        SwitchMaterial publicTopicSwitch = findViewById(R.id.publicTopicSwitch);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView backButton = findViewById(R.id.backButton);
        TextView titleText = findViewById(R.id.titleText);
        ImageView topicSetting = findViewById(R.id.topic_setting);
        ImageView menuItem2 = findViewById(R.id.topic_check);
        saveBtn = findViewById(R.id.topic_check);

        recyclerView = findViewById(R.id.vocabularyListView);
        fab = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        VocabularyAdapter adapter = new VocabularyAdapter();
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                int swichPublic = 0;
                if(publicTopicSwitch.isChecked() == true){
                    swichPublic =1;
                }
                CreateTopic(edtTitleName.getText().toString(), topicDescriptionEdt.getText().toString(), swichPublic, user.getId());
                CreateTopicDetail(topic.getId(), user.getId());

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < adapter.getItemCount(); i++){
                    Log.d("Test new item", adapter.vocabs.get(i).getVocabulary() + " and " + adapter.vocabs.get(i).getMeaning());
                }
//                for(int i = 0; i < adapter.getItemCount() - 1; i++){
//                    Log.d("Create topic", adapter.topics.get(i).first + " + " + adapter.topics.get(i).second );
//                }
            }
        });
    }

    private  void CreateTopic(String topicName, String description, int isPublic, int ownerID){
        ApiService apiService = ApiClient.getClient();
        Call<TopicResponse> call = apiService.CreateTopic(topicName, description, isPublic, ownerID);
        call.enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                if (response.isSuccessful()) {
                    TopicResponse topicResponse = response.body();
                    if (topicResponse != null && "OK".equals(topicResponse.getStatus())) {
                        Topic newTopic = topicResponse.getData();
                        topic = newTopic;
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {

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

    private  void CreateVocabu(String vocabulary, String meaning, String topicID){
        ApiService apiService = ApiClient.getClient();
        Call<VocabuResponse> call = apiService.createVocabulary(vocabulary, meaning, topicID);

        call.enqueue(new Callback<VocabuResponse>() {
            @Override
            public void onResponse(Call<VocabuResponse> call, Response<VocabuResponse> response) {
                if (response.isSuccessful()) {
                    VocabuResponse vocabuResponse = response.body();
                    if (vocabuResponse != null && "OK".equals(vocabuResponse.getStatus())) {


                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<VocabuResponse> call, Throwable t) {

            }
        });
    }
    private void getUser() {
        user = getIntent().getParcelableExtra(Constant.USER_DATA);
        if (user == null) {
            finish();
        }
    }
}
package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

public class AddTopicToFolderActivity extends AppCompatActivity {

    private ImageButton returnBtn;
    private ImageButton acceptTopicBtn;
    private RecyclerView chosenTopicRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic_to_folder);

        returnBtn = findViewById(R.id.returnBtn);
        acceptTopicBtn = findViewById(R.id.acceptTopicBtn);
        chosenTopicRecyclerView = findViewById(R.id.chosenTopicRecyclerView);
    }
}
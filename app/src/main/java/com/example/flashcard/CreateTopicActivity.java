package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.adservices.topics.Topic;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateTopicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView backButton = findViewById(R.id.backButton);
        TextView titleText = findViewById(R.id.titleText);
        ImageView menuItem1 = findViewById(R.id.topic_setting);
        ImageView menuItem2 = findViewById(R.id.topic_check);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TopicAdapter adapter = new TopicAdapter();
        recyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing); // Định nghĩa khoảng cách trong resources
        recyclerView.addItemDecoration(new ItemDecoration(this, spacingInPixels));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addFlashcard();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.backBtn:
                        finish();
                    default:
                        return false;
                }
            }
        });
    }


}
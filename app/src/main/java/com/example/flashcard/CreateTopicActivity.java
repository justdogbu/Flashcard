package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.adapter.VocabularyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateTopicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private ImageView saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

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


}
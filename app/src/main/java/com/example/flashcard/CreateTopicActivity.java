package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.adapter.VocabularyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateTopicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    //private ArrayList<Voca>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView backButton = findViewById(R.id.backButton);
        TextView titleText = findViewById(R.id.titleText);
        ImageView topicSetting = findViewById(R.id.topic_setting);
        ImageView menuItem2 = findViewById(R.id.topic_check);


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
                        break;
                    case R.id.topic_setting:
                        PopupMenu popupMenu = new PopupMenu(CreateTopicActivity.this, topicSetting);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.addVocabularyItem:
                                        Log.d("Test","add vocab");
//                                        vocabularyList.add(vocabularyList.size(),
//                                                new Vocabulary(null, "", "", "", "", null, new ArrayList<>(), new ArrayList<>()));
//                                        vocabularyAdapter.notifyItemInserted(vocabularyList.size() - 1);
//                                        binding.vocabularyListView.scrollToPosition(vocabularyList.size() - 1);
                                        return true;
                                    case R.id.removeAllVocabularyItem:
                                        Log.d("Test", "remove all");
//                                        vocabularyList.clear();
//                                        vocabularyAdapter.notifyDataSetChanged();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popupMenu.inflate(R.menu.add_topic_popup);
                        popupMenu.show();

                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
    }


}
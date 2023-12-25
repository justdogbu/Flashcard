package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.flashcard.databinding.ActivityQuizBinding;
import com.example.flashcard.databinding.ActivityTypingBinding;

public class TypingActivity extends AppCompatActivity {
    private ActivityTypingBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityTypingBinding.inflate(getLayoutInflater());
        bind.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
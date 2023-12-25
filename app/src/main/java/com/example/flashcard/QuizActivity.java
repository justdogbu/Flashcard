package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.flashcard.databinding.ActivityQuizBinding;
import com.example.flashcard.model.quiz.Quiz;
import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.model.vocabulary.Vocabulary;
import com.example.flashcard.repository.ApiClient;
import com.example.flashcard.repository.ApiService;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity{
    private ActivityQuizBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
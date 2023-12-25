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
import com.example.flashcard.viewmodel.StudyViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private ActivityQuizBinding binding;
    private Topic topic;
    private int questionCount = 1;
    private int totalQuestions = 0;
    private List<Vocabulary> vocabulariesList;
    private boolean shuffled = false;
    private boolean answerByDefinition = false;
    private boolean answerByVocabulary = false;
    private boolean questionByDefinition = false;
    private boolean questionByVocabulary = false;
    private Constant.Language studyLanguage = Constant.Language.ENGLISH;
    private TextToSpeech ttsVietnamese;
    private TextToSpeech ttsEnglish;
    private List<Quiz> quizzesList;
    private List<Boolean> answersCorrectness;
    private List<String> chosenAnswers;
    private List<Vocabulary> bookmarkedVocabularies;
    private boolean instantFeedback = false;
    private boolean isClickable = true;
    private Constant.StudyMode studyMode;
    private boolean currentAnswerMode = false;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private StudyViewModel studyViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        ttsEnglish = new TextToSpeech(this, this);
        ttsVietnamese = new TextToSpeech(this, this);
        studyViewModel = new ViewModelProvider(this).get(StudyViewModel.class);
        apiService = ApiClient.getClient();
        sharedPreferences = getSharedPreferences(Constant.SHARE_PREF, MODE_PRIVATE);
        studyLanguage = (Constant.Language) getIntent().getSerializableExtra("studyLanguage");
        answerByDefinition = getIntent().getBooleanExtra("answerByDefinition", false);
        answerByVocabulary = getIntent().getBooleanExtra("answerByVocabulary", false);
        questionByDefinition = getIntent().getBooleanExtra("questionByDefinition", false);
        questionByVocabulary = getIntent().getBooleanExtra("questionByVocabulary", false);
        vocabulariesList = getIntent().getParcelableArrayListExtra("vocabularies");
        totalQuestions = getIntent().getIntExtra("questionCount", 0);
        shuffled = getIntent().getBooleanExtra("shuffleQuestion", false);
        topic = getIntent().getParcelableExtra("topic");
        instantFeedback = getIntent().getBooleanExtra("instantFeedBack", false);
        studyMode = (Constant.StudyMode) getIntent().getSerializableExtra("studyMode");
        bookmarkedVocabularies = getIntent().getParcelableArrayListExtra("bookmarkedVocabularies");
        quizzesList = Utils.generateQuizzes(vocabulariesList, shuffled).subList(0, totalQuestions);
        answersCorrectness = new ArrayList<>(Collections.nCopies(quizzesList.size(), false));
        chosenAnswers = new ArrayList<>(Collections.nCopies(quizzesList.size(), ""));

        initView();
        showQuestion();
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ttsEnglish.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                isClickable = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.questionTxt.setTextColor(getColor(R.color.white));
                    }
                });
            }
        });

        ttsVietnamese.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                isClickable = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.questionTxt.setTextColor(getColor(R.color.white));
                    }
                });
            }
        });

        setContentView(binding.getRoot());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = ttsEnglish.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                Utils.showDialog(Gravity.CENTER, "THIS LANGUAGE IS NOT SUPPORTED", this);
            }
            int res = ttsVietnamese.setLanguage(new Locale("vi"));
            if (res == TextToSpeech.LANG_MISSING_DATA) {
                Utils.showDialog(Gravity.CENTER, "THIS LANGUAGE IS NOT SUPPORTED", this);
            }
        } else {
            Utils.showDialog(Gravity.CENTER, "FAILED", this);
        }
    }

    private void initView() {
        studyViewModel.startTimer();
        if (vocabulariesList.size() == 2) {
            binding.answer4Btn.setVisibility(View.GONE);
            binding.answer3Btn.setVisibility(View.GONE);
        } else if (vocabulariesList.size() == 3) {
            binding.answer4Btn.setVisibility(View.GONE);
        } else if (vocabulariesList.size() == 1) {
            binding.answer4Btn.setVisibility(View.GONE);
            binding.answer3Btn.setVisibility(View.GONE);
            binding.answer2Btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsEnglish.stop();
        ttsEnglish.shutdown();
        ttsVietnamese.stop();
        ttsVietnamese.shutdown();
    }

    private void showQuestion() {
        Quiz quiz = quizzesList.get(questionCount - 1);
        binding.quizProgressTxt.setText(questionCount + "/" + totalQuestions);
        List<Vocabulary> allAnswers;
        if (vocabulariesList.size() >= 4) {
            allAnswers = Arrays.asList(
                    quiz.getCorrectAnswer(),
                    quiz.getWrongAnswer().get(0),
                    quiz.getWrongAnswer().get(1),
                    quiz.getWrongAnswer().get(2)
            );

        }
        else if (vocabulariesList.size() == 3) {
        allAnswers = Arrays.asList(
                quiz.getCorrectAnswer(),
                quiz.getWrongAnswer().get(0),
                quiz.getWrongAnswer().get(1)
            );
        }
        else if (vocabulariesList.size() == 2) {
            allAnswers = Arrays.asList(
                    quiz.getCorrectAnswer(),
                    quiz.getWrongAnswer().get(0)
            );
        } else {
            allAnswers = Collections.singletonList(quiz.getCorrectAnswer());
        }

        List<Vocabulary> shuffledAnswers = new ArrayList<>(allAnswers);
        Collections.shuffle(shuffledAnswers);

        if (studyLanguage == Constant.Language.ENGLISH) {
            if (questionByVocabulary && questionByDefinition) {
                if (new Random().nextBoolean()) {
                    currentAnswerMode = true;
                    binding.questionTxt.setText(quiz.getCorrectAnswer().getVocabulary());
                    binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isClickable) {
                                isClickable = false;
                                binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                                ttsEnglish.speak(quiz.getCorrectAnswer().getVocabulary(), TextToSpeech.QUEUE_FLUSH, null, "");
                            }
                        }
                    });
                    binding.answer1Btn.setText(shuffledAnswers.get(0).getMeaning());
                    binding.answer2Btn.setText(shuffledAnswers.get(1).getMeaning());
                    if (vocabulariesList.size() >= 3) {
                        binding.answer3Btn.setText(shuffledAnswers.get(2).getMeaning());
                    }
                    if (vocabulariesList.size() >= 4) {
                        binding.answer4Btn.setText(shuffledAnswers.get(3).getMeaning());
                    }
                } else {
                    currentAnswerMode = false;
                    binding.questionTxt.setText(quiz.getCorrectAnswer().getVocabulary());
                    binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isClickable) {
                                isClickable = false;
                                binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                                ttsEnglish.speak(quiz.getCorrectAnswer().getVocabulary(), TextToSpeech.QUEUE_FLUSH, null, "");
                            }
                        }
                    });
                    binding.answer1Btn.setText(shuffledAnswers.get(0).getMeaning());
                    binding.answer2Btn.setText(shuffledAnswers.get(1).getMeaning());
                    if (vocabulariesList.size() >= 3) {
                        binding.answer3Btn.setText(shuffledAnswers.get(2).getMeaning());
                    }
                    if (vocabulariesList.size() >= 4) {
                        binding.answer4Btn.setText(shuffledAnswers.get(3).getMeaning());
                    }
                }
            } else if (questionByDefinition) {
                binding.questionTxt.setText(quiz.getCorrectAnswer().getVocabulary());
                binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isClickable) {
                            isClickable = false;
                            binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                            ttsEnglish.speak(quiz.getCorrectAnswer().getVocabulary(), TextToSpeech.QUEUE_FLUSH, null, "");
                        }
                    }
                });
                binding.answer1Btn.setText(shuffledAnswers.get(0).getMeaning());
                binding.answer2Btn.setText(shuffledAnswers.get(1).getMeaning());
                if (vocabulariesList.size() >= 3) {
                    binding.answer3Btn.setText(shuffledAnswers.get(2).getMeaning());
                }
                if (vocabulariesList.size() >= 4) {
                    binding.answer4Btn.setText(shuffledAnswers.get(3).getMeaning());
                }
            } else if (questionByVocabulary) {
                binding.questionTxt.setText(quiz.getCorrectAnswer().getVocabulary());
                binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isClickable) {
                            isClickable = false;
                            binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                            ttsEnglish.speak(quiz.getCorrectAnswer().getVocabulary(), TextToSpeech.QUEUE_FLUSH, null, "");
                        }
                    }
                });
                binding.answer1Btn.setText(shuffledAnswers.get(0).getMeaning());
                binding.answer2Btn.setText(shuffledAnswers.get(1).getMeaning());
                if (vocabulariesList.size() >= 3) {
                    binding.answer3Btn.setText(shuffledAnswers.get(2).getMeaning());
                }
                if (vocabulariesList.size() >= 4) {
                    binding.answer4Btn.setText(shuffledAnswers.get(3).getMeaning());
                }
            }
        } else {
            if (questionByDefinition && questionByVocabulary) {
                if (new Random().nextBoolean()) {
                    currentAnswerMode = true;
                    binding.questionTxt.setText(quiz.getCorrectAnswer().getMeaning());
                    binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isClickable) {
                                isClickable = false;
                                binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                                ttsVietnamese.speak(quiz.getCorrectAnswer().getMeaning(), TextToSpeech.QUEUE_FLUSH, null, "");
                            }
                        }
                    });
                    binding.answer1Btn.setText(shuffledAnswers.get(0).getVocabulary());
                    binding.answer2Btn.setText(shuffledAnswers.get(1).getVocabulary());
                    if (vocabulariesList.size() >= 3) {
                        binding.answer3Btn.setText(shuffledAnswers.get(2).getVocabulary());
                    }
                    if (vocabulariesList.size() >= 4) {
                        binding.answer4Btn.setText(shuffledAnswers.get(3).getVocabulary());
                    }
                } else {
                    currentAnswerMode = false;
                    binding.questionTxt.setText(quiz.getCorrectAnswer().getMeaning());
                    binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isClickable) {
                                isClickable = false;
                                binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                                ttsVietnamese.speak(quiz.getCorrectAnswer().getMeaning(), TextToSpeech.QUEUE_FLUSH, null, "");
                            }
                        }
                    });
                    binding.answer1Btn.setText(shuffledAnswers.get(0).getVocabulary());
                    binding.answer2Btn.setText(shuffledAnswers.get(1).getVocabulary());
                    if (vocabulariesList.size() >= 3) {
                        binding.answer3Btn.setText(shuffledAnswers.get(2).getVocabulary());
                    }
                    if (vocabulariesList.size() >= 4) {
                        binding.answer4Btn.setText(shuffledAnswers.get(3).getVocabulary());
                    }
                }
            } else if (questionByDefinition) {
                binding.questionTxt.setText(quiz.getCorrectAnswer().getMeaning());
                binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isClickable) {
                            isClickable = false;
                            binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                            ttsVietnamese.speak(quiz.getCorrectAnswer().getMeaning(), TextToSpeech.QUEUE_FLUSH, null, "");
                        }
                    }
                });
                binding.answer1Btn.setText(shuffledAnswers.get(0).getVocabulary());
                binding.answer2Btn.setText(shuffledAnswers.get(1).getVocabulary());
                if (vocabulariesList.size() >= 3) {
                    binding.answer3Btn.setText(shuffledAnswers.get(2).getVocabulary());
                }
                if (vocabulariesList.size() >= 4) {
                    binding.answer4Btn.setText(shuffledAnswers.get(3).getVocabulary());
                }
            } else if (questionByVocabulary) {
                binding.questionTxt.setText(quiz.getCorrectAnswer().getMeaning());
                binding.questionTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isClickable) {
                            isClickable = false;
                            binding.questionTxt.setTextColor(getColor(R.color.secondary_color));
                            ttsVietnamese.speak(quiz.getCorrectAnswer().getMeaning(), TextToSpeech.QUEUE_FLUSH, null, "");
                        }
                    }
                });
                binding.answer1Btn.setText(shuffledAnswers.get(0).getVocabulary());
                binding.answer2Btn.setText(shuffledAnswers.get(1).getVocabulary());
                if (vocabulariesList.size() >= 3) {
                    binding.answer3Btn.setText(shuffledAnswers.get(2).getVocabulary());
                }
                if (vocabulariesList.size() >= 4) {
                    binding.answer4Btn.setText(shuffledAnswers.get(3).getVocabulary());
                }
            }
        }
        List<Button> btnList = Arrays.asList(
                binding.answer1Btn,
                binding.answer2Btn,
                binding.answer3Btn,
                binding.answer4Btn
        );

        for (Button btn : btnList) {
            btn.setEnabled(true);
            btn.setClickable(true);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vocabulary correct = shuffledAnswers.get(shuffledAnswers.indexOf(quiz.getCorrectAnswer()));
                    chosenAnswers.set(questionCount - 1, btn.getText().toString());

                    if (instantFeedback) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Button btn : btnList) {
                                    btn.setEnabled(false);
                                    btn.setClickable(false);
                                }

                                if (studyLanguage == Constant.Language.ENGLISH) {
                                    if (questionByDefinition && questionByVocabulary) {
                                        if (currentAnswerMode) {
                                            Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getMeaning() : "", btn.getText().toString());
                                        } else {
                                            Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getMeaning() : "", btn.getText().toString());
                                        }
                                    } else if (questionByDefinition) {
                                        Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getMeaning() : "", btn.getText().toString());
                                    } else if (questionByVocabulary) {
                                        Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getMeaning() : "", btn.getText().toString());
                                    }
                                } else {
                                    if (questionByDefinition && questionByVocabulary) {
                                        if (currentAnswerMode) {
                                            Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getVocabulary() : "", btn.getText().toString());
                                        } else {
                                            Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getVocabulary() : "", btn.getText().toString());
                                        }
                                    } else if (questionByDefinition) {
                                        Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getVocabulary() : "", btn.getText().toString());
                                    } else if (questionByVocabulary) {
                                        Utils.propWrongAnswer(getApplicationContext(), correct != null ? correct.getVocabulary() : "", btn.getText().toString());
                                    }
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                questionCount++;
                                                showQuestion();
                                            }
                                        });
                                    }
                                }, 1000);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                questionCount++;
                                                showQuestion();
                                            }
                                        });
                                    }
                                }, 0);
                            }
                        });
                    }
                }
            });
        }

        btnList.get(shuffledAnswers.indexOf(quiz.getCorrectAnswer())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAnswers.set(questionCount - 1, btnList.get(shuffledAnswers.indexOf(quiz.getCorrectAnswer())).getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        answersCorrectness.set(questionCount - 1, true);
                        if (instantFeedback) {
                            Utils.propCorrectAnswer(getApplicationContext());
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        questionCount++;
                                        showQuestion();
                                    }
                                });
                            }
                        }, instantFeedback ? 1000 : 0);
                    }
                });
            }
        });
    }
}
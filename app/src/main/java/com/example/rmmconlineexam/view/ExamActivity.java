package com.example.rmmconlineexam.view;

import static android.Manifest.permission_group.CAMERA;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rmmconlineexam.MainActivity;
import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.adapter.QuestionAdapter;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.Question;
import com.example.rmmconlineexam.model.StudentAnswer;
import com.example.rmmconlineexam.util.CorrectAnswerCallBack;
import com.example.rmmconlineexam.util.Messenger;
import com.example.rmmconlineexam.util.StoreAnswerCallback;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ExamActivity extends AppCompatActivity implements PostCallback, StoreAnswerCallback {

    private RecyclerView recyclerView;
    private TextView tvTimer;
    private CountDownTimer countDownTimer, backgroundTimer;
    private QuestionAdapter questionAdapter;
    private List<Question> questions = new ArrayList<>();
    private MaterialButton btnSubmit;
    private TextView tvWarningMessage;
    private List<StudentAnswer> studentAnswers = new ArrayList<>();
    private static final long TOTAL_TIME = 30 * 60 * 1000;
    private static final int CAMERA_REQUEST_CODE = 100;

    private int homePressCount = 0;
    private boolean isInBackground = false;
    private static final long MAX_BACKGROUND_TIME = 10 * 1000; // 10 seconds

    private static final int MAX_HOME_PRESSES = 3;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "exam_prefs";
    private static final String KEY_HOME_PRESS_COUNT = "home_press_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam);

        recyclerView = findViewById(R.id.recyclerView);
        tvWarningMessage = findViewById(R.id.tvWarningMessage);
        setupRecyclerView();

        tvTimer = findViewById(R.id.tvTimer);
        btnSubmit = findViewById(R.id.btnSave);

        btnSubmit.setOnClickListener(this::onSubmit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        homePressCount = sharedPreferences.getInt(KEY_HOME_PRESS_COUNT, 0);

        handler = new Handler();


        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInBackground = true;

        // Start the background timer
        backgroundTimer = new CountDownTimer(MAX_BACKGROUND_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Timer running while the app is in the background
            }

            @Override
            public void onFinish() {
                // End the exam if 10 seconds have passed in the background
                if (isInBackground) {
                    stopExam("You have been inactive for too long!");

                }
            }
        };

        backgroundTimer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInBackground = false;

        // Cancel the background timer if the user returns within 10 seconds
        if (backgroundTimer != null) {
            backgroundTimer.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        homePressCount++;

        // Stop the exam if the home button is pressed 3 times
        if (homePressCount >= 3) {
            stopExam("You have exceeded the maximum number of exits!");
        }
    }

    private void stopExam(String message) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (backgroundTimer != null) {
            backgroundTimer.cancel();
        }
        tvWarningMessage.setText(message);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        finish();
    }

    private void onSubmit(View view) {
        boolean hasUnansweredQuestions = false;
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (studentAnswer.getAnswer() == null || studentAnswer.getAnswer().isEmpty()) {
                hasUnansweredQuestions = true;
                break;
            }
        }

        if (hasUnansweredQuestions) {
            Messenger.showAlertDialog(this, "Unanswered Questions", "You have unanswered questions. Are you sure you want to submit?", "Ok").show();
        } else {
            submitExam();
            countDownTimer.cancel();
        }
    }

    private void submitExam() {
        try {
            JSONObject postData = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (StudentAnswer studentAnswer : studentAnswers) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("question_id", studentAnswer.getQuestionId());
                jsonObject.put("answer", studentAnswer.getAnswer());
                jsonArray.put(jsonObject);
            }
            postData.put("answers", jsonArray);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    Messenger.showAlertDialog(ExamActivity.this, "Success", "Exam submitted successfully", "Ok", "Back",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(ExamActivity.this, MainActivity.class));
                                    finish();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(ExamActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).show();
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(ExamActivity.this, "Error", errorMessage, "Ok").show();
                }
            }, "Error", "saveAnswer").execute(postData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvTimer.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                stopExam("Time's up! The exam has ended.");
            }
        };

        // Start the timer
        countDownTimer.start();
    }

    private void setupRecyclerView() {
        try {
            JSONObject postData = new JSONObject();
            new PostTask(this, this, "Fetching exam...", "fetchQuestion").execute(postData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        // Increment Home button press count
        homePressCount++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HOME_PRESS_COUNT, homePressCount);
        editor.apply();

        // Show warning message
        tvWarningMessage.setVisibility(View.VISIBLE);
        tvWarningMessage.setText(String.format(Locale.getDefault(), "Warning: Home button pressed %d times!", homePressCount));


        // Check if Home button press limit is exceeded
        if (homePressCount >= MAX_HOME_PRESSES) {
            Toast.makeText(this, "Exam terminated due to excessive Home button presses.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPostSuccess(String responseData) {
        questions.addAll(new Gson().fromJson(responseData, new TypeToken<List<Question>>() {}.getType()));

        questionAdapter = new QuestionAdapter(this, questions, this);
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Question question : questions) {
            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setCorrectAnswer(question.getCorrectAnswer());
            studentAnswer.setQuestionId(question.getId());
            studentAnswers.add(studentAnswer);
        }
    }

    @Override
    public void onPostError(String errorMessage) {
        // Handle error
    }

    @Override
    public void onStoreAnswerCallback(String answer, int questionId) {
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (studentAnswer.getQuestionId() == questionId) {
                studentAnswer.setAnswer(answer);
            }
        }
        Log.d("studentAnswers", answer);
    }
}

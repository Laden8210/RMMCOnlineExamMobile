package com.example.rmmconlineexam.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.rmmconlineexam.MainActivity;
import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.Answer;
import com.example.rmmconlineexam.model.User;
import com.example.rmmconlineexam.util.Messenger;
import com.example.rmmconlineexam.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class StudentHeroActivity extends AppCompatActivity implements PostCallback {

    private CardView startExamCard, viewResultCard, logoutCard, guidelinesCard;
    private ImageView startExamIcon, viewResultIcon, logoutIcon, guidelinesIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_hero);

        startExamCard = findViewById(R.id.startExamCard);
        viewResultCard = findViewById(R.id.viewResultsCard);
        logoutCard = findViewById(R.id.logoutCard);
        guidelinesCard = findViewById(R.id.guidelinesCard);
        

        startExamCard.setOnClickListener(this::startExamAction);
        
        viewResultCard.setOnClickListener(this::viewResultAction);
        
        logoutCard.setOnClickListener(this::logoutAction);
        
        guidelinesCard.setOnClickListener(this::guidelinesAction);

        startExamIcon = findViewById(R.id.startExamIcon);
        viewResultIcon = findViewById(R.id.viewResultsIcon);
        logoutIcon = findViewById(R.id.logoutIcon);
        guidelinesIcon = findViewById(R.id.guidelinesIcon);

        loadImage(startExamIcon, R.drawable.exam);
        loadImage(viewResultIcon, R.drawable.result);
        loadImage(logoutIcon, R.drawable.logout);
        loadImage(guidelinesIcon, R.drawable.guidelines);

        
    }

    private void loadImage(ImageView imageView, int drawableId) {
        Glide.with(this)
                .load(drawableId)
                .into(imageView);
    }

    private void guidelinesAction(View view) {
        Intent intent = new Intent(this, GuidelinesActivity.class);
        startActivity(intent);
    }

    private void logoutAction(View view) {

        Messenger.showAlertDialog(this, "Logout", "Are you sure you want to logout?", "Yes", "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SessionManager.getInstance(getApplicationContext()).clear();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void viewResultAction(View view) {
        try {
            JSONObject jsonObject = new JSONObject();

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    Gson gson = new Gson();
                    Type answerListType = new TypeToken<List<Answer>>(){}.getType();  // This defines the type of List<Answer>
                    List<Answer> answers = gson.fromJson(responseData, answerListType);


                    if (answers.size() == 0) {
                        Messenger.showAlertDialog(StudentHeroActivity.this, "Exam", "You have not taken the exam yet.", "Ok", null, null, null).show();
                        return;
                    }

                    int correct = 0;

                    for (Answer answer : answers) {
                        if (answer.getIs_correct() == 1) {
                            correct++;
                        }
                    }

                    double percentage = ((double) correct / answers.size()) * 100;

                    String status = percentage >= 60 ? "Pass" : "Fail";

                    String resultMessage = String.format("You have answered %d out of %d questions correctly.\n" +
                            "Percentage: %.2f%%\nStatus: %s", correct, answers.size(), percentage, status);

                    Messenger.showAlertDialog(StudentHeroActivity.this, "Exam Result", resultMessage, "Ok", null, null, null).show();
                }

                @Override
                public void onPostError(String errorMessage) {
                    // Handle error here (if necessary)
                }
            }, "exam/result", "getResult").execute(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startExamAction(View view) {

        try {
            JSONObject jsonObject = new JSONObject();

            new PostTask(this, this, "exam/start", "getCheckStudentExam").execute(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPostSuccess(String responseData) {
        Gson gson = new Gson();

        User user = gson.fromJson(responseData, User.class);

        if (user.getIsExamTaken() == 1){
            Messenger.showAlertDialog(this, "Exam", "You have already taken the exam.", "Ok", null, null, null).show();
            return;
        }
        startActivity(new Intent(this, ExamActivity.class));
    }

    @Override
    public void onPostError(String errorMessage) {

    }
}
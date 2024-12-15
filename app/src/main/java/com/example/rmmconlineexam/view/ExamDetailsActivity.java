package com.example.rmmconlineexam.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.adapter.ExamQuestionAdapter;

public class ExamDetailsActivity extends AppCompatActivity {

    private RecyclerView rvExamDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam_details);

        rvExamDetails = findViewById(R.id.rvExamDetails);
//        rvExamDetails.setAdapter(new ExamQuestionAdapter());
        rvExamDetails.setLayoutManager(new LinearLayoutManager(this));

    }
}
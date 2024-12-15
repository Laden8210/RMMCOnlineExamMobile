package com.example.rmmconlineexam.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.adapter.ExamAdapter;
import com.example.rmmconlineexam.adapter.ExamQuestionAdapter;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.Question;
import com.example.rmmconlineexam.view.CreateQuestionActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;


public class ExamFragment extends Fragment implements PostCallback {


    private ExtendedFloatingActionButton fabAddQuestion;
    private RecyclerView rvExam;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_exam, container, false);

        fabAddQuestion = view.findViewById(R.id.fabAddQuestion);
        fabAddQuestion.setOnClickListener(this::onAddQuestionAction);



        rvExam = view.findViewById(R.id.rvExam);
        rvExam.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            JSONObject postData = new JSONObject();
            new PostTask(getContext(), this, "Fetching exam...", "fetchQuestion").execute(postData);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void onAddQuestionAction(View view) {
        startActivity(new Intent(getContext(), CreateQuestionActivity.class));
    }

    @Override
    public void onPostSuccess(String responseData) {
        List<Question> questions = new Gson().fromJson(responseData, new TypeToken<List<Question>>(){}.getType());

        rvExam.setAdapter(new ExamQuestionAdapter(getContext(), questions));

    }

    @Override
    public void onPostError(String errorMessage) {

    }
}
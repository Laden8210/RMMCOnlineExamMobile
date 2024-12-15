package com.example.rmmconlineexam.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.adapter.StudentListAdapter;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ExamineeFragment extends Fragment implements PostCallback {

    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_examinee, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try{
              JSONObject postData = new JSONObject();
              new PostTask(getContext(), this, "Fetching examinees...", "examinee").execute(postData);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onPostSuccess(String responseData) {
        Gson gson = new Gson();
        Type studentListType = new TypeToken<List<Student>>() {}.getType();
        List<Student> students = gson.fromJson(responseData, studentListType);

        recyclerView.setAdapter(new StudentListAdapter(getContext(), students));
    }

    @Override
    public void onPostError(String errorMessage) {

    }
}
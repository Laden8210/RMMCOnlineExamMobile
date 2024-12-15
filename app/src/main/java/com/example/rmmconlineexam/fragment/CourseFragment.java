package com.example.rmmconlineexam.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.adapter.CourseAdapter;


public class CourseFragment extends Fragment {

    private RecyclerView rvCourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_course, container, false);

        rvCourse = view.findViewById(R.id.rvCourse);
        rvCourse.setAdapter(new CourseAdapter());
        rvCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}
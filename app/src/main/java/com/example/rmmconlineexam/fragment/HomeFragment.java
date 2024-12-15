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

public class HomeFragment extends Fragment {


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

        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(new StudentListAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}
package com.example.rmmconlineexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.view.ExamDetailsActivity;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyViewHolder> {

    private Context context;

    public ExamAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ExamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamAdapter.MyViewHolder holder, int position) {
        holder.cvExam.setOnClickListener(e -> {
            context.startActivity(new Intent(context, ExamDetailsActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cvExam;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvExam = itemView.findViewById(R.id.cvExam);
        }
    }
}

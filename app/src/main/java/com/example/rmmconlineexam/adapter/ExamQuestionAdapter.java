package com.example.rmmconlineexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.model.Question;

import java.util.List;

public class ExamQuestionAdapter extends RecyclerView.Adapter<ExamQuestionAdapter.MyViewHolder> {

    private Context context;
    private List<Question> questions;

    public ExamQuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }
    @NonNull
    @Override
    public ExamQuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_question, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamQuestionAdapter.MyViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.tvQuestion.setText(question.getQuestion());

        for (int i = 0; i < question.getOptions().size(); i++) {
            TextView textView = new TextView(context);
            textView.setText("• "+ question.getOptions().get(i).getOption());
            holder.llOptions.addView(textView);
        }
        String type = question.getType();
        String formattedType = type.replaceAll("_", " ");

        String[] words = formattedType.split(" ");
        StringBuilder capitalizedType = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                capitalizedType.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        formattedType = capitalizedType.toString().trim();
        holder.tvType.setText(formattedType);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQuestion, tvType;
        private LinearLayout llOptions;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            llOptions = itemView.findViewById(R.id.llOptions);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}

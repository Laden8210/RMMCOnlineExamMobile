package com.example.rmmconlineexam.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.model.Question;
import com.example.rmmconlineexam.util.StoreAnswerCallback;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    private Context context;
    private List<Question> questions;
    private StoreAnswerCallback storeAnswerCallback;

    public QuestionAdapter(Context context, List<Question> questions, StoreAnswerCallback storeAnswerCallback) {
        this.context = context;
        this.questions = questions;
        this.storeAnswerCallback = storeAnswerCallback;
    }
    @NonNull
    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.MyViewHolder holder, int position) {

        Question question = questions.get(position);
        holder.tvQuestion.setText(question.getQuestion());
        holder.rgOptions.removeAllViews();

        if (question.getType().equals("Short Answer")){
            holder.tilAnswer.setVisibility(View.VISIBLE);
            holder.rgOptions.setVisibility(View.GONE);

            holder.tilAnswer.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    storeAnswerCallback.onStoreAnswerCallback(s.toString(), question.getId());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }else{
            holder.tilAnswer.setEnabled(false);
            holder.rgOptions.setVisibility(View.VISIBLE);
            for (Question.Option option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(option.getOption());
                holder.rgOptions.addView(radioButton);

                if (holder.tilAnswer.getEditText().getText().toString().equals(option.getOption())){
                    radioButton.setChecked(true);
                }
            }

            holder.rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton radioButton = group.findViewById(checkedId);
                storeAnswerCallback.onStoreAnswerCallback(radioButton.getText().toString(), question.getId());
                holder.tilAnswer.getEditText().setText(radioButton.getText().toString());
            });
        }


    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout tilAnswer;
        private RadioGroup rgOptions;
        private TextView tvQuestion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tilAnswer = itemView.findViewById(R.id.tilAnswer);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);

        }
    }
}

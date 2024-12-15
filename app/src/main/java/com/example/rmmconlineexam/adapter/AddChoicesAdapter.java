package com.example.rmmconlineexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.util.CorrectAnswerCallBack;
import com.example.rmmconlineexam.util.EditChoicesCallback;

import java.util.ArrayList;
import java.util.List;

public class AddChoicesAdapter extends RecyclerView.Adapter<AddChoicesAdapter.MyViewHolder> {
    private Context context;
    private List<String> choices;
    private int selectedPosition = -1;

    private EditChoicesCallback editChoicesCallback;
    private CorrectAnswerCallBack correctAnswerCallBack;

    public AddChoicesAdapter(Context context, List<String> choices, EditChoicesCallback editChoicesCallback, CorrectAnswerCallBack correctAnswerCallBack) {
        this.context = context;
        this.choices = new ArrayList<>(choices);
        this.editChoicesCallback = editChoicesCallback;
        this.correctAnswerCallBack = correctAnswerCallBack;
    }

    @NonNull
    @Override
    public AddChoicesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choices, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddChoicesAdapter.MyViewHolder holder, int position) {
        String choice = choices.get(position);

        holder.etChoice.setText(choice);

        holder.radioButton.setChecked(position == selectedPosition);

        holder.cvChoice.setOnClickListener(v -> {
            editChoicesCallback.onEditChoices(position, choice);
        });

        holder.radioButton.setOnClickListener(v -> {

            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            correctAnswerCallBack.onCorrectAnswer(choice);
        });
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public void addChoices(List<String> choices) {
        this.choices.clear();
        this.choices.addAll(choices);
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView etChoice;
        private CardView cvChoice;
        RadioButton radioButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etChoice = itemView.findViewById(R.id.tvChoice);
            cvChoice = itemView.findViewById(R.id.cardView);
            radioButton = itemView.findViewById(R.id.rbChoice);
        }
    }
}

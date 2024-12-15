package com.example.rmmconlineexam.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.adapter.AddChoicesAdapter;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.util.CorrectAnswerCallBack;
import com.example.rmmconlineexam.util.EditChoicesCallback;
import com.example.rmmconlineexam.util.Messenger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateQuestionActivity extends AppCompatActivity implements EditChoicesCallback, PostCallback, CorrectAnswerCallBack {

    private MaterialButton btnAddChoices;
    private RecyclerView rgChoices;
    private Button btnSaveQuestion;
    private Spinner spQuestionType;
    private TextInputEditText etQuestion, etCorrectAnswer;
    private AddChoicesAdapter addChoicesAdapter;

    private List<String> choices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        btnAddChoices = findViewById(R.id.btnAddChoices);
        btnAddChoices.setOnClickListener(this::addChoicesAction);
        
        rgChoices = findViewById(R.id.rgChoices);
        btnSaveQuestion = findViewById(R.id.btnSaveQuestion);
        btnSaveQuestion.setOnClickListener(this::saveQuestionAction);

        spQuestionType = findViewById(R.id.spQuestionType);

        etQuestion = findViewById(R.id.etQuestion);

        rgChoices.setLayoutManager(new LinearLayoutManager(this));

        addChoicesAdapter = new AddChoicesAdapter(this, choices, this, this);

        etCorrectAnswer = findViewById(R.id.etCorrectAnswer);

        rgChoices.setAdapter(addChoicesAdapter);

        initSpinner();

        rgChoices.setVisibility(View.GONE);
        btnAddChoices.setVisibility(View.GONE);
    }


    private void initSpinner(){
        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(
                this, R.array.question_types_array, android.R.layout.simple_spinner_item
        );
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuestionType.setAdapter(courseAdapter);

        spQuestionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();

                if (selectedType.equals("Multiple Choice")) {
                    rgChoices.setVisibility(View.VISIBLE);
                    etCorrectAnswer.setVisibility(View.VISIBLE);
                    btnAddChoices.setVisibility(View.VISIBLE);
                }else if(selectedType.equals("True or False")){
                    etCorrectAnswer.setVisibility(View.VISIBLE);
                    btnAddChoices.setVisibility(View.GONE);
                    choices.clear();
                    choices.add("True");
                    choices.add("False");
                    addChoicesAdapter.addChoices(choices);
                }else {
                    rgChoices.setVisibility(View.GONE);
                    etCorrectAnswer.setVisibility(View.GONE);
                    btnAddChoices.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveQuestionAction(View view) {

        try {

            JSONObject question = new JSONObject();
            question.put("question", etQuestion.getText().toString().trim());
            question.put("type", spQuestionType.getSelectedItem().toString());

            question.put("correct_answer", etCorrectAnswer.getText().toString().trim());

            if (etQuestion.getText().toString().trim().isEmpty()) {
                Messenger.showAlertDialog(this, "Error", "Please enter a question", "Ok").show();
                return;
            }

            if (etCorrectAnswer.getText().toString().trim().isEmpty()) {
                Messenger.showAlertDialog(this, "Error", "Please enter a correct answer", "Ok").show();
                return;
            }

            if (spQuestionType.getSelectedItem().toString().equals("Multiple Choice")) {


                JSONArray choices = new JSONArray();
                for (String choice : this.choices) {
                    choices.put(choice);
                }
                question.put("choices", choices);
                if (choices.length() < 2) {
                    Messenger.showAlertDialog(this, "Error", "Please add at least 2 choices", "Ok").show();
                    return;
                }

            }else if (spQuestionType.getSelectedItem().toString().equals("True or False")) {
                JSONArray choices = new JSONArray();
                choices.put("True");
                choices.put("False");
                question.put("choices", choices);
            }

            new PostTask(this, this, "error", "saveQuestion").execute(question);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addChoicesAction(View view) {

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_choices, null);
        TextInputEditText etChoice = dialogView.findViewById(R.id.etChoice);

        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setTitle("Add Choice")
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String choice = etChoice.getText() != null ? etChoice.getText().toString().trim() : "";
                    if (!choice.isEmpty()) {
                        choices.add(choice);
                        addChoicesAdapter.addChoices(choices);
                        Log.d("CHOICES", choices.toString());
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void onEditChoices(int position, String choice) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_choices, null);
        TextInputEditText etChoice = dialogView.findViewById(R.id.etChoice);
        etChoice.setText(choice);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Edit Choice")
                .setView(dialogView)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newChoice = etChoice.getText() != null ? etChoice.getText().toString().trim() : "";
                    if (!newChoice.isEmpty()) {
                        choices.set(position, newChoice);
                        addChoicesAdapter.addChoices(choices);
                        return;
                    }
                    Messenger.showAlertDialog(this, "Error", "Please enter a choice", "Ok").show();

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setNeutralButton("Delete", (dialogInterface, i) -> {
                    choices.remove(position);
                    addChoicesAdapter.addChoices(choices);
                })
                .show();
    }

    @Override
    public void onPostSuccess(String responseData) {
        Messenger.showAlertDialog(this, "Success", "Question has been saved", "Ok").show();

    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Error", errorMessage, "Ok").show();
    }

    @Override
    public void onCorrectAnswer(String correctAnswer) {
        etCorrectAnswer.setText(correctAnswer);
    }
}
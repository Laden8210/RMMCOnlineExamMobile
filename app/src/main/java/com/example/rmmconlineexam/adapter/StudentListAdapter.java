package com.example.rmmconlineexam.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.Answer;
import com.example.rmmconlineexam.model.Student;
import com.example.rmmconlineexam.util.Loader;
import com.example.rmmconlineexam.util.Messenger;
import com.example.rmmconlineexam.view.StudentHeroActivity;
import com.example.rmmconlineexam.view.StudentResultActivity;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private Context context;
    private List<Student> studentList;

    public StudentListAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.MyViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvName.setText(student.getFirstName() + " " + student.getLastName());
        holder.tvCourse.setText("Course: "+student.getCourse());
        holder.tvBirthdate.setText("Birthdate: "+student.getBirthdate());
        holder.tvEmail.setText("Email: "+student.getEmail());
        holder.tvContact.setText("Contact: "+student.getContactNumber());
        holder.tvGender.setText("Gender: "+student.getGender());

        if (student.isActivated() == 1) {
            holder.btnConfirm.setText("View Result");
            holder.btnConfirm.setOnClickListener(e -> {
                try {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("student_id", student.getId());

                    new PostTask(context, new PostCallback() {
                        @Override
                        public void onPostSuccess(String responseData) {
                            Gson gson = new Gson();
                            Type answerListType = new TypeToken<List<Answer>>(){}.getType();  // This defines the type of List<Answer>
                            List<Answer> answers = gson.fromJson(responseData, answerListType);


                            if (answers.size() == 0) {
                                Messenger.showAlertDialog(context, "Exam", "The student have not taken the exam yet.", "Ok", null, null, null).show();
                                return;
                            }

                            int correct = 0;

                            for (Answer answer : answers) {
                                if (answer.getIs_correct() == 1) {
                                    correct++;
                                }
                            }

                            double percentage = ((double) correct / answers.size()) * 100;

                            String status = percentage >= 60 ? "Pass" : "Fail";

                            String resultMessage = String.format("The student have answered %d out of %d questions correctly.\n" +
                                    "Percentage: %.2f%%\nStatus: %s", correct, answers.size(), percentage, status);

                            Messenger.showAlertDialog(context, "Exam Result", resultMessage, "Ok", null, null, null).show();
                        }

                        @Override
                        public void onPostError(String errorMessage) {
                            // Handle error here (if necessary)
                        }
                    }, "exam/result", "getStudentResult").execute(jsonObject);
                } catch (Exception we) {
                    we.printStackTrace();
                }
            });
        }else {

            holder.btnConfirm.setOnClickListener(e -> {


                Messenger.showAlertDialog(context, "Confirm", "Are you sure you want to confirm this student?", "Yes", "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    JSONObject object = new JSONObject();
                                    object.put("student_id", student.getId());

                                    Loader loader = new Loader();
                                    loader.showLoader(context);

                                    new PostTask(context, new PostCallback() {
                                        @Override
                                        public void onPostSuccess(String responseData) {
                                            Messenger.showAlertDialog(context, "Success", "Student has been confirmed", "Ok").show();
                                            loader.dismissLoader();
                                        }

                                        @Override
                                        public void onPostError(String errorMessage) {
                                            Messenger.showAlertDialog(context, "Success", errorMessage, "Ok").show();
                                            loader.dismissLoader();
                                        }
                                    }, "Error", "confirmStudent").execute(object);
                                } catch (Exception ex) {

                                }
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            });

        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvCourse, tvBirthdate, tvGender, tvEmail, tvContact;
        private MaterialButton btnConfirm;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvStudentName);
            tvCourse = itemView.findViewById(R.id.tvCourse);
            tvBirthdate = itemView.findViewById(R.id.tvBirthDate);
            tvGender = itemView.findViewById(R.id.tvGender);

            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvContact = itemView.findViewById(R.id.tvContact);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
        }
    }
}

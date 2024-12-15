package com.example.rmmconlineexam.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.Student;
import com.example.rmmconlineexam.util.ImageUtil;
import com.example.rmmconlineexam.util.Loader;
import com.example.rmmconlineexam.util.Messenger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;

public class RegisterExamineeActivity extends AppCompatActivity implements PostCallback {


    private TextInputEditText etFirstName, etMiddleName, etLastName, etEmail, etBirthdate, etContactNumber;

    private Spinner spCourse, spGender;

    private MaterialButton btnRegister;

    private Loader loader;

    private static final int REQUEST_IMAGE_PICK = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_examinee);

        loader = new Loader();


        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etBirthdate = findViewById(R.id.etBirthdate);
        etContactNumber = findViewById(R.id.etContactNumber);
        spCourse = findViewById(R.id.spCourse);
        spGender = findViewById(R.id.spGender);
        btnRegister = findViewById(R.id.btnRegister);



        btnRegister.setOnClickListener(this::registerAction);

        initSpinner();

    }

    private void initSpinner(){
        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(
                this, R.array.courses_array, android.R.layout.simple_spinner_item
        );
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(courseAdapter);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.genders_array, android.R.layout.simple_spinner_item
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);

    }

    private void registerAction(View view) {
        try{

            Student student = new Student();

            student.setFirstName(etFirstName.getText().toString());
            student.setMiddleName(etMiddleName.getText().toString());
            student.setLastName(etLastName.getText().toString());
            student.setEmail(etEmail.getText().toString());
            student.setBirthdate(etBirthdate.getText().toString());
            student.setContactNumber(etContactNumber.getText().toString());
            student.setCourse(spCourse.getSelectedItem().toString());
            student.setGender(spGender.getSelectedItem().toString());

            if (student.validateField(this, "Student Registration")) {
                loader.showLoader(this);
                new PostTask(this, this, "Error", "register-student").execute(student.toJSONObject());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(String responseData) {
        Messenger.showAlertDialog(this, "Registration", "Registration successful", "Ok").show();
        loader.dismissLoader();

        etBirthdate.setText("");
        etContactNumber.setText("");
        etEmail.setText("");
        etFirstName.setText("");
        etLastName.setText("");
        etMiddleName.setText("");
        spCourse.setSelection(0);
        spGender.setSelection(0);

    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Error", errorMessage, "Ok").show();
        loader.dismissLoader();
    }
}
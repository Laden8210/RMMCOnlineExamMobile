package com.example.rmmconlineexam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rmmconlineexam.api.PostCallback;
import com.example.rmmconlineexam.api.PostTask;
import com.example.rmmconlineexam.model.LoginDao;
import com.example.rmmconlineexam.util.Messenger;
import com.example.rmmconlineexam.util.SessionManager;
import com.example.rmmconlineexam.view.AdminHeroActivity;
import com.example.rmmconlineexam.view.RegisterExamineeActivity;
import com.example.rmmconlineexam.view.StudentHeroActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PostCallback {

    private TextView tvRegisterExaminee;

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvRegisterExaminee = findViewById(R.id.tvRegister);
        tvRegisterExaminee.setOnClickListener(this::registerAction);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::loginAction);

        if (SessionManager.getInstance(this).getToken() != null) {

            if (SessionManager.getInstance(this).getUserType() == null) {
                SessionManager.getInstance(this).clear();
                return;
            }

            if (SessionManager.getInstance(this).getUserType().equals("student")) {
                startActivity(new Intent(this, StudentHeroActivity.class));
            } else {
                startActivity(new Intent(this, AdminHeroActivity.class));
            }

        }
    }

    private void loginAction(View view) {

        if (etUsername.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            Messenger.showAlertDialog(this, "Error", "Please fill up all fields", "Ok");
            return;
        }

        try {
            JSONObject postData = new JSONObject();
            postData.put("username", etUsername.getText().toString());
            postData.put("password", etPassword.getText().toString());

            new PostTask(this, this, "Logging in...", "login").execute(postData);
        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void registerAction(View view) {

        startActivity(new Intent(this, RegisterExamineeActivity.class));
    }

    @Override
    public void onPostSuccess(String responseData) {
        Gson gson = new Gson();
        LoginDao loginDao = gson.fromJson(responseData, LoginDao.class);

        SessionManager.getInstance(this).setToken(loginDao.getToken());

        if (loginDao.getUser().getUser_type().equals("admin")) {
            startActivity(new Intent(this, AdminHeroActivity.class));
            SessionManager.getInstance(this).setUserType("admin");
        } else {
            startActivity(new Intent(this, StudentHeroActivity.class));
            SessionManager.getInstance(this).setUserType("student");
        }

    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Error", errorMessage, "Ok");
    }
}
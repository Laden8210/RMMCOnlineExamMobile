package com.example.rmmconlineexam.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rmmconlineexam.MainActivity;
import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.fragment.CourseFragment;
import com.example.rmmconlineexam.fragment.ExamFragment;
import com.example.rmmconlineexam.fragment.ExamineeFragment;
import com.example.rmmconlineexam.fragment.HomeFragment;
import com.example.rmmconlineexam.fragment.SettingFragment;
import com.example.rmmconlineexam.util.Messenger;
import com.example.rmmconlineexam.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_hero);
        BottomNavigationView bnvHero = findViewById(R.id.bnvHero);

        bnvHero.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.menu_setting) {
                Messenger.showAlertDialog(this, "Logout", "Are you sure you want to logout?", "Yes", "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SessionManager.getInstance(AdminHeroActivity.this).clear();
                                startActivity(new Intent(AdminHeroActivity.this, MainActivity.class));
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }

            if (item.getItemId() == R.id.menu_examinee) {
                selectedFragment = new ExamineeFragment();
                return loadFragment(selectedFragment);
            }

            if (item.getItemId() == R.id.menu_exam) {
                selectedFragment = new ExamFragment();
                return loadFragment(selectedFragment);
            }

            return false;

        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nfvHero, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}
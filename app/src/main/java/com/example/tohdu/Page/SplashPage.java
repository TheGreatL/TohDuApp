package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;

import java.util.ArrayList;

public class SplashPage extends AppCompatActivity implements ImportantMethod {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_splash);
        runCode();
    }

    @Override
    public void runCode() {
        SharedPreferences sharedPreferences = getSharedPreferences("ISAlreadyExist", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isAlreadyExist", false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean value;
            SQLiteDB.writeTermData(this, "1stsem1yr", "1st Term 1st Year");
            SQLiteDB.writeTermData(this, "2ndsem1yr", "2nd Term 1st Year");

            SQLiteDB.writeTermData(this, "1stsem2yr", "1st Term 2nd Year");
            SQLiteDB.writeTermData(this, "2ndsem2yr", "2nd Term 2nd Year");

            SQLiteDB.writeTermData(this, "1stsem3yr", "1st Term 3rd Year");
            SQLiteDB.writeTermData(this, "2ndsem3yr", "2nd Term 3rd Year");

            SQLiteDB.writeTermData(this, "1stsem4yr", "1st Term 4th Year");
            value = SQLiteDB.writeTermData(this, "2ndsem4yr", "2nd Term 4th Year");


            editor.putBoolean("isAlreadyExist", value).apply();

        }

        new Handler().postDelayed(() -> {
            nextPage(HomePage.class, null);
        }, 2000);
    }

    @Override
    public void onButtonClicked(View view) {

    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> info) {
        startActivity(new Intent(this, nextActivity));
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}
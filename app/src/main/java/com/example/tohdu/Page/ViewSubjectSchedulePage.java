package com.example.tohdu.Page;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;

import java.util.ArrayList;

public class ViewSubjectSchedulePage extends AppCompatActivity implements ImportantMethod {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_view_subject_schedule);

    }

    @Override
    public void runCode() {

    }

    @Override
    public void onButtonClicked(View view) {

    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> data) {

    }

    @Override
    public void initData(int length) {

    }

    @Override
    public void bottomNaviAction(View view) {

    }
}
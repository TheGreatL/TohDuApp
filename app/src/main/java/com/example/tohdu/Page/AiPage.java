package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class AiPage extends AppCompatActivity implements ImportantMethod {


    private int timeBackPressOccur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_ai);
        runCode();
    }

    @Override
    public void runCode() {
        initBottomNav(R.id.aiItem);
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (timeBackPressOccur >= 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AiPage.this);
                    builder.setTitle("Leave")
                            .setCancelable(false)
                            .setMessage("Do You Wan to Leave This App?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            })
                            .setNegativeButton("No",null);
                    builder.show();

                }
                timeBackPressOccur++;

            }
        });
    }

    private void initBottomNav(int currentPage) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(currentPage);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (currentPage == menuItem.getItemId()) return false;


            if (menuItem.getItemId() == R.id.homeItem) {
                nextPage(HomePage.class, new ArrayList<>(Collections.singletonList("Home")));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (menuItem.getItemId() == R.id.scheduleItem) {
                nextPage(SchedulePage.class, new ArrayList<>(Collections.singletonList("Schedule")));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (menuItem.getItemId() == R.id.noteItem) {
                nextPage(NotePage.class, new ArrayList<>(Collections.singletonList("Note")));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (menuItem.getItemId() == R.id.aiItem) {
                nextPage(AiPage.class, new ArrayList<>(Collections.singletonList("Ai")));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            return true;
        });

    }

    @Override
    public void onButtonClicked(View view) {

        if (view.getId() == R.id.addButton) {
            nextPage(GeneralTodoListPage.class, new ArrayList<>(Collections.singletonList("AddToDo")));
        }
    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> data) {
        startActivity(new Intent(this, nextActivity));

    }




}
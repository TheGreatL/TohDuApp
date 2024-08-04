package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;

import java.util.ArrayList;
import java.util.Collections;

public class NotePage extends AppCompatActivity implements ImportantMethod {
    private int CurrentPage = 0;
    private int currentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_note);
        runCode();
    }

    @Override
    public void runCode() {
        initBottomNav(R.id.notePage);
    }

    private void initBottomNav(int currentPage) {

        int[] pageID = {R.id.homePage, R.id.schedulePage, R.id.notePage, R.id.aiPage};
        int[] iconID = {R.id.homeIcon, R.id.scheduleIcon, R.id.noteIcon, R.id.aiIcon};
        for (int index = 0; index < iconID.length; index++) {
            if (pageID[index] == currentPage) {
                this.CurrentPage = pageID[index];
                this.currentId = iconID[index];
                break;
            }
        }
        if (currentPage == R.id.homePage) {
            TextView label = findViewById(R.id.homeLabel);
            label.setVisibility(View.VISIBLE);
            ImageView icon = findViewById(R.id.homeIcon);

            icon.setColorFilter(ContextCompat.getColor(this, R.color.Neon), PorterDuff.Mode.SRC_ATOP);
        } else if (currentPage == R.id.schedulePage) {
            TextView label = findViewById(R.id.scheduleLabel);
            label.setVisibility(View.VISIBLE);
            ImageView icon = findViewById(R.id.scheduleIcon);

            icon.setColorFilter(ContextCompat.getColor(this, R.color.Neon), PorterDuff.Mode.SRC_ATOP);
        } else if (currentPage == R.id.notePage) {
            TextView label = findViewById(R.id.noteLabel);
            label.setVisibility(View.VISIBLE);
            ImageView icon = findViewById(R.id.noteIcon);

            icon.setColorFilter(ContextCompat.getColor(this, R.color.Neon), PorterDuff.Mode.SRC_ATOP);
        } else if (currentPage == R.id.aiPage) {
            TextView label = findViewById(R.id.aiLabel);
            label.setVisibility(View.VISIBLE);
            ImageView icon = findViewById(R.id.aiIcon);

            icon.setColorFilter(ContextCompat.getColor(this, R.color.Neon), PorterDuff.Mode.SRC_ATOP);
        }


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

    @Override
    public void initData(int length) {

    }

    @Override
    public void bottomNaviAction(View view) {

        if (view.getId() == CurrentPage || view.getId() == currentId)
            return;

        if (view.getId() == R.id.homePage || view.getId() == R.id.homeIcon) {
            nextPage(HomePage.class, new ArrayList<>(Collections.singleton(Collections.singletonList("As"))));

            finish();
        } else if (view.getId() == R.id.schedulePage || view.getId() == R.id.scheduleIcon) {

            nextPage(SchedulePage.class, new ArrayList<>(Collections.singleton(Collections.singletonList("As"))));
            finish();
        } else if (view.getId() == R.id.notePage || view.getId() == R.id.noteIcon) {

            nextPage(NotePage.class, new ArrayList<>(Collections.singleton(Collections.singletonList("As"))));
            finish();
        } else if (view.getId() == R.id.aiPage || view.getId() == R.id.aiIcon) {

            nextPage(AiPage.class, new ArrayList<>(Collections.singleton(Collections.singletonList("As"))));
            finish();
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}
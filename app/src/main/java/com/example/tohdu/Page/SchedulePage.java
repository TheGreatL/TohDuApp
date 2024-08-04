package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.example.tohdu.RecyclerClasses.SubjectAdapter;
import com.example.tohdu.RecyclerClasses.SubjectClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SchedulePage extends AppCompatActivity implements ImportantMethod {

    enum daysInAWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
    }

    private int CurrentPage = 0;
    private int currentId = 0;
    private final String[] terms = {"1st Year/1st Term", "1st Year/2nd Term", "2nd Year/1st Term", "2nd Year/2nd Term", "3rd Year/1st Term", "3rd Year/2nd Term"
            , "4th Year/1st Term", "4th Year/2nd Term"};
    private final String[] termsID = {"1stsem1yr", "2ndsem1yr", "1stsem2yr", "2ndsem2yr", "1stsem3yr", "2ndsem3yr"
            , "1stsem4yr", "2ndsem4yr"};

    private String termName = "";
    private String termID = "";
    private ArrayList<SubjectClass> mondayList;
    private ArrayList<SubjectClass> tuesdayList;
    private ArrayList<SubjectClass> wednesdayList;
    private ArrayList<SubjectClass> thursdayList;
    private ArrayList<SubjectClass> fridayList;
    private ArrayList<SubjectClass> saturdayList;

    private RecyclerView mondayRecyclerView;
    private RecyclerView tuesdayRecyclerView;
    private RecyclerView wednesdayRecyclerView;
    private RecyclerView thursdayRecyclerView;
    private RecyclerView fridayRecyclerView;
    private RecyclerView saturdayRecyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_schedule);
        runCode();
    }


    @Override
    public void runCode() {

        swipeRefreshLayout = findViewById(R.id.refreshBar);

        swipeRefreshLayout.setOnRefreshListener(() -> {

            swipeRefreshLayout.setRefreshing(false);
        });

        mondayRecyclerView = findViewById(R.id.mondayRecyclerView);
        tuesdayRecyclerView = findViewById(R.id.tuesdayRecyclerView);
        wednesdayRecyclerView = findViewById(R.id.wednesdayRecyclerView);
        thursdayRecyclerView = findViewById(R.id.thursdayRecyclerView);
        fridayRecyclerView = findViewById(R.id.fridayRecyclerView);
        saturdayRecyclerView = findViewById(R.id.saturdayRecyclerView);

        initBottomNav(R.id.schedulePage);
        AutoCompleteTextView dropdown = findViewById(R.id.termDropDown);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, terms);
        dropdown.setAdapter(arrayAdapter);
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            termName = terms[position];
            termID = termsID[position];
            loadRecyclerView(termID);
        });


    }

    private void loadRecyclerView(String termID) {

        mondayList = SQLiteDB.getSubjectData(this, daysInAWeek.MONDAY.toString(), termID);
        mondayRecyclerView.setAdapter(new SubjectAdapter(mondayList, position -> {
            Log.d("MondayRecyclerView", "loadRecyclerView: " + mondayList.get(position));
        }));

        tuesdayList = SQLiteDB.getSubjectData(this, daysInAWeek.TUESDAY.toString(), termID);
        tuesdayRecyclerView.setAdapter(new SubjectAdapter(tuesdayList, position -> {

        }));

        wednesdayList = SQLiteDB.getSubjectData(this, daysInAWeek.WEDNESDAY.toString(), termID);
        wednesdayRecyclerView.setAdapter(new SubjectAdapter(wednesdayList, position -> {

        }));

        thursdayList = SQLiteDB.getSubjectData(this, daysInAWeek.THURSDAY.toString(), termID);
        thursdayRecyclerView.setAdapter(new SubjectAdapter(thursdayList, position -> {

        }));

        fridayList = SQLiteDB.getSubjectData(this, daysInAWeek.FRIDAY.toString(), termID);
        fridayRecyclerView.setAdapter(new SubjectAdapter(fridayList, position -> {

        }));

        saturdayList = SQLiteDB.getSubjectData(this, daysInAWeek.SATURDAY.toString(), termID);
        saturdayRecyclerView.setAdapter(new SubjectAdapter(saturdayList, position -> {

        }));

        if (!mondayList.isEmpty() || !tuesdayList.isEmpty() || !wednesdayList.isEmpty() || !thursdayList.isEmpty() || !fridayList.isEmpty() || !saturdayList.isEmpty()) {
            findViewById(R.id.addButton).setVisibility(View.GONE);
            findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
            return;
        }
        findViewById(R.id.addButton).setVisibility(View.VISIBLE);
        findViewById(R.id.deleteButton).setVisibility(View.GONE);

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
            if (termName.isEmpty() && termID.isEmpty()) {
                Toast.makeText(this, "Select Year/Term First", Toast.LENGTH_SHORT).show();
                return;
            }
            nextPage(AddSchedulePage.class, new ArrayList<>(Arrays.asList(termName, termID)));


        } else if (view.getId() == R.id.deleteButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Subject")
                    .setMessage("Do you want to delete these subjects schedule?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                                Toast.makeText(SchedulePage.this, "Item Delete", Toast.LENGTH_SHORT).show();
                                SQLiteDB.deleteSubjectSchedule(SchedulePage.this, termID);
                            }
                    )
                    .setNegativeButton("No", null);
            builder.show();
        }
    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> data) {
        Intent intent = new Intent(this, nextActivity);
        for (int index = 0; index < data.size(); index++) {
            intent.putExtra("data" + index, data.get(index).toString());

        }
        startActivity(intent);

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
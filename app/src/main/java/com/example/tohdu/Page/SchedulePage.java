package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.example.tohdu.RecyclerClasses.SubjectAdapter;
import com.example.tohdu.RecyclerClasses.SubjectClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SchedulePage extends AppCompatActivity implements ImportantMethod {

    enum daysInAWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
    }

    private final String[] terms = {"1st Year/1st Term", "1st Year/2nd Term", "2nd Year/1st Term", "2nd Year/2nd Term", "3rd Year/1st Term", "3rd Year/2nd Term"
            , "4th Year/1st Term", "4th Year/2nd Term"};
    private final String[] termsID = {"1stsem1yr", "2ndsem1yr", "1stsem2yr", "2ndsem2yr", "1stsem3yr", "2ndsem3yr"
            , "1stsem4yr", "2ndsem4yr"};

    private int timeBackPressOccur=0;
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

        initBottomNav(R.id.scheduleItem);
        AutoCompleteTextView dropdown = findViewById(R.id.termDropDown);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, terms);
        dropdown.setAdapter(arrayAdapter);
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            termName = terms[position];
            termID = termsID[position];

            loadRecyclerView(termID);
        });
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (timeBackPressOccur >= 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SchedulePage.this);
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

    private void goViewSubjectDetailsPage(int id) {
        nextPage(ViewSubjectDetailsPage.class, new ArrayList<>(Collections.singletonList(String.valueOf(id))));
    }

    private void loadRecyclerView(String termID) {

        mondayList = SQLiteDB.getSubjectData(this, daysInAWeek.MONDAY.toString(), termID);
        mondayRecyclerView.setAdapter(new SubjectAdapter(mondayList, position -> goViewSubjectDetailsPage(mondayList.get(position).getSubjectID())));

        tuesdayList = SQLiteDB.getSubjectData(this, daysInAWeek.TUESDAY.toString(), termID);
        tuesdayRecyclerView.setAdapter(new SubjectAdapter(tuesdayList, position -> goViewSubjectDetailsPage(tuesdayList.get(position).getSubjectID())));

        wednesdayList = SQLiteDB.getSubjectData(this, daysInAWeek.WEDNESDAY.toString(), termID);
        wednesdayRecyclerView.setAdapter(new SubjectAdapter(wednesdayList, position -> goViewSubjectDetailsPage(wednesdayList.get(position).getSubjectID())));

        thursdayList = SQLiteDB.getSubjectData(this, daysInAWeek.THURSDAY.toString(), termID);
        thursdayRecyclerView.setAdapter(new SubjectAdapter(thursdayList, position -> goViewSubjectDetailsPage(thursdayList.get(position).getSubjectID())));

        fridayList = SQLiteDB.getSubjectData(this, daysInAWeek.FRIDAY.toString(), termID);
        fridayRecyclerView.setAdapter(new SubjectAdapter(fridayList, position -> goViewSubjectDetailsPage(fridayList.get(position).getSubjectID())));

        saturdayList = SQLiteDB.getSubjectData(this, daysInAWeek.SATURDAY.toString(), termID);
        saturdayRecyclerView.setAdapter(new SubjectAdapter(saturdayList, position -> goViewSubjectDetailsPage(saturdayList.get(position).getSubjectID())));

        if (!mondayList.isEmpty() || !tuesdayList.isEmpty() || !wednesdayList.isEmpty() || !thursdayList.isEmpty() || !fridayList.isEmpty() || !saturdayList.isEmpty()) {
            findViewById(R.id.addButton).setVisibility(View.GONE);
            findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);


            if(mondayList.isEmpty()){
                findViewById(R.id.mondayTxt).setVisibility(View.GONE);
            }
            else if(tuesdayList.isEmpty()){
                findViewById(R.id.tuesdayTxt).setVisibility(View.GONE);
            }

            else if(wednesdayList.isEmpty()){
                findViewById(R.id.wednesdayTxt).setVisibility(View.GONE);
            }

            else if(thursdayList.isEmpty()){
                findViewById(R.id.thursdayTxt).setVisibility(View.GONE);
            }

            else if(fridayList.isEmpty()){
                findViewById(R.id.fridayTxt).setVisibility(View.GONE);
            }

            else if(saturdayList.isEmpty()){
                findViewById(R.id.saturdayTxt).setVisibility(View.GONE);
            }
            return;
        }
        findViewById(R.id.addButton).setVisibility(View.VISIBLE);
        findViewById(R.id.deleteButton).setVisibility(View.GONE);

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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (menuItem.getItemId() == R.id.noteItem) {
                nextPage(NotePage.class, new ArrayList<>(Collections.singletonList("Note")));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        return super.getOnBackInvokedDispatcher();
    }
}
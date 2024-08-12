package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tohdu.AlarmReceiver;
import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.example.tohdu.RecyclerClasses.TodoAdapter;
import com.example.tohdu.RecyclerClasses.TodoItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class HomePage extends AppCompatActivity implements ImportantMethod {

    private List<TodoItem> todoItemList;
    private List<TodoItem> historyList;
    private RecyclerView todoRecyclerView;
    private RecyclerView historyRecyclerView;
    private int timeBackPressOccur = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home);
        runCode();
    }

    @Override
    public void runCode() {
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initializeRecyclerView();
            swipeRefreshLayout.setRefreshing(false);
        });
        initializeRecyclerView();
        initBottomNav(R.id.homeItem);
        createNotificationChannel();
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (timeBackPressOccur >= 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
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

    private void initializeRecyclerView() {
        todoItemList = SQLiteDB.getTodoItem(this);
        assert todoItemList != null;
        todoRecyclerView.setAdapter(new TodoAdapter(todoItemList,
                position -> {
                    ArrayList<Object> pass = new ArrayList<>();
                    pass.add("ViewAlarm");
                    pass.add(todoItemList.get(position).getId());
                    pass.add("NOT DONE");
                    nextPage(GeneralTodoListPage.class, pass);

                }, R.layout.layout_tohdu));
        historyList = SQLiteDB.getTodoHistory(this);
        assert historyList != null;
        historyRecyclerView.setAdapter(new TodoAdapter(historyList,
                position -> {
                    ArrayList<Object> pass = new ArrayList<>();
                    pass.add("ViewAlarm");
                    pass.add(historyList.get(position).getId());
                    pass.add("DONE");
                    nextPage(GeneralTodoListPage.class, pass);

                }, R.layout.layout_tohdu));

        setAlarm();
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

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm() {
        if (todoItemList.isEmpty()) return;
        for (int index = 0; index < todoItemList.size(); index++) {
            String time = todoItemList.get(index).getTime();

            int hours = Integer.parseInt(time.split(":")[0]);
            int minutes = Integer.parseInt(time.split(":")[1]);

            String date = todoItemList.get(index).getDate();

            int year = Integer.parseInt(date.split("/")[0]);
            int months = Integer.parseInt(date.split("/")[1]);
            int day = Integer.parseInt(date.split("/")[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, (months - 1));
            calendar.set(Calendar.DAY_OF_MONTH, day);

            calendar.set(Calendar.HOUR_OF_DAY, hours); // Set the alarm time to 14:00 (2 PM) UTC
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, 0);


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("title", todoItemList.get(index).getTitle());
            intent.putExtra("description", todoItemList.get(index).getMessage());
            intent.putExtra("id", todoItemList.get(index).getId());
            intent.putExtra("reqCode", index);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, index, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Check if alarmManager is null to handle rare cases where getSystemService returns null
            if (alarmManager == null) {
//                Log.d("SampleDebugTag", "Alarm Manager is null");
                Toast.makeText(this, "Alarm Manager is null", Toast.LENGTH_SHORT).show();
                return;
            }

            // Log the time at which the alarm is scheduled to trigger
//            Log.d("SampleDebugTag", "Alarm set for: " + calendar.getTime().toString());
//            Log.d("SampleDebugTag", "Alarm time in millis: " + calendar.getTimeInMillis());

            // Use setExactAndAllowWhileIdle for precise timing
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


//
//            Log.d("SampleDebugTag", "\n");
        }

    }


    private void createNotificationChannel() {
        CharSequence name = "TohDuApp";
        String desc = "Channel for Alarm Manager";
        int imp = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("TohDuApp", name, imp);
        channel.setDescription(desc);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == R.id.addButton) {
            nextPage(GeneralTodoListPage.class, new ArrayList<>(Collections.singletonList("AddToDo")));
        }
    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> info) {
        Intent intent = new Intent(this, nextActivity);
        for (int index = 0; index < info.size(); index++) {

            intent.putExtra("data" + index, info.get(index).toString());
        }
        intent.putExtra("size", info.size());
        startActivity(intent);
    }


}
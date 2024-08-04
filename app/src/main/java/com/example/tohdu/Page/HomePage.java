package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tohdu.AlarmReceiver;
import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.example.tohdu.RecyclerClasses.TodoAdapter;
import com.example.tohdu.RecyclerClasses.TodoItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class HomePage extends AppCompatActivity implements ImportantMethod {

    private List<TodoItem> todoItemList;
    private int CurrentPage = 0;
    private int currentId = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home);

        runCode();
    }

    @Override
    public void runCode() {
        initBottomNav(R.id.homePage);
        todoItemList = SQLiteDB.getTodoItem(this);
        RecyclerView todoRecyclerView = findViewById(R.id.todoRecyclerView);
        assert todoItemList != null;
        todoRecyclerView.setAdapter(new TodoAdapter(todoItemList,
                position -> {
                    ArrayList<Object> pass = new ArrayList<>();
                    pass.add("ViewAlarm");
                    pass.add(todoItemList.get(position).getId());
                    pass.add("NOT DONE");
                    nextPage(GeneralTodoListPage.class, pass);

                }, R.layout.layout_tohdu));

        List<TodoItem> historyList = SQLiteDB.getTodoHistory(this);

        RecyclerView history = findViewById(R.id.historyRecyclerView);
        assert historyList != null;
        history.setAdapter(new TodoAdapter(historyList,
                position -> {
                    ArrayList<Object> pass = new ArrayList<>();
                    pass.add("ViewAlarm");
                    pass.add(historyList.get(position).getId());
                    pass.add("DONE");
                    nextPage(GeneralTodoListPage.class, pass);

                }, R.layout.layout_tohdu));

        createNotificationChannel();
        setAlarm();


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

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm() {

        if (todoItemList.isEmpty()) return;
        //Dapat Nasa Huli ng iterration yung matagal
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
                Log.d("SampleDebugTag", "Alarm Manager is null");
                Toast.makeText(this, "Alarm Manager is null", Toast.LENGTH_SHORT).show();
                return;
            }

            // Log the time at which the alarm is scheduled to trigger
            Log.d("SampleDebugTag", "Alarm set for: " + calendar.getTime().toString());
            Log.d("SampleDebugTag", "Alarm time in millis: " + calendar.getTimeInMillis());

            // Use setExactAndAllowWhileIdle for precise timing
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Log.d("SampleDebugTag", "Alarm scheduled");

            Log.d("SampleDebugTag", "\n");
        }

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TohDuApp";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("TohDuApp", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onButtonClicked(View view) {


        if (view.getId() == R.id.addButton) {
            finish();
            startActivity(getIntent());
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
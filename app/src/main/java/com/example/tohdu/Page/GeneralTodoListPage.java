package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.example.tohdu.RecyclerClasses.TodoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GeneralTodoListPage extends AppCompatActivity implements ImportantMethod {

    private String time,date;
    private int todoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runCode();

    }

    @Override
    public void runCode() {

        String activity = getIntent().getStringExtra("data0");
        setContentView(Objects.equals(activity, "AddToDo") ? R.layout.page_add_tohdo : R.layout.page_view_todo_details);
        if (Objects.equals(activity, "ViewAlarm")) {

            try {
                todoID = Integer.parseInt(getIntent().getStringExtra("data1"));
            } catch (Exception e) {
                todoID = getIntent().getIntExtra("data1", -1);
            }
            String table = Objects.equals(getIntent().getStringExtra("data2"), "NOT DONE") ? "TodoTbl" : "HistoryTBL";
            if(table.equals("HistoryTBL")) findViewById(R.id.doneButton).setVisibility(View.GONE);
            TextView title = findViewById(R.id.todoTitle);
            TextView date = findViewById(R.id.todoDate);
            TextView time = findViewById(R.id.todoTime);
            EditText note = findViewById(R.id.todoNote);

            note.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!SQLiteDB.updateNote(GeneralTodoListPage.this, table, todoID, s.toString())) {
                        Toast.makeText(GeneralTodoListPage.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            TodoItem todoItem = null;
            try {
                todoItem = SQLiteDB.getSingleTodo(this, todoID, table);

            } catch (RuntimeException e) {
                Toast.makeText(this, "This task is Already Done", Toast.LENGTH_SHORT).show();
                nextPage(HomePage.class, null);
                finish();
            }
            if (todoItem == null) return;


            String year = todoItem.getDate().split("/")[0];
            String month = todoItem.getDate().split("/")[1];
            String day = todoItem.getDate().split("/")[2];

            String setDate = UtilClass.months[Integer.parseInt(month) - 1] + " " + day + ", " + year;
            int hours = Integer.parseInt(todoItem.getTime().split(":")[0]);
            int minutes = Integer.parseInt(todoItem.getTime().split(":")[1]);

            String prefix = hours >= 12 ? "PM" : "AM";

            String setTime = (hours > 12 ? hours - 12 : hours == 0 ? 12 : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + prefix;

            title.setText(todoItem.getTitle());
            date.setText(setDate);
            time.setText(setTime);
            note.setText(todoItem.getMessage());

        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == R.id.pickDate) {
            String currentDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            int currYear = Integer.parseInt(currentDay.split("-")[0]);
            int currMonth = Integer.parseInt(currentDay.split("-")[1]);
            int currDay = Integer.parseInt(currentDay.split("-")[2]);
            date = currYear + "/" + (currMonth + 1) + "/" + (currDay < 10 ? "0" + currDay : currDay);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.Date_TimePickerStyle);
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener((datePicker, selectedYear, selectedMonth, selectedDay) -> {


                //If Current Year ignore, and go to months
                if (currYear > selectedYear) {
                    UtilClass.errorMessage(this,"It should be ahead of the current year");
                    return;
                }
                //pag mas malaki yung current month sa selected month
                // if equal yung year or mas mababa sa current year
                if (currMonth > (selectedMonth + 1) && currYear >= selectedYear) {
                    UtilClass.errorMessage(this,"It should be ahead of the current year and month");
                    return;
                }

                date = selectedYear + "/" + ((selectedMonth + 1) < 10 ? "0" + (selectedMonth + 1) : selectedMonth + 1) + "/" + (selectedDay < 10 ? "0" + selectedDay : selectedDay);
                ((TextView) findViewById(R.id.pickDate)).setText(selectedYear + " / " + (selectedMonth + 1) + " / " + (selectedDay < 10 ? "0" + selectedDay : selectedDay));
            });

        } else if (view.getId() == R.id.pickTime) {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            int currentHours = Integer.parseInt(currentTime.split(":")[0]);
            int currentMinutes = Integer.parseInt(currentTime.split(":")[1]);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.Date_TimePickerStyle, (view1, hour, minute) -> {
                String timePrefix = hour >= 12 ? "PM" : "AM";


                //If Greater then 12, will -10
                //If not, the hour will retain
                // If hour is 0 the hour will become 12
                //0 in hours means 12 AM;

               time = (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
                ((TextView) findViewById(R.id.pickTime)).setText((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":" + (minute < 10 ? "0" + minute : minute) + timePrefix);

            }, currentHours, currentMinutes, false);

            timePickerDialog.show();

        } else if (view.getId() == R.id.submitButton) {
            String title = ((EditText) findViewById(R.id.todoTitleEditText)).getText().toString();
            String note = ((EditText) findViewById(R.id.noteEditText)).getText().toString();

            if ((time == null || time.isEmpty()) || (date == null || date.isEmpty())) {
                Toast.makeText(this, "Please Don't Leave Field Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_process_dialog);
            dialog.show();
            if (SQLiteDB.writeTodo(this, title,date, time, note)) {
                Toast.makeText(this, "Successful Set", Toast.LENGTH_SHORT).show();
                nextPage(HomePage.class, null);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        } else if (view.getId() == R.id.backButton) {
            finish();

        } else if (view.getId() == R.id.doneButton) {
            if(!SQLiteDB.deleteItem(this, todoID)){
                return;
            }
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Your Todo will go to history", Toast.LENGTH_SHORT).show();
            nextPage(HomePage.class,null);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            finish();
        } else if (view.getId() == R.id.changeButton) {
            Toast.makeText(this, "Change Button", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> data) {
        startActivity(new Intent(this, nextActivity));
    }




}
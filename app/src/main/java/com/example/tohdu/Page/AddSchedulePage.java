package com.example.tohdu.Page;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class AddSchedulePage extends AppCompatActivity implements ImportantMethod {


    private LinearLayout subjectInputContainer;
    private final Map<Integer, String> viewMap = new LinkedHashMap<>();
    private final Map<Integer, Map<Integer, String>> additionalSchedListContainer = new LinkedHashMap<>();
    //     ArrayList<String> container = new ArrayList<>();

    private final ArrayList<ArrayList<String>> subjectListContainer = new ArrayList<>();


    private int numOfSub = 0;
    private String termYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runCode();

    }

    @Override
    public void runCode() {
        setContentView(R.layout.page_add_schedule);

        termYear = getIntent().getStringExtra("data0");

        subjectInputContainer = findViewById(R.id.subjectInputLinearLayout);

        ((TextView) findViewById(R.id.termTextView)).setText(termYear);

        EditText numberOfDays = findViewById(R.id.numDayOffEditText);
        numberOfDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addSubject(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void addSubject(@NonNull String num) {
        ///Enter Subject Name First

        //First Thing to Do
        if (num.isEmpty()) return;


        int number = Integer.parseInt(num);
        if (number > 10) {
            Toast.makeText(this, "Too Many Please Lower Your Input", Toast.LENGTH_SHORT).show();
            return;
        }
        subjectListContainer.clear();
        additionalSchedListContainer.clear();
        subjectInputContainer.removeAllViews();
        viewMap.clear();
        numOfSub = number;
//Subject Set
        for (int index = 1; index <= number; index++) {
            int[] indexInArray = {index};
            Map<Integer, String> additionalSched = new LinkedHashMap<>();
            View view = getLayoutInflater().inflate(R.layout.layout_input_subject, null);


            TextView label = view.findViewById(R.id.subjectLabel);
            label.setText((label.getText() + ":" + index));


            EditText subjectName = view.findViewById(R.id.subjectNameEditText);
            subjectName.setId(View.generateViewId());
            viewMap.put(subjectName.getId(), "SubjectName:" + index);


//          ------------------ TIME
            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            int currentHours = Integer.parseInt(time.split(":")[0]);
            int currentMinutes = Integer.parseInt(time.split(":")[1]);
//           ----------------


            ArrayAdapter<CharSequence> spinnerAdapter;

            Spinner subjectDaySpinner1 = view.findViewById(R.id.subjectDaySpinner);
            subjectDaySpinner1.setId(View.generateViewId());
            viewMap.put(subjectDaySpinner1.getId(), "");

            spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.daySpinner, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectDaySpinner1.setAdapter(spinnerAdapter);
            subjectDaySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    viewMap.replace(subjectDaySpinner1.getId(), parent.getItemAtPosition(position).toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            EditText subjectRoom1 = view.findViewById(R.id.subjectRoomEditText);
            subjectRoom1.setId(View.generateViewId());
            viewMap.put(subjectRoom1.getId(), "RoomName");

            TextView startView1 = view.findViewById(R.id.pickStartTimeView);
            startView1.setId(View.generateViewId());
            viewMap.put(startView1.getId(), "");
            startView1.setOnClickListener(v -> {

                if (isSubjectNameAlreadyInputted(subjectName)) return;

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.Date_TimePickerStyle, (tView, hour, minute) -> {
                    String timePrefix = hour >= 12 ? "PM" : "AM";
                    ((TextView) findViewById(startView1.getId())).setText((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":" + (minute < 10 ? "0" + minute : minute) + timePrefix);

                    viewMap.replace(startView1.getId(), (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute));


                }, currentHours, currentMinutes, false);
                timePickerDialog.show();
            });


            TextView endView1 = view.findViewById(R.id.pickEndTimeView);
            endView1.setId(View.generateViewId());
            viewMap.put(endView1.getId(), "");
            endView1.setOnClickListener((v) -> {

                if (isSubjectNameAlreadyInputted(subjectName)) return;

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.Date_TimePickerStyle, (tView, hour, minute) -> {
                    String timePrefix = hour >= 12 ? "PM" : "AM";
                    ((TextView) findViewById(endView1.getId())).setText((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":" + (minute < 10 ? "0" + minute : minute) + timePrefix);

                    viewMap.replace(endView1.getId(), (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute));


                }, currentHours, currentMinutes, false);
                timePickerDialog.show();
            });

            //================
            LinearLayout scheduleLayout = view.findViewById(R.id.scheduleLayout);
            Button addButton = view.findViewById(R.id.addScheduleButton);
            addButton.setOnClickListener(addButtonView -> {

                if (isSubjectNameAlreadyInputted(subjectName)) return;
                View addedView = getLayoutInflater().inflate(R.layout.layout_input_add_subject, null);


                Spinner subjectDaySpinner = addedView.findViewById(R.id.subjectDaySpinner);
                subjectDaySpinner.setId(View.generateViewId());
                additionalSched.put(subjectDaySpinner.getId(), "");

                ArrayAdapter<CharSequence> spinnerAdapterAd = ArrayAdapter.createFromResource(this, R.array.daySpinner, android.R.layout.simple_spinner_item);
                spinnerAdapterAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectDaySpinner.setAdapter(spinnerAdapterAd);
                subjectDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        additionalSched.replace(subjectDaySpinner.getId(), subjectName.getText() + "/" + parent.getItemAtPosition(position).toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                EditText subjectRoom = addedView.findViewById(R.id.subjectRoomEditText);
                subjectRoom.setId(View.generateViewId());
                additionalSched.put(subjectRoom.getId(), "RoomName");

                TextView startView = addedView.findViewById(R.id.pickStartTimeView);
                startView.setId(View.generateViewId());
                additionalSched.put(startView.getId(), "");
                startView.setOnClickListener(v -> {

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.Date_TimePickerStyle, (tView, hour, minute) -> {
                        String timePrefix = hour >= 12 ? "PM" : "AM";

                        additionalSched.replace(startView.getId(), (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute));

                        ((TextView) findViewById(startView.getId())).setText((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":" + (minute < 10 ? "0" + minute : minute) + timePrefix);

                    }, currentHours, currentMinutes, false);
                    timePickerDialog.show();
                });


                TextView endView = addedView.findViewById(R.id.pickEndTimeView);
                endView.setId(View.generateViewId());
                additionalSched.put(endView.getId(), "");
                endView.setOnClickListener((v) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.Date_TimePickerStyle, (tView, hour, minute) -> {
                        String timePrefix = hour >= 12 ? "PM" : "AM";

                        additionalSched.replace(endView.getId(), (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute));
                        ((TextView) findViewById(endView.getId())).setText((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":" + (minute < 10 ? "0" + minute : minute) + timePrefix);

                    }, currentHours, currentMinutes, false);
                    timePickerDialog.show();
                });
                scheduleLayout.addView(addedView);
            });

            Button clearButton = view.findViewById(R.id.clearScheduleButton);
            clearButton.setOnClickListener(clearBtnView -> {

                for (int childCount = scheduleLayout.getChildCount() - 1; childCount > 1; childCount--) {
                    if (scheduleLayout.getChildCount() == 1) break;
                    scheduleLayout.removeViewAt(childCount);
                }

                if (scheduleLayout.getChildCount() != 1) {

                    scheduleLayout.removeViewAt(scheduleLayout.getChildCount() - 1);
                }

                this.additionalSchedListContainer.remove(indexInArray[0]);
                //  this.additionalSched.clear();
            });


            //================
            EditText subjectInstructor = view.findViewById(R.id.subjectInstructorEditText);
            subjectInstructor.setId(View.generateViewId());
            viewMap.put(subjectInstructor.getId(), "Instructor");


            Spinner subjectTypeSpinner = view.findViewById(R.id.subjectTypeSpinner);
            subjectTypeSpinner.setId(View.generateViewId());
            spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.typeSpinner, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectTypeSpinner.setAdapter(spinnerAdapter);
            viewMap.put(subjectTypeSpinner.getId(), "");

            subjectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viewMap.replace(subjectTypeSpinner.getId(), parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            additionalSchedListContainer.put(index, additionalSched);
            subjectInputContainer.addView(view);
        }


    }

    private boolean isSubjectNameAlreadyInputted(EditText subjectName) {
        if (subjectName.getText().toString().isEmpty()) {
            Toast.makeText(AddSchedulePage.this, "Please Enter The Subject Name First", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == R.id.backButton) {
            finish();
        } else if (view.getId() == R.id.saveButton) {
            this.subjectListContainer.clear();
            if (numOfSub == 0) {
                Toast.makeText(this, "Please use app correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            int index = 0;
            String subjectName = "";
            StringBuilder formatter = new StringBuilder();

            ArrayList<String> temporaryContainer = new ArrayList<>();

            for (Map.Entry<Integer, String> entry : viewMap.entrySet()) {
                View viewInput = findViewById(entry.getKey());
                String text = "";
                switch (index) {
                    case 0:
                        subjectName = ((EditText) viewInput).getText().toString().trim();
                        text = "SubjectsName=" + subjectName;
//                        Log.d("General_Schedule_Page", text);
                        temporaryContainer.add(text);
                        break;
                    case 1:
                        //Tuesday
                        formatter.append("SubjectSchedule").append(":");
                        formatter.append(entry.getValue());
                        formatter.append(",");
                        break;
                    case 2:
                        //A405
                        formatter.append(((EditText) viewInput).getText().toString().trim().toUpperCase());
                        formatter.append("=");
                        break;
                    case 3:
                        formatter.append(entry.getValue());
                        formatter.append("-");
                        break;
                    case 4:
                        //Schedule Info
                        formatter.append(entry.getValue());
//                        Log.d("General_Schedule_Page", formatter.toString());
                        temporaryContainer.add(formatter.toString());
                        initializeAdditionalSched(subjectName, temporaryContainer);

                        break;
                    case 5:
                        //Instructor
                        text = "SubjectInstructor:" + ((EditText) viewInput).getText().toString().trim();
//                        Log.d("General_Schedule_Page", text);
                        temporaryContainer.add(text);
                        break;

                    case 6:
                        //Subject Type
                        text = "SubjectType:" + entry.getValue().toUpperCase();
//                        Log.d("General_Schedule_Page", text);
                        temporaryContainer.add(text);

                        //Clearing Variable
                        formatter.setLength(0);
                        subjectName = "";
                        index = 0;

                        subjectListContainer.add(temporaryContainer);
                        temporaryContainer = new ArrayList<>();
                        continue;


                }
                index++;


            }

            boolean write = false;
            for (int indexOfList = 0; indexOfList < subjectListContainer.size(); indexOfList++) {
                Log.d("General_Schedule_Page", "Subject:" + (indexOfList + 1) + " " + subjectListContainer.get(indexOfList));
                if (SQLiteDB.writeSubject(this, termYear, getIntent().getStringExtra("data1"), subjectListContainer.get(indexOfList)))
                    write = true;
                else return;


            }
            if (write) {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.layout_process_dialog);
                dialog.show();

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                nextPage(SchedulePage.class, new ArrayList<>(Collections.singletonList("as")));
            }


//


        }
    }

    private void initializeAdditionalSched(String subjectName, ArrayList<String> temporaryContainer) {
//        Log.d("General_Schedule_Page", "357:"+subjectName);
//        Log.d("General_Schedule_Page", "358:"+additionalSched.size());

        StringBuilder formatter = new StringBuilder();
        int index = 0;
        for (Map.Entry<Integer, Map<Integer, String>> childMap : additionalSchedListContainer.entrySet()) {
            Map<Integer, String> tempMap = childMap.getValue();
            for (Map.Entry<Integer, String> addSched : tempMap.entrySet()) {
                View viewInput = findViewById(addSched.getKey());
                String subjectNameHolder;

                switch (index) {
                    case 0:
                        //Philpop/Wednesday
                        try {
                            subjectNameHolder = addSched.getValue().split("/")[0];
                            if (!subjectNameHolder.equals(subjectName)) continue;
                            formatter.append("SubjectSchedule").append(":");
                            formatter.append(addSched.getValue().split("/")[1]);
                            formatter.append(",");
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Toast.makeText(this, "Please Double Check Your input Before You fucking Click Save", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case 1:
                        //A405
                        formatter.append(((EditText) viewInput).getText().toString().trim().toUpperCase());
                        formatter.append("=");
                        break;
                    case 2:
                        formatter.append(addSched.getValue());
                        formatter.append("-");
                        break;
                    case 3:
                        //Schedule Info
                        formatter.append(addSched.getValue());
                        temporaryContainer.add(formatter.toString());
                        formatter.setLength(0);
                        index = 0;

                        continue;


                }
                index++;
            }
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


}
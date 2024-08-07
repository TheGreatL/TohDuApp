package com.example.tohdu.Page;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tohdu.Database.SQLiteDB;
import com.example.tohdu.ImportantMethod;
import com.example.tohdu.R;
import com.example.tohdu.RecyclerClasses.SubjectClass;

import java.util.ArrayList;

public class ViewSubjectDetailsPage extends AppCompatActivity implements ImportantMethod {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_view_subject_details);
        runCode();
    }

    @Override
    public void runCode() {
            int id =Integer.parseInt( getIntent().getStringExtra("data0"));
        Log.d("GETSTRINGSINGLESUBJECT", "ID"+id);
      SubjectClass subjectList =  SQLiteDB.getSingleSubject(this,id);

        assert subjectList != null;
        ((TextView)findViewById(R.id.subjectNameView)).setText(subjectList.getName());
        ((TextView)findViewById(R.id.subjectScheduleView)).setText(subjectList.getSched());
        ((TextView)findViewById(R.id.subjectInstructorView)).setText(subjectList.getInstructor());
        ((TextView)findViewById(R.id.subjectTermView)).setText(subjectList.getTermName());
        ((TextView)findViewById(R.id.subjectTypeView)).setText(subjectList.getSubjectType());

    }

    @Override
    public void onButtonClicked(View view) {
        if(view.getId()==R.id.backButton) finish();
    }

    @Override
    public void nextPage(Class<?> nextActivity, ArrayList<Object> data) {

    }


}
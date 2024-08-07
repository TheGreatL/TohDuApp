package com.example.tohdu;

import android.view.View;

import java.util.ArrayList;

public interface ImportantMethod {

    void runCode();
    void onButtonClicked(View view);
    void nextPage(Class<?> nextActivity, ArrayList<Object> data);
}

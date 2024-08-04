package com.example.tohdu.RecyclerClasses;

public class TodoItem {
    private String title,date,time,message;
    private int id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public TodoItem(String title, String date, String time, String message, int id) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.message = message;
        this.id = id;
    }
}

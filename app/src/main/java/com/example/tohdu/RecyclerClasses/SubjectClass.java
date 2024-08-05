package com.example.tohdu.RecyclerClasses;

public class SubjectClass {
    private int subjectID;
    private String name;
    private String room;
    private String timeIn;
    private String day;
    private String timeOut;
    private String sched;
    private String termName;

    public void setSched(String sched) {
        this.sched = sched;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public SubjectClass( String name, String instructor, String subjectType,String termName, String sched) {
        this.name = name;
        this.sched = sched;
        this.instructor = instructor;
        this.subjectType = subjectType;
        this.termName = termName;
    }

    public SubjectClass(int subjectID, String name, String room, String instructor, String timeIn, String timeOut) {
        this.subjectID = subjectID;
        this.name = name;
        this.room = room;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.instructor = instructor;
    }

    public SubjectClass(int subjectID, String name, String room, String timeIn, String day, String timeOut, String instructor, String subjectType) {
        this.subjectID = subjectID;
        this.name = name;
        this.room = room;
        this.timeIn = timeIn;
        this.day = day;
        this.timeOut = timeOut;
        this.instructor = instructor;
        this.subjectType = subjectType;
    }
    public String getSched() {
        return sched;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    private String instructor;
    private String subjectType;

}

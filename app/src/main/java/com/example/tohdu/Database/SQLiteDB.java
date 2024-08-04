package com.example.tohdu.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tohdu.Page.SchedulePage;
import com.example.tohdu.RecyclerClasses.SubjectAdapter;
import com.example.tohdu.RecyclerClasses.SubjectClass;
import com.example.tohdu.RecyclerClasses.TodoItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.Subject;

public class SQLiteDB extends SQLiteOpenHelper {

    public SQLiteDB(@Nullable Context context) {
        super(context, "TohDu_DB", null, 2);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoTBL(id INTEGER PRIMARY KEY AUTOINCREMENT,Title VARCHAR(100),Date DATE, Time TIME,Note TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS HistoryTBL(id INTEGER PRIMARY KEY,Title VARCHAR(100),Date DATE, Time TIME,Note TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TermTBL(TermID VARCHAR(10) PRIMARY KEY NOT NULL ,  TermName VARCHAR(50))");
        db.execSQL("CREATE TABLE IF NOT EXISTS SubjectTBL(SubjectID INTEGER PRIMARY KEY AUTOINCREMENT,TermID VARCHAR(10) ,SubjectName VARCHAR(50),SubjectScheduleID VARCHAR(50) UNIQUE, SubjectInstructor VARCHAR(50),SubjectType VARCHAR(10),FOREIGN KEY (TermID) REFERENCES TermTBL(TermID));");
        db.execSQL("CREATE TABLE IF NOT EXISTS ScheduleTbl(  ScheduleID VARCHAR(50),SubjectDay VARCHAR(50),SubjectRoom VARCHAR(50),SubjectTimeStart TIME,SubjectTimeEnd TIME,FOREIGN KEY (ScheduleID) REFERENCES SubjectTBL(SubjectScheduleID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TodoTBL");
        db.execSQL("DROP TABLE IF EXISTS HistoryTBL");
        db.execSQL("DROP TABLE IF EXISTS TermTBL");
        db.execSQL("DROP TABLE IF EXISTS SubjectTBL");
        db.execSQL("DROP TABLE IF EXISTS ScheduleTbl");
        onCreate(db);
    }

    public static boolean writeTermData(Context context, String... data) {
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("TermID", data[0]);
            values.put("TermName", data[1]);
            return 1 <= sqLiteDatabase.insert("TermTBL", null, values);
        } catch (SQLException e) {
            errorMessage("Writing Term Data Failed", context);
        }
        return false;
    }

    public static boolean writeTodo(Context context, String... data) {

        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("Title", data[0]);
            values.put("Date", data[1]);
            values.put("Time", data[2]);
            values.put("Note", data[3]);
            return 1 <= sqLiteDatabase.insert("TodoTBL", null, values);

        } catch (SQLException exception) {
            errorMessage("Error Write", context);
        }


        return false;
    }

    public static boolean updateNote(Context context, String table, int id, String newNote) {
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("Note", newNote);

            return 1 <= sqLiteDatabase.update(table, values, "id=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            errorMessage("Error on updating", context);
        }
        return false;
    }

    @SuppressLint("Range")
    public static TodoItem getSingleTodo(Context context, int id, String table) throws RuntimeException {

        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getReadableDatabase();
             Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + table + " WHERE id = ?", new String[]{String.valueOf(id)})) {

            cursor.moveToNext();
            return new TodoItem(
                    cursor.getString(cursor.getColumnIndex("Title")),
                    cursor.getString(cursor.getColumnIndex("Date")),
                    cursor.getString(cursor.getColumnIndex("Time")),
                    cursor.getString(cursor.getColumnIndex("Note")),
                    cursor.getInt(cursor.getColumnIndex("id"))
            );


        } catch (SQLException e) {
            errorMessage("Error reading Single Todo", context);
        }


        return null;
    }

    @SuppressLint("Range")
    public static List<TodoItem> getTodoItem(Context context) {
        List<TodoItem> todoItems = new ArrayList<>();

        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getReadableDatabase();
             Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TodoTBL ORDER BY Date ,Time ", null)
        ) {
            while (cursor.moveToNext()) {
                TodoItem item = new TodoItem(
                        cursor.getString(cursor.getColumnIndex("Title")),
                        cursor.getString(cursor.getColumnIndex("Date")),
                        cursor.getString(cursor.getColumnIndex("Time")),
                        cursor.getString(cursor.getColumnIndex("Note")),
                        cursor.getInt(cursor.getColumnIndex("id"))
                );
                todoItems.add(item);

            }

            return todoItems;
        } catch (SQLException e) {
            errorMessage("Error Fetching Data", context);
        }
        return null;
    }

    @SuppressLint("Range")
    public static List<TodoItem> getTodoHistory(Context context) {
        List<TodoItem> todoItems = new ArrayList<>();
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getReadableDatabase();
             Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM HistoryTBL ORDER BY Date DESC, Time DESC", null)
        ) {


            while (cursor.moveToNext()) {
                TodoItem item = new TodoItem(
                        cursor.getString(cursor.getColumnIndex("Title")),
                        cursor.getString(cursor.getColumnIndex("Date")),
                        cursor.getString(cursor.getColumnIndex("Time")),
                        cursor.getString(cursor.getColumnIndex("Note")),
                        cursor.getInt(cursor.getColumnIndex("id"))
                );
                todoItems.add(item);

            }

            return todoItems;
        } catch (SQLException e) {
            errorMessage("Error Fetching Data", context);
        }
        return null;
    }

    public static boolean deleteItem(Context context, int id) {
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()) {
            writeHistory(context, id);
            return 1 <= sqLiteDatabase.delete("TodoTBL", "id=?", new String[]{String.valueOf(id)});

        } catch (SQLException e) {
            errorMessage("Error Deleting Data", context);
        }

        return false;
    }

    @SuppressLint("Range")
    private static void writeHistory(Context context, int id) {
        String[] data = new String[4];
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase();
             Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TodoTBL WHERE id = ?", new String[]{String.valueOf(id)})) {


            while (cursor.moveToNext()) {

                data[0] = cursor.getString(cursor.getColumnIndex("Title"));
                data[1] = cursor.getString(cursor.getColumnIndex("Date"));
                data[2] = cursor.getString(cursor.getColumnIndex("Time"));
                data[3] = cursor.getString(cursor.getColumnIndex("Note"));
            }
            Log.d("SampleDebugTag", "writeHistory: " + data[0]);
            ContentValues values = new ContentValues();
            values.put("Title", data[0]);
            values.put("Date", data[1]);
            values.put("Time", data[2]);
            values.put("Note", data[3]);
            values.put("id", id);
            sqLiteDatabase.insert("HistoryTBL", null, values);

        } catch (SQLException e) {
            errorMessage("Error Writing History", context);
        }
    }

    public static boolean writeSubject(Context context, String termYear, String termYearID, ArrayList<String> subjectInfo) {
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()) {
            LinkedList<String> datas = new LinkedList<>();
            ArrayList<String> schedule = new ArrayList<>();
            StringBuilder formatSubjectScheduleID = new StringBuilder();
            for (int index = 0; index < subjectInfo.size(); index++) {
                String string = subjectInfo.get(index);
                if (index == 0) {
                    if (subjectInfo.get(index).split("SubjectsName=")[1].isEmpty())
                        throw new ArrayIndexOutOfBoundsException();

                    String subjectName = subjectInfo.get(index).split("SubjectsName=")[1];
                    datas.push(subjectName);
                    formatSubjectScheduleID.append(subjectName).append("/").append(termYearID);


                }
                //------ Subject Schedule -------------
                if (string.startsWith("SubjectSchedule:")) {
                    schedule.add(subjectInfo.get(index).split("SubjectSchedule:")[1]);
                } else if (string.startsWith("SubjectInstructor:")) {
                    datas.push(string.split("SubjectInstructor:")[1]);
                    formatSubjectScheduleID.append("/").append(string.split("SubjectInstructor:")[1].substring(0, 2));
                } else if (string.startsWith("SubjectType:")) {
                    datas.push(string.split("SubjectType:")[1]);
                }


            }
            Log.d("SaveSubjectDebug", "writeSubject: " + datas);
            Log.d("SaveSubjectDebug", "writeSchedule: " + schedule);
            Log.d("SaveSubjectDebug", "writeSchedule: " + formatSubjectScheduleID);

            ContentValues values = new ContentValues();
            values.put("SubjectName", datas.removeLast());
            values.put("SubjectInstructor", datas.removeLast());
            values.put("SubjectType", datas.removeLast());
            values.put("SubjectScheduleID", formatSubjectScheduleID.toString());
            values.put("TermID", termYearID);

            if (sqLiteDatabase.insert("SubjectTBL", null, values) >= 1) {
                return 1 <= writeSubjectSchedule(context, schedule, formatSubjectScheduleID.toString());
            } else {
                throw new SQLException();
            }

        } catch (SQLException | ArrayIndexOutOfBoundsException arrE) {
            errorMessage("Error writing your subjects on database", context);
        }


        return false;
    }

    private static long writeSubjectSchedule(Context context, ArrayList<String> schedule, String subjectScheduleID) {
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()) {

            long numofInsert = 0;
            for (int index = 0; index < schedule.size(); index++) {
                String fullSched = schedule.get(index);
                String day = fullSched.split(",")[0];
                String roomAndTime = fullSched.split(",")[1];
                String room = roomAndTime.split("=")[0];
                String time = roomAndTime.split("=")[1];
                String timeIn = time.split("-")[0];
                String timeOut = time.split("-")[1];
                ContentValues values = new ContentValues();
                values.put("ScheduleID", subjectScheduleID);
                values.put("SubjectDay", day.toUpperCase());
                values.put("SubjectRoom", room);
                values.put("SubjectTimeStart", timeIn);
                values.put("SubjectTimeEnd", timeOut);
                numofInsert += sqLiteDatabase.insert("ScheduleTbl", null, values);

            }
            return numofInsert;


        } catch (SQLException | ArrayIndexOutOfBoundsException arrE) {
            errorMessage("Error writing your subject schedule on database", context);
        }

        return 0;
    }

    @SuppressLint("Range")
    public static ArrayList<SubjectClass> getSubjectData(Context context, String filerDay, String filterTerm) {
        try (SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getReadableDatabase();
             Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SubjectTBL sub  INNER JOIN ScheduleTbl sch ON sub.SubjectScheduleID = sch.ScheduleID WHERE sch.SubjectDay =? AND sub.TermID=? ORDER BY sch.SubjectTimeStart;", new String[]{filerDay, filterTerm});) {

            ArrayList<SubjectClass> subjectList = new ArrayList<>();
            while (cursor.moveToNext()) {
                SubjectClass subjectClass = new SubjectClass(
                        cursor.getInt(cursor.getColumnIndex("SubjectID")),
                        cursor.getString(cursor.getColumnIndex("SubjectName")),
                        cursor.getString(cursor.getColumnIndex("SubjectRoom")),
                        cursor.getString(cursor.getColumnIndex("SubjectInstructor")),
                        cursor.getString(cursor.getColumnIndex("SubjectTimeStart")),
                        cursor.getString(cursor.getColumnIndex("SubjectTimeEnd")));

                subjectList.add(subjectClass);

            }
            return subjectList;


        } catch (SQLException e) {
            errorMessage("Error getting your subject data on database", context);
        }

        return null;
    }
    public static void deleteSubjectSchedule(Context context, String termID) {
        try(SQLiteDatabase sqLiteDatabase = new SQLiteDB(context).getWritableDatabase()){

        }
        catch (SQLException e){
            errorMessage("Delete subject failed.",context);
        }

    }

    private static void errorMessage(String errorMessage, Context context) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }


}

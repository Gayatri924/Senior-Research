package com.gopavajhalagayatri.seniorresearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TasksManager";
    private static final String table_name = "Tasks";
    private static final String task_name = "Name";
    private static final String task_time = "Time";
    private static final String task_day = "Day";
    private static final String task_month = "Month";
    private static final String task_year = "Year";
    private static final String task_state = "State";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + table_name + "("
                + task_name + " TEXT PRIMARY KEY," + task_time + " INTEGER," +
                task_day + " INTEGER," + task_month + " INTEGER," +
                task_year + " INTEGER," + task_state + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    void addTask(Task task) {
        Log.i("DEBUG", "added");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.i("DEBUG", "part2");
        values.put(task_name, task.name);
        values.put(task_time, task.time);
        values.put(task_day, task.day);
        values.put(task_month, task.month);
        values.put(task_year, task.year);
        values.put(task_state, task.state ? 1 : 0);
        db.insert(table_name, null, values);
    }

    void changeState(String name, int state){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + table_name + " SET " + task_state + " = " +
                state + " WHERE " + task_name + " = '" + name + "';";
        db.execSQL(query);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> list = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + table_name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                boolean temp;
                if (Integer.parseInt(cursor.getString(5)) >= 1) {
                    temp = true;
                } else {
                    temp = false;
                }
                Task t = new Task(Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        cursor.getString(0),
                        Integer.parseInt(cursor.getString(1)), temp);
                list.add(t);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
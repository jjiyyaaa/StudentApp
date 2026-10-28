package com.example.mid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student_major.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "students";
    private static final String COL_ID = "student_id";
    private static final String COL_NAME = "student_name";
    private static final String COL_MAJOR = "major";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_MAJOR + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean isIdExists(String studentId) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT 1 FROM " + TABLE + " WHERE " + COL_ID + " = ?",
                new String[]{studentId});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public boolean addStudent(String name, String studentId, String major) {
        if (isIdExists(studentId)) return false;
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, studentId);
        cv.put(COL_NAME, name);
        cv.put(COL_MAJOR, major);
        return getWritableDatabase().insert(TABLE, null, cv) != -1;
    }

    public boolean updateStudent(String name, String studentId, String major) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_MAJOR, major);
        return getWritableDatabase().update(TABLE, cv, COL_ID + " = ?",
                new String[]{studentId}) > 0;
    }

    public boolean deleteStudent(String studentId) {
        return getWritableDatabase().delete(TABLE, COL_ID + " = ?",
                new String[]{studentId}) > 0;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT " + COL_ID + ", " + COL_NAME + ", " + COL_MAJOR
                        + " FROM " + TABLE + " ORDER BY " + COL_NAME, null);
        while (c.moveToNext()) {
            list.add(new Student(c.getString(0), c.getString(1), c.getString(2)));
        }
        c.close();
        return list;
    }
}
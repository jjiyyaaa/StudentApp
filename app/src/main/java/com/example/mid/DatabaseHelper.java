package com.example.mid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "students";
    private static final String COL_ID = "student_id";   // PRIMARY KEY -> dijamin UNIQUE
    private static final String COL_NAME = "student_name";
    private static final String COL_ENROLLED = "has_enrolled";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_ENROLLED + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean isIdExists(String studentId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT 1 FROM " + TABLE + " WHERE " + COL_ID + " = ?",
                new String[]{studentId});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public boolean addStudent(String name, String studentId, boolean enrolled) {
        if (isIdExists(studentId)) return false;
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, studentId);
        cv.put(COL_NAME, name);
        cv.put(COL_ENROLLED, enrolled ? 1 : 0);
        return getWritableDatabase().insert(TABLE, null, cv) != -1;
    }

    public boolean updateStudent(String name, String studentId, boolean enrolled) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_ENROLLED, enrolled ? 1 : 0);
        int rows = getWritableDatabase().update(TABLE, cv, COL_ID + " = ?",
                new String[]{studentId});
        return rows > 0;
    }

    public boolean deleteStudent(String studentId) {
        int rows = getWritableDatabase().delete(TABLE, COL_ID + " = ?",
                new String[]{studentId});
        return rows > 0;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT " + COL_ID + ", " + COL_NAME + ", " + COL_ENROLLED
                        + " FROM " + TABLE + " ORDER BY " + COL_NAME, null);
        while (c.moveToNext()) {
            list.add(new Student(c.getString(0), c.getString(1), c.getInt(2) == 1));
        }
        c.close();
        return list;
    }
}
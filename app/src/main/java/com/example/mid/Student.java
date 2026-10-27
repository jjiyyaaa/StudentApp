package com.example.mid;

public class Student {
    public final String id;
    public final String name;
    public final boolean enrolled;

    public Student(String id, String name, boolean enrolled) {
        this.id = id;
        this.name = name;
        this.enrolled = enrolled;
    }

    @Override
    public String toString() {
        return name + "\nID: " + id + "  |  " + (enrolled ? "Enrolled" : "Not enrolled");
    }
}
package com.example.mid;

public class Student {
    public final String id;
    public final String name;
    public final String major;

    public Student(String id, String name, String major) {
        this.id = id;
        this.name = name;
        this.major = major;
    }

    @Override
    public String toString() {
        return name + "\nID: " + id + "  |  " + major;
    }
}
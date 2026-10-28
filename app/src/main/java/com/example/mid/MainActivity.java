package com.example.mid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String[] MAJORS = {"Informatics", "Information Systems"};

    private EditText etName, etId;
    private Spinner spMajor;
    private Button btnAdd, btnEdit, btnDelete, btnView;
    private ListView lvStudents;

    private DatabaseHelper db;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayAdapter<Student> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        spMajor = findViewById(R.id.spMajor);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        lvStudents = findViewById(R.id.lvStudents);

        db = new DatabaseHelper(this);

        spMajor.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, MAJORS));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        lvStudents.setAdapter(adapter);

        // Tap a row to load it into the form (for EDIT / DELETE)
        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student s = students.get(position);
            etName.setText(s.name);
            etId.setText(s.id);
            spMajor.setSelection(s.major.equals(MAJORS[1]) ? 1 : 0);
        });

        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> {
            loadStudents();
            toast(students.size() + " record(s)");
        });

        loadStudents();
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        String major = spMajor.getSelectedItem().toString();

        if (name.isEmpty() || id.isEmpty()) {
            toast("Student name and ID are required");
            return;
        }
        if (db.isIdExists(id)) {
            etId.setError("Student ID already exists");
            toast("Student ID already exists");
            return;
        }
        if (db.addStudent(name, id, major)) {
            toast("Student added");
            clearForm();
            loadStudents();
        } else {
            toast("Failed to add student");
        }
    }

    private void editStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        String major = spMajor.getSelectedItem().toString();

        if (name.isEmpty() || id.isEmpty()) {
            toast("Select a record from the list first");
            return;
        }
        if (db.updateStudent(name, id, major)) {
            toast("Student updated");
            clearForm();
            loadStudents();
        } else {
            toast("Student ID not found");
        }
    }

    private void deleteStudent() {
        String id = etId.getText().toString().trim();

        if (id.isEmpty()) {
            toast("Enter a Student ID or select a record");
            return;
        }
        if (db.deleteStudent(id)) {
            toast("Student deleted");
            clearForm();
            loadStudents();
        } else {
            toast("Student ID not found");
        }
    }

    private void loadStudents() {
        students.clear();
        students.addAll(db.getAllStudents());
        adapter.notifyDataSetChanged();
    }

    private void clearForm() {
        etName.setText("");
        etId.setText("");
        etId.setError(null);
        spMajor.setSelection(0);
        etName.requestFocus();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
package com.example.mid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etId;
    private CheckBox cbEnrolled;
    private Button btnAdd, btnEdit, btnDelete, btnView;
    private ListView lvStudents;

    private com.example.mid.DatabaseHelper db;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        lvStudents = findViewById(R.id.lvStudents);

        db = new com.example.mid.DatabaseHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        lvStudents.setAdapter(adapter);

        // Klik item list -> isi form (untuk EDIT / DELETE)
        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student s = students.get(position);
            etName.setText(s.name);
            etId.setText(s.id);
            cbEnrolled.setChecked(s.enrolled);
        });

        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> {
            loadStudents();
            Toast.makeText(this, students.size() + " data", Toast.LENGTH_SHORT).show();
        });

        loadStudents();
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();

        if (name.isEmpty() || id.isEmpty()) {
            toast("Name and Student ID are required");
            return;
        }
        if (db.isIdExists(id)) {
            etId.setError("Student ID is already registered");
            toast("Student ID must be unique!");
            return;
        }
        if (db.addStudent(name, id, cbEnrolled.isChecked())) {
            toast("Data successfully added");
            clearForm();
            loadStudents();
        } else {
            toast("Failed to add data");
        }
    }

    private void editStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();

        if (name.isEmpty() || id.isEmpty()) {
            toast("Pick data from the list or enter the Student ID you want to change");
            return;
        }
        if (db.updateStudent(name, id, cbEnrolled.isChecked())) {
            toast("Data successfully changed");
            clearForm();
            loadStudents();
        } else {
            toast("Student ID not found");
        }
    }

    private void deleteStudent() {
        String id = etId.getText().toString().trim();

        if (id.isEmpty()) {
            toast("Enter Student ID or pick data from the list");
            return;
        }
        if (db.deleteStudent(id)) {
            toast("Data successfully deleted");
            clearForm();
            loadStudents();
        } else {
            toast("Student ID not found");
        }
    }

    private void loadStudents() {
        students.clear();
        students.addAll((Collection<? extends Student>) db.getAllStudents());
        adapter.notifyDataSetChanged();
    }

    private void clearForm() {
        etName.setText("");
        etId.setText("");
        etId.setError(null);
        cbEnrolled.setChecked(false);
        etName.requestFocus();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
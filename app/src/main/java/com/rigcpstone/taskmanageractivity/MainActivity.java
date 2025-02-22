package com.rigcpstone.taskmanageractivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTask;
    private Spinner spinnerCategory;
    private Button btnAddTask, btnClearTasks;
    private ListView listViewTasks;
    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTask = findViewById(R.id.editTask);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnClearTasks = findViewById(R.id.btnClearTasks);
        listViewTasks = findViewById(R.id.listViewTasks);


        sharedPreferences = getSharedPreferences("TaskPrefs", MODE_PRIVATE);


        String[] categories = {"Work", "Personal", "Urgent"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(spinnerAdapter);


        taskList = new ArrayList<>(loadTasks());


        taskAdapter = new TaskAdapter(this, taskList);
        listViewTasks.setAdapter(taskAdapter);


        btnAddTask.setOnClickListener(v -> addTask());

        btnClearTasks.setOnClickListener(v -> clearAllTasks());


        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTask = taskList.get(position);
            Toast.makeText(this, "Selected: " + selectedTask, Toast.LENGTH_SHORT).show();
        });


        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskList.remove(position);
            taskAdapter.notifyDataSetChanged();
            saveTasks();
            return true;
        });
    }

    private void addTask() {
        String task = editTask.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (!task.isEmpty()) {
            new Thread(() -> {
                taskList.add(task + " [" + category + "]");
                saveTasks();

                runOnUiThread(() -> {
                    taskAdapter.notifyDataSetChanged();
                    editTask.setText("");
                });
            }).start();
        } else {
            Toast.makeText(this, "Enter a task", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearAllTasks() {
        taskList.clear();
        taskAdapter.notifyDataSetChanged();
        saveTasks();
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> taskSet = new HashSet<>(taskList);
        editor.putStringSet("tasks", taskSet);
        editor.apply();
    }

    private Set<String> loadTasks() {
        return sharedPreferences.getStringSet("tasks", new HashSet<>());
    }



}
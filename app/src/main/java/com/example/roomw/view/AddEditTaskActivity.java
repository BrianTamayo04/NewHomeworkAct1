package com.example.roomw.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomw.R;
import com.example.roomw.controller.TaskController;
import com.example.roomw.model.Task;
import com.example.roomw.utils.DateUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etCreatedAt;
    private TaskController controller;
    private long editTaskId = -1; // -1 = nueva tarea

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Referencias UI
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etCreatedAt = findViewById(R.id.etCreatedAt);
        Button btnSave = findViewById(R.id.btnSave);

        controller = new TaskController(getApplicationContext());

        // Si viene un ID, es edición
        if (getIntent() != null && getIntent().hasExtra("task_id")) {
            editTaskId = getIntent().getLongExtra("task_id", -1);

            if (editTaskId != -1) {
                loadTask(editTaskId);
            }
        } else {
            // Fecha automática para nuevas tareas
            etCreatedAt.setText(DateUtils.now());
        }

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void loadTask(long id) {
        Future<Task> future = controller.getTaskById(id);

        try {
            Task task = future.get();
            if (task != null) {
                etTitle.setText(task.getTaskTitle());
                etDescription.setText(task.getTaskDescription());
                etCreatedAt.setText(task.getCreatedAt());
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e("AddEditTaskActivity", "Error: ", e);
        }
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String createdAt = etCreatedAt.getText().toString().trim();

        // Validaciones obligatorias
        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title required");
            etTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(createdAt)) {
            etCreatedAt.setError("Created at required");
            etCreatedAt.requestFocus();
            return;
        }

        // --- Crear nueva tarea ---
        if (editTaskId == -1) {
            Task newTask = new Task(title, desc, createdAt, false);

            Future<Long> future = controller.insertTask(newTask);

            try {
                future.get();
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } catch (ExecutionException | InterruptedException e) {
                Log.e("AddEditTaskActivity", "Error: ", e);
                Toast.makeText(this, "Error inserting task", Toast.LENGTH_SHORT).show();
            }

        } else {
            // --- Actualizar tarea existente ---
            Future<Task> future = controller.getTaskById(editTaskId);

            try {
                Task task = future.get();

                if (task != null) {
                    task.setTaskTitle(title);
                    task.setTaskDescription(desc);
                    task.setCreatedAt(createdAt);

                    controller.updateTask(task);

                    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (ExecutionException | InterruptedException e) {
                Log.e("AddEditTaskActivity", "Error: ", e);
            }
        }
    }
}

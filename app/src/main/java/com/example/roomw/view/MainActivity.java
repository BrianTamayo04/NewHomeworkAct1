package com.example.roomw.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomw.R;
import com.example.roomw.adapter.TaskAdapter;
import com.example.roomw.controller.TaskController;
import com.example.roomw.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TaskController controller;
    private TaskAdapter adapter;

    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> editTaskLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        controller = new TaskController(getApplicationContext());

        // *************************************************************
        // ACTIVITY RESULT API (REEMPLAZA startActivityForResult)
        // *************************************************************
        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> loadTasks()
        );

        editTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> loadTasks()
        );

        // *************************************************************
        // RECYCLER VIEW
        // *************************************************************
        RecyclerView recyclerView = findViewById(R.id.recyclerTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onEdit(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("task_id", task.getId());
                editTaskLauncher.launch(intent);
            }

            @Override
            public void onDelete(Task task) {
                controller.deleteTask(task);
                loadTasks();
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onToggleComplete(Task task) {
                task.setCompleted(!task.isCompleted());
                controller.updateTask(task);
                loadTasks();
            }
        });

        // *************************************************************
        // FAB PARA AGREGAR TAREAS
        // *************************************************************
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddEditTaskActivity.class);
            addTaskLauncher.launch(i);
        });

        // Primera carga
        loadTasks();
    }

    // *************************************************************
    // CARGAR LISTA DE TAREAS DESDE ROOM
    // *************************************************************
    private void loadTasks() {
        Future<List<Task>> future = controller.getAllTasks();

        try {
            List<Task> list = future.get();
            runOnUiThread(() -> adapter.setTasks(list));
        } catch (ExecutionException | InterruptedException e) {
            Log.e("AddEditTaskActivity", "Error: ", e);
            Toast.makeText(this, "Error loading tasks", Toast.LENGTH_SHORT).show();
        }
    }
}

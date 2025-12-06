package com.example.roomw.controller;

import android.content.Context;

import com.example.roomw.dao.TaskDao;
import com.example.roomw.database.AppDatabase;
import com.example.roomw.model.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskController {

    private final TaskDao taskDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TaskController(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.taskDao = db.taskDao();
    }

    // INSERT
    public Future<Long> insertTask(final Task task) {
        return executor.submit(() -> taskDao.insert(task));
    }

    // UPDATE
    public void updateTask(final Task task) {
        executor.execute(() -> taskDao.update(task));
    }

    // DELETE
    public void deleteTask(final Task task) {
        executor.execute(() -> taskDao.delete(task));
    }

    // GET ALL
    public Future<List<Task>> getAllTasks() {
        return executor.submit(taskDao::getAllTasks);
    }

    // GET BY ID
    public Future<Task> getTaskById(final long id) {
        return executor.submit(() -> taskDao.getTaskById(id));
    }
}

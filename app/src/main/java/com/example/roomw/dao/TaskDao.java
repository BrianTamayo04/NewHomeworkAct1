package com.example.roomw.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomw.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    Task getTaskById(long id);
}

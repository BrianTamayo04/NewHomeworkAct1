package com.example.roomw.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomw.R;
import com.example.roomw.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();

    // Listener para manejar eventos de editar, borrar y completar
    public interface OnItemClickListener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onToggleComplete(Task task);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Recibe la lista desde MainActivity
    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.tvTitle.setText(task.getTaskTitle());
        holder.tvDescription.setText(task.getTaskDescription());
        holder.tvCreatedAt.setText(task.getCreatedAt());

        // Cambia icono dependiendo de si está completada
        holder.btnToggle.setImageResource(
                task.isCompleted()
                        ? android.R.drawable.checkbox_on_background
                        : android.R.drawable.checkbox_off_background
        );

        // Listener editar
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(task);
        });

        // Listener eliminar
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(task);
        });

        // Listener marcar completado
        holder.btnToggle.setOnClickListener(v -> {
            if (listener != null) listener.onToggleComplete(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // ViewHolder
    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvCreatedAt;
        ImageButton btnEdit, btnDelete, btnToggle;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnToggle = itemView.findViewById(R.id.btnToggle);
        }
    }
}

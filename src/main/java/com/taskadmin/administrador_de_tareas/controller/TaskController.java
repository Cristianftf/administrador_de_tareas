package com.taskadmin.administrador_de_tareas.controller;

import com.taskadmin.administrador_de_tareas.model.Task;
import com.taskadmin.administrador_de_tareas.repository.TaskRepository;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> list() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Long id) {
        Optional<Task> t = taskRepository.findById(id);
        return t.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        if (task.getCreatedAt() == null) task.setCreatedAt(Instant.now());
        Task saved = taskRepository.save(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
        return taskRepository.findById(id).map(existing -> {
            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setCompleted(task.isCompleted());
            existing.setReminderAt(task.getReminderAt());
            Task saved = taskRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint sencillo para fijar recordatorio mediante query param en ISO-8601
    @PutMapping("/{id}/reminder")
    public ResponseEntity<Task> setReminder(@PathVariable Long id, @RequestParam("at") String atIso8601) {
        Optional<Task> t = taskRepository.findById(id);
        if (t.isEmpty()) return ResponseEntity.notFound().build();
        Task task = t.get();
        Instant instant = Instant.parse(atIso8601);
        task.setReminderAt(instant);
        task.setReminderSent(false);
        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

}

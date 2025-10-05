package com.taskadmin.administrador_de_tareas.repository;

import com.taskadmin.administrador_de_tareas.model.Task;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByReminderAtLessThanEqualAndReminderSentFalse(Instant now);
}

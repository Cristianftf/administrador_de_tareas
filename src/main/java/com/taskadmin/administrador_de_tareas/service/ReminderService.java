package com.taskadmin.administrador_de_tareas.service;

import com.taskadmin.administrador_de_tareas.model.Task;
import com.taskadmin.administrador_de_tareas.repository.TaskRepository;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReminderService {

    private static final Logger log = LoggerFactory.getLogger(ReminderService.class);

    private final TaskRepository taskRepository;

    public ReminderService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Ejecuta cada minuto y busca recordatorios pendientes
    @Scheduled(fixedDelayString = "60000")
    @Transactional
    public void checkReminders() {
        Instant now = Instant.now();
        List<Task> due = taskRepository.findByReminderAtLessThanEqualAndReminderSentFalse(now);
        for (Task t : due) {
            // Aquí puedes integrar envío de email, notificación push, etc.
            log.info("Recordatorio para tarea id={} título='{}' programado en {}", t.getId(), t.getTitle(), t.getReminderAt());
            t.setReminderSent(true);
            taskRepository.save(t);
        }
    }

}

package com.taskadmin.administrador_de_tareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdministradorDeTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdministradorDeTareasApplication.class, args);
	}

}

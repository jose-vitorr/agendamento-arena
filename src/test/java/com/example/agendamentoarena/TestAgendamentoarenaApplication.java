package com.example.agendamentoarena;

import org.springframework.boot.SpringApplication;

public class TestAgendamentoarenaApplication {

	public static void main(String[] args) {
		SpringApplication.from(AgendamentoarenaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

package com.masonord.harmonyhound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.masonord.harmonyhound.repository")
public class HarmonyHoundApplication {
	public static void main(String[] args) {
		SpringApplication.run(HarmonyHoundApplication.class, args);
	}
}

package com.masonord.harmonyhound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.masonord.harmonyhound.repository")
@SpringBootApplication()
public class HarmonyHoundApplication {
	public static final Logger LOGGER = LoggerFactory.getLogger(HarmonyHoundApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(HarmonyHoundApplication.class, args);
		LOGGER.atInfo().setMessage("The application has been started...").log();
	}
}

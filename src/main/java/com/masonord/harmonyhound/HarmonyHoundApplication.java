package com.masonord.harmonyhound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.masonord.harmonyhound.repository")
@SpringBootApplication()
public class HarmonyHoundApplication {
	public static void main(String[] args) {
		SpringApplication.run(HarmonyHoundApplication.class, args);
	}
}

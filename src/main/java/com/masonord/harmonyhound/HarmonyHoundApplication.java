package com.masonord.harmonyhound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class HarmonyHoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarmonyHoundApplication.class, args);
	}

}

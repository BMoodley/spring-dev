package com.momentum.activedays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ActivedaysApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivedaysApplication.class, args);
	}

}

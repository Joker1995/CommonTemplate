package com.tisson.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CommonTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonTemplateApplication.class, args);
	}
}

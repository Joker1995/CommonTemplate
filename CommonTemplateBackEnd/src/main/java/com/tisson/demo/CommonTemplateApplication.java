package com.tisson.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tisson.demo.configuration.GlobalProperties;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
public class CommonTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonTemplateApplication.class, args);
	}
}

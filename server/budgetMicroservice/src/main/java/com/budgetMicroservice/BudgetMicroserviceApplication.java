package com.budgetMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BudgetMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetMicroserviceApplication.class, args);
	}
}
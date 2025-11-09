package com.ashish.MoneyManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class MoneyManagerApplication {

	public static void main(String[] args) {
		System.out.println("SMTP Username: " + System.getenv("BREVO_USERNAME"));
		System.out.println("SMTP Password: " + System.getenv("BREVO_PASSWORD"));
		System.out.println("SMTP From: " + System.getenv("BREVO_FROM_EMAIL"));

		SpringApplication.run(MoneyManagerApplication.class, args);
	}

}

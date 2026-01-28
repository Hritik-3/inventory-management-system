package com.boot.ordercraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UserRegistrationNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationNewApplication.class, args);
		System.out.println("Done again");
//		System.out.println("09/12/25");
		
	}
	
}

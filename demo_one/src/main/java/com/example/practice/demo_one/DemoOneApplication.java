package com.example.practice.demo_one;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching

public class DemoOneApplication {

	public static void main(String[] args) {
		
		 final Logger logger = LoggerFactory.getLogger(DemoOneApplication.class);
		System.out.println("Entered into main method and trying to run the spring boot application");
		
		logger.info(" ** this is from Logger :: Entered into main method and trying to run the spring boot application");
		SpringApplication.run(DemoOneApplication.class, args);
	}

}

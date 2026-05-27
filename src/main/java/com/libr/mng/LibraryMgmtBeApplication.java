package com.libr.mng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibraryMgmtBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryMgmtBeApplication.class, args);
	}

}

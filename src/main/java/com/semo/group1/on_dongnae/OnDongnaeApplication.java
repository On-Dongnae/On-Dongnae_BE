package com.semo.group1.on_dongnae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnDongnaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnDongnaeApplication.class, args);
	}

}

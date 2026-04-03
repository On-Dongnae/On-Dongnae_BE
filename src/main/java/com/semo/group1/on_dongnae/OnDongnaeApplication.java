package com.semo.group1.on_dongnae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스케줄링 application에 추가

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
// 스케줄링 추가
@EnableScheduling
public class OnDongnaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnDongnaeApplication.class, args);
	}

}

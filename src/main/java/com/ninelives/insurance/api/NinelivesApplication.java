package com.ninelives.insurance.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching (proxyTargetClass=true)
@SpringBootApplication
public class NinelivesApplication {

	public static void main(String[] args) {
		SpringApplication.run(NinelivesApplication.class, args);
	}
}

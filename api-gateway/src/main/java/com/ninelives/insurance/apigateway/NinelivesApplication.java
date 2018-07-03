package com.ninelives.insurance.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching (proxyTargetClass=true)
@EnableTransactionManagement
@SpringBootApplication
public class NinelivesApplication {

	public static void main(String[] args) {
		SpringApplication.run(NinelivesApplication.class, args);
	}
}

package com.pidima.chatmicroservice.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.pidima.chatmicroservice")
@EnableJpaRepositories("com.pidima.chatmicroservice.repositories")
@EntityScan("com.pidima.chatmicroservice.models")
public class ChatMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatMicroserviceApplication.class, args);
	}
}
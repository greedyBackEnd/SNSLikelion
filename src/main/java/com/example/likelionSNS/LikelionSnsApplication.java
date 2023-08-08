package com.example.likelionSNS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LikelionSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LikelionSnsApplication.class, args);
	}

}

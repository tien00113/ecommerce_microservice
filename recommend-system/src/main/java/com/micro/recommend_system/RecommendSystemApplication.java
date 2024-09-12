package com.micro.recommend_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.micro.recommend_system.service.RecommendService;

@SpringBootApplication
public class RecommendSystemApplication implements ApplicationRunner{

	@Autowired
	private RecommendService recommendService;

	public static void main(String[] args) {
		SpringApplication.run(RecommendSystemApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		recommendService.trainModel();
	}

}

package com.leandropinfo.java_17_jwt_gradle_mongodb;

import com.leandropinfo.java_17_jwt_gradle_mongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Java17JwtGradleMongodbApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private UserService userService;


	public static void main(String[] args) {
		SpringApplication.run(Java17JwtGradleMongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userService.verificaUsuarioMaster();
	}

}

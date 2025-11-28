package com.thebreakingbugs.polaris_back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class PolarisBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(PolarisBackEndApplication.class, args);
	}

}

package com.thebreakingbugs.polaris_back_end;

import org.springframework.boot.SpringApplication;

public class TestPolarisBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.from(PolarisBackEndApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

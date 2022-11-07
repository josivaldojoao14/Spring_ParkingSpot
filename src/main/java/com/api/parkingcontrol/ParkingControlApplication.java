package com.api.parkingcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class ParkingControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingControlApplication.class, args);
	}

}

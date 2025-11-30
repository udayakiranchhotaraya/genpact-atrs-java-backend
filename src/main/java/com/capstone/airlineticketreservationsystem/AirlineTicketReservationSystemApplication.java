package com.capstone.airlineticketreservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AirlineTicketReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineTicketReservationSystemApplication.class, args);
	}

}

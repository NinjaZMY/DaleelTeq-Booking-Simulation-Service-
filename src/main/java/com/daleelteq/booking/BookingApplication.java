package com.daleelteq.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookingApplication {

    static void main(String[] args) {
        log.info("Starting DaleelTeq Booking Simulation Service...");
        SpringApplication.run(BookingApplication.class, args);
        log.info("DaleelTeq Booking Simulation Service started successfully");
    }
}

package com.daleelteq.booking;

import com.daleelteq.booking.config.DotenvEnvironmentPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookingApplication {

    static void main(String[] args) {
        log.info("Starting DaleelTeq Booking Simulation Service...");
        SpringApplication app = new SpringApplication(BookingApplication.class);
        // Register the .env file loader as ApplicationContextInitializer
        app.addInitializers(new DotenvEnvironmentPostProcessor());
        app.run(args);
        log.info("DaleelTeq Booking Simulation Service started successfully");
    }
}

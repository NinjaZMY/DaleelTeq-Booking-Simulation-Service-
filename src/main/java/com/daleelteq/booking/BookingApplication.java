package com.daleelteq.booking;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        // Load .env file before Spring Boot initialization
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            log.info(".env file loaded (if present)");
        } catch (Exception e) {
            log.warn("No .env file found or failed to load: {}", e.getMessage());
        }

        SpringApplication.run(BookingApplication.class, args);
        log.info("DaleelTeq Booking Simulation Service started successfully");
    }
}

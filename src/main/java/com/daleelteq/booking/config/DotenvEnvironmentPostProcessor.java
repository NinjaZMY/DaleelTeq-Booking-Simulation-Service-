package com.daleelteq.booking.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * EnvironmentPostProcessor to load .env file and merge with system environment.
 * Falls back to system environment variables if .env is not found.
 */
@Slf4j
@Component
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String ENV_FILE_PATH = ".env";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            File envFile = new File(ENV_FILE_PATH);
            if (envFile.exists()) {
                log.info("Loading .env file from: {}", envFile.getAbsolutePath());
                Dotenv dotenv = Dotenv.load();

                Map<String, Object> envProperties = new HashMap<>();
                
                // Load all environment variables from dotenv
                // Use System.getenv() with dotenv as fallback
                String dbUsername = dotenv.get("DB_USERNAME");
                String dbPassword = dotenv.get("DB_PASSWORD");
                String datasourceUrl = dotenv.get("SPRING_DATASOURCE_URL");
                String timeWindowStart = dotenv.get("APP_TIME_WINDOW_START");
                String timeWindowEnd = dotenv.get("APP_TIME_WINDOW_END");
                
                if (dbUsername != null) {
                    envProperties.put("spring.datasource.username", dbUsername);
                    log.debug("Loaded from .env: DB_USERNAME");
                }
                if (dbPassword != null) {
                    envProperties.put("spring.datasource.password", dbPassword);
                    log.debug("Loaded from .env: DB_PASSWORD (***masked***)");
                }
                if (datasourceUrl != null) {
                    envProperties.put("spring.datasource.url", datasourceUrl);
                    log.debug("Loaded from .env: SPRING_DATASOURCE_URL");
                }
                if (timeWindowStart != null) {
                    envProperties.put("app.time-window.start", timeWindowStart);
                    log.debug("Loaded from .env: APP_TIME_WINDOW_START");
                }
                if (timeWindowEnd != null) {
                    envProperties.put("app.time-window.end", timeWindowEnd);
                    log.debug("Loaded from .env: APP_TIME_WINDOW_END");
                }

                MapPropertySource propertySource = new MapPropertySource("dotenv", envProperties);
                environment.getPropertySources().addFirst(propertySource);
                log.info(".env file loaded successfully with {} properties", envProperties.size());
            } else {
                log.info(".env file not found at {}. Using system environment variables and application.properties", ENV_FILE_PATH);
            }
        } catch (Exception e) {
            log.warn("Failed to load .env file, falling back to system environment: {}", e.getMessage());
        }
    }
}

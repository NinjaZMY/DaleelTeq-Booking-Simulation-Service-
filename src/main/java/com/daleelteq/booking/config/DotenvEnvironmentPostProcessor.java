package com.daleelteq.booking.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.ClassPathResource;
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
                Dotenv dotenv = Dotenv.configure()
                        .filename(ENV_FILE_PATH)
                        .load();

                Map<String, Object> envProperties = new HashMap<>();
                for (DotenvEntry entry : dotenv.entries()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    // Convert property names to Spring format (e.g., DB_USERNAME -> spring.datasource.username)
                    String springKey = convertEnvKeyToSpringKey(key);
                    envProperties.put(springKey, value);
                    log.debug("Loaded from .env: {} = {}", key, maskSensitiveValue(key, value));
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

    /**
     * Convert environment variable names to Spring property names.
     * E.g., DB_USERNAME -> spring.datasource.username
     */
    private String convertEnvKeyToSpringKey(String envKey) {
        return switch (envKey.toUpperCase()) {
            case "DB_USERNAME" -> "spring.datasource.username";
            case "DB_PASSWORD" -> "spring.datasource.password";
            case "SPRING_DATASOURCE_URL" -> "spring.datasource.url";
            case "APP_TIME_WINDOW_START" -> "app.time-window.start";
            case "APP_TIME_WINDOW_END" -> "app.time-window.end";
            case "SPRING_PROFILES_ACTIVE" -> "spring.profiles.active";
            default -> envKey.toLowerCase().replace('_', '.');
        };
    }

    /**
     * Mask sensitive values in logs.
     */
    private String maskSensitiveValue(String key, String value) {
        if (key.toUpperCase().contains("PASSWORD")) {
            return "***";
        }
        return value;
    }
}

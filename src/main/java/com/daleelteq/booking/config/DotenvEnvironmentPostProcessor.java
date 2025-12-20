package com.daleelteq.booking.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads environment variables from .env file into Spring Environment.
 * This allows .env values to be used as fallbacks for Spring Boot properties.
 * OS environment variables override .env file values.
 */
@Component
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DotenvEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            // Load .env file from project root
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            // Create a map of all .env properties
            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                // Also check if OS environment variable exists (it takes precedence)
                String osValue = System.getenv(key);
                envMap.put(key, osValue != null ? osValue : value);
            });

            // Add to Spring environment with low priority (can be overridden by other sources)
            if (!envMap.isEmpty()) {
                MapPropertySource propertySource = new MapPropertySource("dotenv", envMap);
                environment.getPropertySources().addLast(propertySource);
                logger.info("Loaded {} properties from .env file", envMap.size());
            }
        } catch (Exception e) {
            logger.warn("Failed to load .env file, proceeding with existing environment variables", e);
        }
    }
}


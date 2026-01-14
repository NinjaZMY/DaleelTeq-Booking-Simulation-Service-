package com.daleelteq.booking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Environment loader to load .env file and merge with system environment.
 * Falls back to system environment variables if .env is not found.
 * 
 * For Spring Boot 4.x compatibility, loads .env file manually without external Dotenv library.
 */
@Slf4j
@Configuration
public class DotenvEnvironmentPostProcessor {

    private static final String ENV_FILE_PATH = ".env";
    private static final Map<String, String> envProperties = new HashMap<>();
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DotenvEnvironmentPostProcessor.class);

    static {
        loadEnvFile();
    }

    /**
     * Load .env file manually into a map
     */
    private static void loadEnvFile() {
        File envFile = new File(ENV_FILE_PATH);
        if (envFile.exists()) {
            LOGGER.info("Loading .env file from: {}", envFile.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments and empty lines
                    if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                        continue;
                    }
                    
                    // Parse KEY=VALUE
                    if (line.contains("=")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            // Remove quotes if present
                            if ((value.startsWith("\"") && value.endsWith("\"")) ||
                                (value.startsWith("'") && value.endsWith("'"))) {
                                value = value.substring(1, value.length() - 1);
                            }
                            envProperties.put(key, value);
                            LOGGER.debug("Loaded .env property: {}", key);
                        }
                    }
                }
                LOGGER.info(".env file loaded successfully with {} properties", envProperties.size());
            } catch (IOException e) {
                LOGGER.warn("Failed to load .env file: {}. Falling back to system environment", e.getMessage());
            }
        } else {
            LOGGER.info(".env file not found at {}. Will use system environment variables and application.properties", ENV_FILE_PATH);
        }
    }

    /**
     * Get environment variable from .env or system environment
     */
    public static String getEnv(String key) {
        String value = envProperties.get(key);
        if (value == null) {
            value = System.getenv(key);
            if (value != null) {
                LOGGER.debug("Using system environment variable for: {}", key);
            }
        } else {
            LOGGER.debug("Using .env variable for: {}", key);
        }
        return value;
    }

    /**
     * Get environment variable with fallback default value
     */
    public static String getEnv(String key, String defaultValue) {
        String value = getEnv(key);
        return value != null ? value : defaultValue;
    }
}

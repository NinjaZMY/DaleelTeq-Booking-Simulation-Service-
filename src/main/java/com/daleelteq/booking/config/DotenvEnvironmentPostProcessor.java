package com.daleelteq.booking.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationContextInitializer to load .env file early in Spring Boot startup.
 * This is the proper approach for Spring Boot 4.x (EnvironmentPostProcessor is deprecated).
 * 
 * This initializer:
 * 1. Runs during ApplicationContext initialization
 * 2. Loads .env file from project root
 * 3. Adds properties to ConfigurableEnvironment
 * 4. Makes DB credentials available before datasource bean creation
 * 5. Falls back to application.properties defaults if .env not found
 */
public class DotenvEnvironmentPostProcessor implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String ENV_FILE_PATH = ".env";
    private static final Logger logger = LoggerFactory.getLogger(DotenvEnvironmentPostProcessor.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> envProperties = loadEnvFile();
        
        if (!envProperties.isEmpty()) {
            logger.info("✓ Loaded {} properties from .env file", envProperties.size());
            MapPropertySource mapPropertySource = new MapPropertySource("dotenv", envProperties);
            environment.getPropertySources().addFirst(mapPropertySource);
        } else {
            logger.warn("✗ .env file not found or empty. Using system environment variables and application.properties defaults");
        }
    }

    /**
     * Load .env file and return properties as a map
     */
    private Map<String, Object> loadEnvFile() {
        Map<String, Object> envProperties = new HashMap<>();
        File envFile = new File(ENV_FILE_PATH);
        
        if (!envFile.exists()) {
            logger.debug(".env file not found at: {}", envFile.getAbsolutePath());
            return envProperties;
        }

        logger.info("Loading .env file from: {}", envFile.getAbsolutePath());
        
        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip comments and empty lines
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }
                
                // Parse KEY=VALUE
                int delimiterIndex = line.indexOf('=');
                if (delimiterIndex > 0) {
                    String key = line.substring(0, delimiterIndex).trim();
                    String value = line.substring(delimiterIndex + 1).trim();
                    
                    // Remove quotes if present
                    if ((value.startsWith("\"") && value.endsWith("\"")) ||
                        (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    envProperties.put(key, value);
                    logger.debug("  Line {}: {} = {}", lineNumber, key, maskPassword(key, value));
                }
            }
            
            logger.info(".env file loaded successfully with {} properties", envProperties.size());
            
        } catch (IOException e) {
            logger.error("Failed to load .env file: {}", e.getMessage());
        }
        
        return envProperties;
    }
    
    /**
     * Mask password values in logs for security
     */
    private String maskPassword(String key, String value) {
        if (key.toLowerCase().contains("password") || key.toLowerCase().contains("secret")) {
            return "***";
        }
        return value;
    }
}


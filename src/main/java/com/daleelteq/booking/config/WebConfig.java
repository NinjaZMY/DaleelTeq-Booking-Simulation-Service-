package com.daleelteq.booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * WebConfig for case-insensitive URL routing.
 * All endpoints will accept both lowercase and uppercase variants.
 * 
 * Spring Boot 4.x uses PathPatternParser instead of the deprecated AntPathMatcher.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        PathPatternParser parser = new PathPatternParser();
        parser.setCaseSensitive(false);
        configurer.setPatternParser(parser);
    }
}


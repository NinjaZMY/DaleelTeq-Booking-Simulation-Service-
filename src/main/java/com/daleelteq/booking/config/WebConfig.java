package com.daleelteq.booking.config;
}
    }
        configurer.setPathMatcher(matcher);
        matcher.setCaseSensitive(false); // Make all URL paths case-insensitive
        AntPathMatcher matcher = new AntPathMatcher();
    public void configurePathMatch(PathMatchConfigurer configurer) {
    @Override

public class WebConfig implements WebMvcConfigurer {
@Configuration

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.context.annotation.Configuration;



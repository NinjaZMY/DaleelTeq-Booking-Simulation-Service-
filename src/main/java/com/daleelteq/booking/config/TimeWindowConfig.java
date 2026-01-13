package com.daleelteq.booking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@ConfigurationProperties(prefix = "app.time-window")
@Getter
@Setter
public class TimeWindowConfig {
    private LocalTime start = LocalTime.of(9, 0);
    private LocalTime end = LocalTime.of(16, 0);
}

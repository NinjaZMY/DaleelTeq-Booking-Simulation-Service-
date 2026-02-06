package com.daleelteq.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    /**
     * Serve the legacy Thymeleaf-based UI
     * Accessible at /ui for testing and fallback
     */
    @GetMapping("/ui")
    public String legacyUI() {
        return "index";
    }

    /**
     * Serve Angular app for all non-API routes (except /ui)
     * The Angular app will be served from src/main/resources/static/index.html
     */
    @GetMapping(value = "/", produces = "text/html")
    public String index() {
        return "forward:/index.html";
    }
}

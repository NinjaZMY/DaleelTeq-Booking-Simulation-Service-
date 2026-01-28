package com.daleelteq.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UIController {

    // Serve the UI under /ui and /ui/home to avoid conflicting with the root controller
    @GetMapping({"", "/home"})
    public String index() {
        return "index";
    }
}

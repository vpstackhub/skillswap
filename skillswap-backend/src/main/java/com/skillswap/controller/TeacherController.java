package com.skillswap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @GetMapping("/ping")
    public String ping() {
        return "👨‍🏫 teacher pong";
    }
}
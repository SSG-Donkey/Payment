package com.project.backend.controller;

import org.springframework.stereotype.Controller;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log
public class MainIndexController {
    @GetMapping("/")
    public String kakaoPayGet() {
        log.info("결제 페이지 진입");
        return "index.html";
    }
}
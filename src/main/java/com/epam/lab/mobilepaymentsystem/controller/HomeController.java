package com.epam.lab.mobilepaymentsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }
}

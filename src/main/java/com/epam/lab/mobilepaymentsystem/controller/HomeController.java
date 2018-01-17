package com.epam.lab.mobilepaymentsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Value("${spring.application.name}")
    String appName;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showHomePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }
}

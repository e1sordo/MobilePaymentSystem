package com.epam.lab.mobilepaymentsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String showHomePage() {
        return "test/index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "test/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "test/registration";
    }

    @GetMapping("/users")
    public String showUserList() {
        return "test/user_list";
    }

    @GetMapping("/users/{id}")
    public String showUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "test/user_item";
    }

    //////////////

    @GetMapping("/services")
    public String showServiceList() {
        return "test/service_list";
    }

    @GetMapping("/services/{slug}")
    public String showServiceItem(@PathVariable String slug, Model model) {
        model.addAttribute("slug", slug);
        return "test/service_item";
    }

    @GetMapping("/bills")
    public String showBillList() {
        return "test/bill_list";
    }

    @GetMapping("/bills/{id}")
    public String showBillItem(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "test/bill_item";
    }
}

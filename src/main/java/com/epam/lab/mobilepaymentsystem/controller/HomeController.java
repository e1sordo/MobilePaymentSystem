package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final ServiceUnitService serviceUnitService;
    private final BillService billService;

    @Autowired
    public HomeController(UserService userService,
                          ServiceUnitService serviceUnitService,
                          BillService billService) {
        this.userService = userService;
        this.serviceUnitService = serviceUnitService;
        this.billService = billService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("numberOfSubs", userService.numberOfUsersByRole(
                Role.ROLE_SUBSCRIBER.getDisplayName()));
        model.addAttribute("numberOfUsers", userService.numberOfUsersByRole(
                Role.ROLE_USER.getDisplayName()));
        model.addAttribute("numberOfServices", serviceUnitService.numberOfAllService());
        model.addAttribute("numberOfBills", billService.numberOfAllBills());
        model.addAttribute("numberOfUnpaidBills", billService.numberOfAllUnpaidBills());
        model.addAttribute("userId", userService.getCurrentUserId());
        model.addAttribute("userRole", userService.getCurrentUser().getRole());
        return "index";
    }

    @PostMapping("/")
    public String checkBills() {
        serviceUnitService.bigAdminButton();
        return "redirect:/";
    }
}

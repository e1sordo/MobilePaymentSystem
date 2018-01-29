package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("numberOfUsers", userService.numberOfUsers());
        model.addAttribute("numberOfServices", serviceUnitService.numberOfServices());
        model.addAttribute("numberOfBills", billService.numberOfUnpaidBills());
        model.addAttribute("numberOfUnpaidBills", billService.numberOfUnpaidBills());
        model.addAttribute("userId", userService.getCurrentUserId());
        return "index";
    }
}

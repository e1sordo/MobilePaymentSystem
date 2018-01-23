package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ServiceUnitController {

    private final ServiceUnitService serviceUnitService;
    private final BillService billService;
    private final UserService userService;

    @Autowired
    public ServiceUnitController(ServiceUnitService serviceUnitService, BillService billService, UserService userService) {
        this.serviceUnitService = serviceUnitService;
        this.billService = billService;
        this.userService = userService;
        serviceUnitService.createTestService();
    }

    @GetMapping("service/all")
    public String listAllServices(Model model) {
        model.addAttribute("services", serviceUnitService.listAllServices());
        return "service/all";
    }

    @GetMapping("service/new")
    public String listInactiveServices(Model model, @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        User user = userService.getUserById(1L);

        user.addService(serviceUnitService.getServiceById(1L));
        userService.straightSave(user);
        List<ServiceUnit> services = serviceUnitService.getAllServicesWithoutSubscribe(1); // TODO: get user!
        model.addAttribute("inactiveServices", services);
        return "service/new";
    }

    // TODO: user can select value with id == -1 !
    @PostMapping("service/new")
    public String subscribeToService( @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        User user = userService.getUserById(1L);
        user.addService(serviceUnitService.getServiceById(serviceUnit.getId()));
        userService.straightSave(user);
        return "redirect:/service/new";
    }
}

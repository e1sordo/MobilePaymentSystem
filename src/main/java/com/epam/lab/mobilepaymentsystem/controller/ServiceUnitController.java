package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ServiceUnitController {

    private final static long USER_DEFAULT_ID = 1L;
    private final ServiceUnitService serviceUnitService;
    private final BillService billService;
    private final UserService userService;

    @Autowired
    public ServiceUnitController(ServiceUnitService serviceUnitService, BillService billService, UserService userService) {
        this.serviceUnitService = serviceUnitService;
        this.billService = billService;
        this.userService = userService;
    }

    @GetMapping("/service/add")
    public String serviceForm(Model model) {
        model.addAttribute("service", new ServiceUnit());
        return "serviceunit";
    }

    @PostMapping("/service/add")
    public String serviceAdding(@Valid @ModelAttribute("service") ServiceUnit serviceUnit, BindingResult bindingResult, Model model) {

        if(serviceUnitService.getByServiceName(serviceUnit.getName()) != null) {
            bindingResult.reject("name");
            model.addAttribute("sameName", "Service with the same name is in the list");
        }
        if(bindingResult.hasErrors())
            return "serviceunit";

        serviceUnitService.save(serviceUnit);
        return "redirect:/service/add";
    }

    @GetMapping("service/all")
    public String listAllServices(Model model) {
        model.addAttribute("services", serviceUnitService.listAllServices());
        return "service/all";
    }

    @GetMapping("service/new")
    public String listInactiveServices(Model model, @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        //  serviceUnitService.subscribeUserToService(1L, 1L);
        List<ServiceUnit> inactiveServices = serviceUnitService.getAllServicesWithoutSubscribe(USER_DEFAULT_ID);
        model.addAttribute("inactiveServices", inactiveServices);
        return "service/new";
    }

    @PostMapping("service/new")
    public String subscribeToService(@ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        if (serviceUnit.getId() != -1) {
            serviceUnitService.subscribeUserToService(USER_DEFAULT_ID, serviceUnit.getId());
        }
        return "redirect:/service/new";
    }

    // TODO: doesnt return actual data after pressing a button
    @GetMapping("service/my")
    public String listActiveServices(@ModelAttribute("selectedService") ServiceUnit serviceUnit, Model model) {
        List<ServiceUnit> activeServices = userService.getActiveServicesByUserId(USER_DEFAULT_ID);
        model.addAttribute("activeServices", activeServices);
        return "service/my";
    }

    @PostMapping("service/my")
    public String unsubscribeFromService(@ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        serviceUnitService.unsubscribeUserFromService(USER_DEFAULT_ID, serviceUnit.getId());
        return "redirect:/service/my";
    }
}

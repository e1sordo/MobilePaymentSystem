package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ServiceUnitController {

    private final ServiceUnitService serviceUnitService;

    @Autowired
    public ServiceUnitController(ServiceUnitService serviceUnitService) {
        this.serviceUnitService = serviceUnitService;
    }

    @ModelAttribute("serviceList")
    public List<ServiceUnit> serviceUnitList() {
        return serviceUnitService.getAll();
    }

    @GetMapping(value="/service/add")
    public String serviceForm(Model model) {
        model.addAttribute("service", new ServiceUnit());
        return "serviceunit";
    }

    @PostMapping(value = "/service/add")
    public String serviceAdding(@ModelAttribute("service") ServiceUnit serviceUnit) {
        serviceUnitService.save(serviceUnit);
        return "redirect:/service/add";
    }

}

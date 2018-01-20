package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceUnitController {

    private final ServiceUnitService serviceUnitService;

    @Autowired
    public ServiceUnitController(ServiceUnitService serviceUnitService) {
        this.serviceUnitService = serviceUnitService;
    }

    @RequestMapping(value="/service/add", method = RequestMethod.GET)
    public String serviceForm(Model model) {
        model.addAttribute("serviceForm", new ServiceUnit());
        return "addService";
    }

    @RequestMapping(value="/service", method = RequestMethod.GET)
    public String servicesList(Model model) {
        model.addAttribute("services", serviceUnitService.getServiceUnitsRepository().findAll());
        return "service";
    }

    @RequestMapping(value = "/service/add", method = RequestMethod.POST)
    public String serviceAdding(@ModelAttribute("serviceForm") ServiceUnit serviceUnit) {
        long id = serviceUnit.getId();
        String name = serviceUnit.getName();
        int cost = serviceUnit.getCost();

        ServiceUnit tempUnit = new ServiceUnit(name, cost);
        serviceUnitService.save(tempUnit);
        return "redirect:/service";
    }

}

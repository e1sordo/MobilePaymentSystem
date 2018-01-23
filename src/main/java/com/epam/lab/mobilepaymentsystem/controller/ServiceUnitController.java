package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ServiceUnitController {

    private final ServiceUnitService serviceUnitService;
    private final BillService billService;

    @Autowired
    public ServiceUnitController(ServiceUnitService serviceUnitService, BillService billService) {
        this.serviceUnitService = serviceUnitService;
        this.billService = billService;
        serviceUnitService.createTestService();
    }

    @GetMapping("services/management")
    public String listAllServices(Model model) {
        model.addAttribute("services", serviceUnitService.listAllServices());
        return "userservicemanagement";
    }

    @PostMapping("services/management")
    public String createTotalBill (@ModelAttribute ("services")Iterable<ServiceUnit> serviceUnits){
        User user = new User();
        user.setId(1);

        for (ServiceUnit service : serviceUnits) {
            billService.createAndSaveBill(user, service);
        }

        return "redirect:/billsmanagement";
    }
}

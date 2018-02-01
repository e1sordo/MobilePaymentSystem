package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ServiceUnitController {

    private final ServiceUnitService serviceUnitService;
    private final UserService userService;

    @Autowired
    public ServiceUnitController(ServiceUnitService serviceUnitService, UserService userService) {
        this.serviceUnitService = serviceUnitService;
        this.userService = userService;
    }

    @GetMapping("/services/add")
    public String serviceForm(Model model) {
        model.addAttribute("service", new ServiceUnit());
        return "service/service_add";
    }

    @PostMapping("/services/add")
    public String serviceAdding(@Valid @ModelAttribute("service") ServiceUnit serviceUnit, BindingResult bindingResult, Model model) {
        return serviceUnitService.validateNewServiceAndAdd(serviceUnit, bindingResult, model);
    }

    @GetMapping("/services")
    public String listAllServices(Model model, @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        List<ServiceUnit> inactiveServices = serviceUnitService.getAllServicesWithoutSubscribeOfUserByUserId(userService.getCurrentUserId());
        model.addAttribute("inactiveServices", inactiveServices);
        model.addAttribute("services", serviceUnitService.getAllServices());
        model.addAttribute("userRole", userService.getCurrentUser().getRole());
        return "service/service_list";
    }

    @GetMapping("/services/{id}")
    public String serviceUnitPage(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceUnitService.getServiceById(id));
        return "service/service_item";
    }

    @PostMapping("/services")
    public String subscribeToService(@ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        serviceUnitService.subscribeUserToServiceByUserAndServiceId(userService.getCurrentUserId(), serviceUnit.getId());
        return "redirect:/services";
    }

    // TODO: unsubscribe process doesn't return actual data after pressing a button
    @GetMapping("/profile/services")
    public String listActiveServices(@ModelAttribute("selectedService") ServiceUnit serviceUnit, Model model) {
        List<ServiceUnit> activeServices = serviceUnitService.getAllPaidServiceOfUserByUserId(userService.getCurrentUserId());
        model.addAttribute("activeServices", activeServices);
        return "service/my";
    }

    @PostMapping("users/{id}/services")
    public String unsubscribeFromService(@PathVariable Long id,
                                         @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
       // serviceUnitService.unsubscribeUserFromServiceByUserAndServiceId(userService.getCurrentUserId(), serviceUnit.getId());
        return "redirect:/users/" + id + "/services";
    }
}

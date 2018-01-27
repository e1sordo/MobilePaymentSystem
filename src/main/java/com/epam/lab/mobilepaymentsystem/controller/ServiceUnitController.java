package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
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

    // добавляет новую услугу. Отдельная страница
    // возвращает шаблон добавления услуги
    @GetMapping("/services/add")
    public String serviceForm(Model model) {
        model.addAttribute("service", new ServiceUnit());
        return "service/service_add";
    }

    // етод добавления услуги в базу
    // после добавления делает редирект на эту же самую страницу
    // можно добавить сообщение, что успешно добавлено
    @PostMapping("/services/add")
    public String serviceAdding(@ModelAttribute("service") ServiceUnit serviceUnit) throws IOException {
        if(serviceUnit.getCost() == 0) {
            throw new IllegalArgumentException("cost can't be equal to 0");
        }
        serviceUnitService.save(serviceUnit);
        return "redirect:/services/add";
    }

    // выводит список всех доступных услуг
    // возвращает шаблон со всеми услугами
    @GetMapping("/services")
    public String listAllServices(Model model, @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        List<ServiceUnit> inactiveServices = serviceUnitService.getAllServicesWithoutSubscribe();
        model.addAttribute("inactiveServices", inactiveServices);
        model.addAttribute("services", serviceUnitService.listAllServices());
        return "service/service_list";
    }

    @GetMapping("/services/{id}")
    public String serviceUnitPage(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceUnitService.getServiceById(id));
        return "service/service_item";
    }

    @PostMapping("/services")
    public String subscribeToService(@ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        serviceUnitService.subscribeUserToService(serviceUnit.getId());
        return "redirect:/services";
    }

    // TODO: doesnt return actual data after pressing a button
    @GetMapping("service/my")
    public String listActiveServices(@ModelAttribute("selectedService") ServiceUnit serviceUnit, Model model) {
        List<ServiceUnit> activeServices = userService.getActiveServicesByUserId();
        model.addAttribute("activeServices", activeServices);
        return "service/my";
    }

    @PostMapping("users/{id}/services")
    public String unsubscribeFromService(@PathVariable Long id,
                                         @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        serviceUnitService.unsubscribeUserFromService(serviceUnit.getId());
        return "redirect:/users/" + id + "/services";
    }
}

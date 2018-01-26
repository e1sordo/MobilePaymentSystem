package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String listAllServices(Model model,
                                  @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        Long userId = userService.getCurrentUserId();
        List<ServiceUnit> inactiveServices = serviceUnitService.getAllServicesWithoutSubscribe(userId);
        model.addAttribute("inactiveServices", inactiveServices);
        model.addAttribute("services", serviceUnitService.listAllServices());
        return "service/service_list";
    }

    @GetMapping("/services/{id}")
    public String serviceUnitPage(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceUnitService.getServiceById(id));
        return "service/service_item";
    }


//    @GetMapping("/services")
//    public String listInactiveServices(Model model, @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
//        //  serviceUnitService.subscribeUserToService(1L, 1L);
//        List<ServiceUnit> inactiveServices = serviceUnitService.getAllServicesWithoutSubscribe(userId);
//        model.addAttribute("inactiveServices", inactiveServices);
//        return "service/service_list";
//    }

    @PostMapping("/services")
    public String subscribeToService(@ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        Long userId = userService.getCurrentUserId();
        if (serviceUnit.getId() != -1) {
            serviceUnitService.subscribeUserToService(userId, serviceUnit.getId());
        }
        return "redirect:/services";
    }






//    // TODO: doesnt return actual data after pressing a button
//    @GetMapping("users/{id}/services")
//    public String listActiveServices(@PathVariable Long id, Model model,
//                                     @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
//        List<ServiceUnit> activeServices = userService.getActiveServicesByUserId(id);
//        model.addAttribute("activeServices", activeServices);
//        return "service/list";
//    }

    @PostMapping("users/{id}/services")
    public String unsubscribeFromService(@PathVariable Long id,
                                         @ModelAttribute("selectedService") ServiceUnit serviceUnit) {
        serviceUnitService.unsubscribeUserFromService(id, serviceUnit.getId());
        return "redirect:/users/" + id + "/services";
    }
}

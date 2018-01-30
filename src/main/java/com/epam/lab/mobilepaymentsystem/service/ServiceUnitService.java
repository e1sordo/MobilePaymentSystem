package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ServiceUnitService {

    private final ServiceUnitsRepository serviceUnitsRepository;
    private final UserService userService;
    private final BillService billService;

    @Autowired
    public ServiceUnitService(ServiceUnitsRepository serviceUnitsRepository, UserService userService, BillService billService) {
        this.serviceUnitsRepository = serviceUnitsRepository;
        this.userService = userService;
        this.billService = billService;
    }

    public void save(ServiceUnit serviceUnit) {
        serviceUnitsRepository.save(serviceUnit);
    }

    public Iterable<ServiceUnit> getAllServices() {
        return serviceUnitsRepository.findAll();
    }

    public List<ServiceUnit> getAllPaidServiceOfUser(long userId) {
        List<Bill> paidBills = (List<Bill>) billService.getAllNonExpiredPaidBillsOfUser(userId);
        List<ServiceUnit> serviceUnits = new ArrayList<>();

        for (Bill bill : paidBills) {
            serviceUnits.add(bill.getServiceUnit());
        }

        return serviceUnits;
    }

    public List<ServiceUnit> getAllServicesWithoutSubscribe() {
        List<ServiceUnit> services = (List<ServiceUnit>) serviceUnitsRepository.findAll();
        User user = userService.getCurrentUser();
        Set<ServiceUnit> userServices = user.getServiceUnits();
        services.removeAll(userServices);
        return services;
    }

    public ServiceUnit getByServiceName(String name) {
        return serviceUnitsRepository.findByName(name);
    }

    public ServiceUnit getServiceById(Long id) {
        return serviceUnitsRepository.findOne(id);
    }

    public long numberOfServices() {
        return serviceUnitsRepository.count();
    }

    public void subscribeUserToService(long serviceId) {
        // if we chose actual existing service
        if (serviceId != -1) {
            User user = userService.getCurrentUser();
            ServiceUnit service = getServiceById(serviceId);

            // we create bill only if the service is new
            if (user.addService(service)) {
                billService.createAndSaveBill(user, service);
                userService.updateUser(user);
            }
        }
    }

    public void unsubscribeUserFromService(long serviceId) {
        User user = userService.getCurrentUser();
        ServiceUnit service = getServiceById(serviceId);

        // if service exists and it is unpaid - remove also unpaid bill
        if (user.removeService(service)) {
            userService.updateUser(user);
            billService.deleteUnpaidBill(user.getId(), serviceId);
        }
    }


    // TODO: error about problems with cost doesn't appear
    public String validateNewServiceAndAdd(ServiceUnit serviceUnit, BindingResult bindingResult, Model model) {
        if(getByServiceName(serviceUnit.getName()) != null) {
            bindingResult.reject("name");
            model.addAttribute("sameName", "Service with the same name is in the list");
        }

        if(bindingResult.hasErrors()) {
            // TODO: info about other errors!
            return "service/service_add";
        }

        save(serviceUnit);
        return "redirect:/services";
    }

//    public void updateService(ServiceUnit serviceUnit) {
//
//    }


}

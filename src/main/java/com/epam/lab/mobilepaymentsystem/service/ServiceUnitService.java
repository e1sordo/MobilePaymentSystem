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

    public List<ServiceUnit> getAllPaidServiceOfUserByUserId(long userId) {
        List<Bill> paidBills = billService.getAllNonExpiredPaidBillsOfUserByUserId(userId);
        List<ServiceUnit> serviceUnits = new ArrayList<>();

        for (Bill bill : paidBills) {
            serviceUnits.add(bill.getServiceUnit());
        }

        return serviceUnits;
    }

    public List<ServiceUnit> getAllServicesWithoutSubscribeOfUserByUserId(long userId) {
        List<ServiceUnit> services = serviceUnitsRepository.findAll();
        User user = userService.getUserById(userId);
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

    public long numberOfActiveServicesOfUserByUserId(long userId) {
        return getAllPaidServiceOfUserByUserId(userId).size();
    }

    public long numberOfAllService() {
        return serviceUnitsRepository.count();
    }

    public void subscribeUserToServiceByUserAndServiceId(long userId, long serviceId) {
        // if we chose actual existing service
        if (serviceId != -1) {
            User user = userService.getUserById(userId);
            ServiceUnit service = getServiceById(serviceId);

            // we create bill only if the service is new
            if (user.addService(service)) {
                Bill bill = billService.createAndSaveBill(user, service);
                billService.withdrawCashToPayForOneBill(bill, user);
                userService.updateUser(user);
            }
        }
    }

    public void unsubscribeUserFromServiceByUserAndServiceId(long userId, long serviceId) {
        User user = userService.getUserById(userId);
        ServiceUnit service = getServiceById(serviceId);

        // if service exists and it is unpaid - remove also unpaid bill
        if (user.removeService(service)) {
            userService.updateUser(user);
            billService.deleteUnpaidBillByUserIdAndServiceId(user.getId(), serviceId);
        }
    }

    public String validateNewServiceAndAdd(ServiceUnit serviceUnit, BindingResult bindingResult, Model model) {
        if(getByServiceName(serviceUnit.getName()) != null) {
            bindingResult.reject("name");
            model.addAttribute("sameName", "Service with the same name is in the list");
        }

        if(bindingResult.hasErrors()) {
            return "service/service_add";
        }

        save(serviceUnit);
        return "redirect:/services";
    }

    public void globalCheckBill() {
        Iterable<User> users = userService.getAllUsers();
        for (User user : users) {
           localCheckBill(user.getId());
        }
    }

    public void localCheckBill(long userId) {
        Iterable<Bill> outOfDateUnpaidBills = billService.getAllUnpaidBillsOfUserByUserId(userId);

        for (Bill bill : outOfDateUnpaidBills) {
            unsubscribeUserFromServiceByUserAndServiceId(userId, bill.getServiceUnit().getId());
        }

        billService.withdrawCashToPayForServicesByUserId(userId);
    }
}

package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

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

    public long numberOfAllService() {
        return serviceUnitsRepository.count();
    }

    public long numberOfAllPaidActiveUserServicesByUserId(long userId) {
        return billService.getAllNonExpiredActivePaidServiceOfUserByUserId(userId).size()
                + billService.getAllExpiredActiveServicesOfUserByUserId(userId).size();
    }

    /**
     * Subscribe User with ID userId to Service with ID serviceID
     * Withdraw cash at the moment of subscribing
     * Create paid bill if payment is successful
     * Create unpaid bill if payment is unsuccessful
     * Already existing Service cannot be subscribed to User
     * @param userId ID of User
     * @param serviceId ID of Service
     */
    public void subscribeUserToServiceByUserAndServiceId(long userId, long serviceId) {
        if (serviceId != -1) {
            User user = userService.getUserById(userId);
            ServiceUnit service = getServiceById(serviceId);

            if (user.addService(service)) {
                Bill bill = billService.createAndSaveBill(user, service);
                billService.withdrawCashToPayForOneBill(bill, user);
                userService.updateUser(user);
            }
        }
    }

    /**
     * Unsubscribe User from Service
     * Delete unpaid Bill of this Service from User in process
     * Service cannot be unsubscribed if User doesn't have it
     * @param bill Bill that contains information about User subscribed to Service
     * @param userId ID of User
     */
    public void unsubscribeUserFromServiceByBillAndUserId(Bill bill, long userId) {
        User user = userService.getUserById(userId);
        ServiceUnit service = getServiceById(bill.getServiceUnit().getId());

        if (user.removeService(service)) {
            userService.updateUser(user);
            billService.deleteUnpaidBillByUserIdAndServiceId(userId, service.getId());
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

    @Transactional
    public void localCheckBill(long userId) {
        List<Bill> outOfDateUnpaidBills = billService.getAllUnpaidBillsOfUserByUserId(userId);

        for (Bill bill : outOfDateUnpaidBills) {
            unsubscribeUserFromServiceByBillAndUserId(bill, userId);
        }

        billService.withdrawCashToPayForServicesByUserId(userId);
    }
}

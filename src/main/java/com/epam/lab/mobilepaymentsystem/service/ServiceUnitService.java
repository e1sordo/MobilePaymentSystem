package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Iterable<ServiceUnit> listAllServices() {
        // todo add get as suffix
        return serviceUnitsRepository.findAll();
    }

    public List<ServiceUnit> getAllServicesWithoutSubscribe(long userId) {
        List<ServiceUnit> services = (List<ServiceUnit>) serviceUnitsRepository.findAll();
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

    public void subscribeUserToService(long userId, long serviceId) {
        User user = userService.getUserById(userId);
        ServiceUnit service = getServiceById(serviceId);

        // we create bill only if the service is new
        if (user.addService(service)) {
            userService.straightSave(user);
            billService.createAndSaveBill(user, service);
        }
    }

    public void unsubscribeUserFromService(long userId, long serviceId) {
        User user = userService.getUserById(userId);
        ServiceUnit service = getServiceById(serviceId);

        // if service exists and it is unpaid - remove also unpaid bill
        if (user.removeService(service)) {
            userService.straightSave(user);
            billService.deleteUnpaidBill(userId, serviceId);
        }
    }
}

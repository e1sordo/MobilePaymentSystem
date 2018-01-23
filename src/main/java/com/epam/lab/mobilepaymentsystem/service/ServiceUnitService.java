package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.dao.UserRepository;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ServiceUnitService {

    private final ServiceUnitsRepository serviceUnitsRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServiceUnitService(ServiceUnitsRepository serviceUnitsRepository, UserRepository userRepository) {
        this.serviceUnitsRepository = serviceUnitsRepository;
        this.userRepository = userRepository;
    }

    // TODO: return type?
    public Iterable<ServiceUnit> listAllServices() {
        return serviceUnitsRepository.findAll();
    }

    public void createTestService() {
        ServiceUnit serviceUnit = new ServiceUnit();
        serviceUnit.setName("test service");
        serviceUnit.setCost(100);
        serviceUnitsRepository.save(serviceUnit);
    }

    public List<ServiceUnit> getAllServicesWithoutSubscribe(long id) {
        List<ServiceUnit> services = (List<ServiceUnit>) serviceUnitsRepository.findAll();
        User user = userRepository.findUserById(id);
        Set<ServiceUnit> userServices = user.getServiceUnits();
        services.removeAll(userServices);
        return services;
    }

    public ServiceUnit getServiceById(long id) {
        return serviceUnitsRepository.findServiceUnitById(id);
    }
}

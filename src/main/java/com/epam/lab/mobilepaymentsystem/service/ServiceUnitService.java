package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.ServiceUnitsRepository;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUnitService {

    private final ServiceUnitsRepository serviceUnitsRepository;

    @Autowired
    public ServiceUnitService(ServiceUnitsRepository serviceUnitsRepository) {
        this.serviceUnitsRepository = serviceUnitsRepository;
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
}

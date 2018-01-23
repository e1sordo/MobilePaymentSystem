package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceUnitsRepository extends CrudRepository<ServiceUnit, Long> {
    ServiceUnit findByName(String name);

    List<ServiceUnit> findAll();
}

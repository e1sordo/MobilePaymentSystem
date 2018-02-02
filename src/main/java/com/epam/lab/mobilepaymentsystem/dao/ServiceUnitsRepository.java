package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceUnitsRepository extends CrudRepository<ServiceUnit, Long> {

    ServiceUnit findByName(String name);

    List<ServiceUnit> findAll();
}

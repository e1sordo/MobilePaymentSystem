package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import org.springframework.data.repository.CrudRepository;

public interface ServiceUnitsRepository extends CrudRepository<ServiceUnit, Long> {
}

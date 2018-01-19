package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import org.springframework.data.repository.CrudRepository;

public interface BillsRepository extends CrudRepository<Bill, Long> {
}

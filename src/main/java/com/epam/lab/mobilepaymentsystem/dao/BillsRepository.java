package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import org.springframework.data.repository.CrudRepository;

public interface BillsRepository extends CrudRepository<Bill, Long> {

    void deleteByUser_IdAndServiceUnit_IdAndPaidFor(long userId, long serviceId, boolean isPaid);

    Bill findBillByUser_IdAndServiceUnit_id(long userId, long serviceId);

    Iterable<Bill> findAllByUser_IdAndPaidFor(long userId, boolean isPaid);
}

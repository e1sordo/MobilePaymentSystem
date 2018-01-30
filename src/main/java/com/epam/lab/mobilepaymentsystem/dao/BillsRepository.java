package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface BillsRepository extends CrudRepository<Bill, Long> {

    void deleteByUser_IdAndServiceUnit_IdAndPaidFor(long userId, long serviceId, boolean isPaid);

    Bill findBillByUser_IdAndServiceUnit_id(long userId, long serviceId);

    Iterable<Bill> findAllByUser_IdAndPaidFor(long userId, boolean isPaid);

    Iterable<Bill> findAllByPaidForAndStartDateBeforeAndEndDateAfterOrderByUser_Id(Boolean paidFor, Date startDate, Date endDate);

    Iterable<Bill> findAllByUser_IdAndPaidForAndStartDateBeforeAndEndDateAfterOrderByActualCostDesc(long user_id, Boolean paidFor, Date startDate, Date endDate);

    long countAllByPaidFor(Boolean paidFor);

    long countAllByPaidForAndUser_Id(Boolean paidFor, long user_id);
}

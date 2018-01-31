package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface BillsRepository extends CrudRepository<Bill, Long> {

    void deleteByUser_IdAndServiceUnit_IdAndPaidFor(long userId, long serviceId, boolean isPaid);

    Bill findBillByUser_IdAndServiceUnit_idAndEndDateBefore(long user_id, long serviceUnit_id, Date endDate);

    List<Bill> findAllByUser_IdAndPaidFor(long userId, boolean isPaid);

    List<Bill> findAllByPaidForOrderByUser_Id(Boolean paidFor);

    List<Bill> findAllByPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByUser_Id(Boolean paidFor, Date startDate, Date endDate);

    List<Bill> findAllByUser_IdAndPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByActualCostDesc(long user_id, Boolean paidFor, Date startDate, Date endDate);

    List<Bill> findAllByUser_IdAndPaidForAndEndDateBefore(long user_id, Boolean paidFor, Date endDate);

    long countAllByPaidFor(Boolean paidFor);

    long countAllByPaidForAndUser_Id(Boolean paidFor, long user_id);
}

package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.BillsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class BillService {

    private static final boolean PAID = true;
    private static final boolean UNPAID = false;

    private final BillsRepository billsRepository;
    private final UserService userService;

    @Autowired
    public BillService(BillsRepository billsRepository, UserService userService) {
        this.billsRepository = billsRepository;
        this.userService = userService;
    }

    public void save(Bill bill) {
        billsRepository.save(bill);
    }

    public Bill createAndSaveBill(User user, ServiceUnit serviceUnit) {
        Bill bill = new Bill();
        bill.setUser(user);
        bill.setServiceUnit(serviceUnit);
        bill.setPaidFor(false);
        bill.setActualCost(serviceUnit.getCost());
        setDateForBill(bill);

        billsRepository.save(bill);
        return bill;
    }

    private void setDateForBill(Bill bill) {
        Calendar calendar = Calendar.getInstance();
        bill.setStartDate(new Date(calendar.getTimeInMillis()));
        int duration = bill.getServiceUnit().getDuration();
        if (duration <= 0) {
            calendar.set(2038, Calendar.JANUARY, 13);
            // https://en.wikipedia.org/wiki/Year_2038_problem :)
            bill.setEndDate(new Date(calendar.getTimeInMillis()));
        } else {
            calendar.add(Calendar.DATE, bill.getServiceUnit().getDuration());
            bill.setEndDate(new Date(calendar.getTimeInMillis()));
        }
    }

    public Bill getById(long billId) {
        return billsRepository.findOne(billId);
    }

    @Transactional
    public void deleteUnpaidBillByUserIdAndServiceId(long userId, long serviceId) {
        billsRepository.deleteByUser_IdAndServiceUnit_IdAndPaidFor(userId, serviceId, UNPAID);
    }

    public List<Bill> getAllPaidBillsOfUserByUserId(long userId) {
        return billsRepository.findAllByUser_IdAndPaidFor(userId, PAID);
    }

    public List<Bill> getAllNonExpiredPaidBillsOfUserByUserId(long userId) {
        return billsRepository.
                findAllByUser_IdAndPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByActualCostDesc(userId, PAID, getCurrentDate(), getCurrentDate());
    }

    public List<Bill> getAllUnpaidBillsOfUserByUserId(long userId) {
        return billsRepository.findAllByUser_IdAndPaidFor(userId, UNPAID);
    }

    // Методы для получения всех оплаченных и неоплаченных счетов всех пользователей [Admin]
    public List<Bill> getAllUnpaidBillsOfAllUsersOrderedById() {
        return billsRepository.findAllByPaidForOrderByUser_Id(UNPAID);
    }

    public List<Bill> getAllNonExpiredPaidBillsOfAllUsersOrderedById() {
        return billsRepository.
                findAllByPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByUser_Id(PAID, getCurrentDate(), getCurrentDate());
    }

    public List<Bill> getAllExpiredPaidBillsOfUserByUserId(long userId) {
        return billsRepository.findAllByUser_IdAndPaidForAndEndDateBefore(userId, PAID, getCurrentDate());
    }

    public long numberOfAllBills() {
        return billsRepository.count();
    }

    public long numberOfAllUnpaidBills() {
        return billsRepository.countAllByPaidFor(UNPAID);
    }

    public long numberOfUnpaidBillsOfUserByUserId(long userId) {
        return billsRepository.countAllByPaidForAndUser_Id(UNPAID, userId);
    }

    // payment for EXPIRED PAID BILL
    public void withdrawCashToPayForServicesByUserId(long userId) {
        List<ServiceUnit> serviceUnits = userService.getActiveServicesByUserId();
        User user = userService.getUserById(userId);

        for (ServiceUnit service : serviceUnits) {
            Bill bill = getExpiredPaidBillByUserIdAndServiceId(userId, service.getId());
            if (bill != null) {
                withdrawCashToPayForOneBill(bill, user);
            }
        }

        userService.updateUser(user); // todo: maybe it's not needed
    }

    public void withdrawCashToPayForOneBill(Bill bill, User user) {
        if (bill.getActualCost() <= user.getBankAccount()) {
            int bankAccount = user.getBankAccount() - bill.getActualCost();
            user.setBankAccount(bankAccount);
            bill.setPaidFor(true);
            setDateForBill(bill);
            save(bill);
        }
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public Bill getExpiredPaidBillByUserIdAndServiceId(long userId, long serviceId) {
        return billsRepository.findBillByUser_IdAndServiceUnit_idAndEndDateBefore(userId, serviceId, getCurrentDate());
    }

    public List<Bill> getAllPaidServiceOfUserByUserId(long userId) {
        return getAllNonExpiredPaidBillsOfUserByUserId(userId);
    }
}

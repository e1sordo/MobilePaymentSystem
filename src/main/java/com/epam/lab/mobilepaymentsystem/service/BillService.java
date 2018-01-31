package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.BillsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
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

        Calendar calendar = Calendar.getInstance();
        bill.setStartDate(new Date(calendar.getTimeInMillis()));
        int duration = serviceUnit.getDuration();
        if (duration <= 0) {
            calendar.set(2038, Calendar.JANUARY, 13);
            // https://en.wikipedia.org/wiki/Year_2038_problem :)
            bill.setEndDate(new Date(calendar.getTimeInMillis()));
        } else {
            calendar.add(Calendar.DATE, serviceUnit.getDuration());
            bill.setEndDate(new Date(calendar.getTimeInMillis()));
        }

        billsRepository.save(bill);
        return bill;
    }

    public Bill getById(long billId) {
        return billsRepository.findOne(billId);
    }

    public void deleteUnpaidBill(long userId, long serviceId) {
        billsRepository.deleteByUser_IdAndServiceUnit_IdAndPaidFor(userId, serviceId, UNPAID);
    }

    public List<Bill> getAllPaidBillsOfUser(long userId) {
        return billsRepository.findAllByUser_IdAndPaidFor(userId, PAID);
    }

    public List<Bill> getAllNonExpiredPaidBillsOfUser(long userId) {
        return billsRepository.findAllByUser_IdAndPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByActualCostDesc(userId, PAID, getToday(), getToday());
    }

    public List<Bill> getAllNonExpiredUnpaidBillsOfUser(long userId) {
        return billsRepository.findAllByUser_IdAndPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByActualCostDesc(userId, UNPAID, getToday(), getToday());
    }

    // Методы для получения всех оплаченных и неоплаченных счетов всех пользователей [Admin]
    public List<Bill> getAllNonExpiredUnpaidBillsOfAllUsersOrderedById() {
        return billsRepository.findAllByPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByUser_Id(UNPAID, getToday(), getToday());
    }

    public List<Bill> getAllNonExpiredPaidBillsOfAllUsersOrderedById() {
        return billsRepository.findAllByPaidForAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByUser_Id(PAID, getToday(), getToday());
    }

    public List<Bill> getAllExpiredUnpaidBillsOfUser(long userId) {
        return billsRepository.findAllByUser_IdAndPaidForAndEndDateBefore(userId, UNPAID, getToday());
    }

    public long numberOfAllBills() {
        return billsRepository.count();
    }

    public long numberOfAllUnpaidBills() {
        return billsRepository.countAllByPaidFor(UNPAID);
    }

    public long numberOfUnpaidBillsOfUser(long userId) {
        return billsRepository.countAllByPaidForAndUser_Id(UNPAID, userId);
    }

    public int countTotalSum(Iterable<Bill> bills) {
        int total = 0;

        for (Bill bill: bills) {
            total += bill.getActualCost();
        }
        return total;
    }

    public void withdrawCashToPayForBills(long userId) {
        List<Bill> unpaidBills = getAllNonExpiredUnpaidBillsOfUser(userId);
        User user = userService.getUserById(userId);
        Iterator<Bill> iterator = unpaidBills.iterator();

        while (iterator.hasNext()) {
            Bill bill = iterator.next();
            withdrawCashToPayForOneBill(bill, user);
            iterator.remove();
        }
        userService.updateUser(user); // todo: maybe it's not needed
    }

    public void withdrawCashToPayForOneBill(Bill bill, User user) {
        if (bill.getActualCost() <= user.getBankAccount()) {
            int bankAccount = user.getBankAccount() - bill.getActualCost();
            user.setBankAccount(bankAccount);
            bill.setPaidFor(true);
            save(bill);
        }
    }

    private Date getToday() {
        return new Date(System.currentTimeMillis());
    }
}

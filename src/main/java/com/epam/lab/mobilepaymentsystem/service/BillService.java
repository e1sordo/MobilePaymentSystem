package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.BillsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.Role;
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

    public void createAndSaveBill(User user, ServiceUnit serviceUnit) {
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
        changeRoleDueToBillsChange(user.getId());
    }

    public void deleteUnpaidBill(long userId, long serviceId) {
        billsRepository.deleteByUser_IdAndServiceUnit_IdAndPaidFor(userId, serviceId, UNPAID);
        changeRoleDueToBillsChange(userId);
    }

    public Iterable<Bill> listAllPaidBillsOfUser(long id) {
        return billsRepository.findAllByUser_IdAndPaidFor(id, PAID);
    }

    public Iterable<Bill> listAllUnpaidBillsOfUser(long id) {
        return billsRepository.findAllByUser_IdAndPaidFor(id, UNPAID);
    }

    // TODO: involve date in logic
//    private Iterable<Bill> getAllUnpaidBillsOfUserForTodayOrderedByCost() {
//        return
//    }

    public long numberOfPaidBills() {
        return billsRepository.count();
    }

    public long numberOfUnpaidBills() {
        return billsRepository.count();
    }

    public int countTotalSum(Iterable<Bill> bills) {
        int total = 0;

        for (Bill bill: bills) {
            total += bill.getActualCost();
        }
        return total;
    }

    public void withdrawCashToPayForBills(long id) {
        List<Bill> unpaidBills = (List<Bill>) listAllUnpaidBillsOfUser(id);
        User user = userService.getUserById(id);
        Iterator<Bill> iterator = unpaidBills.iterator();

        while (iterator.hasNext()) {
            Bill bill = iterator.next();

            if (bill.getActualCost() <= user.getBankAccount()) {
                int bankAccount = user.getBankAccount() - bill.getActualCost();
                user.setBankAccount(bankAccount);
                bill.setPaidFor(true);
                save(bill);
                iterator.remove();
            }
        }

        changeRoleDueToBillsChange(id);
    }

    // TODO: billService or userService?
    private void changeRoleDueToBillsChange(long id) {
        List<Bill> unpaidBills = (List<Bill>) listAllUnpaidBillsOfUser(id);
        User user = userService.getUserById(id);
        if (unpaidBills.isEmpty()) {
            userService.changeUserRole(user, Role.ROLE_SUBSCRIBER);
        } else {
            // TODO: what role should we use?
            userService.changeUserRole(user, Role.ROLE_LOCKED);
        }
    }
}

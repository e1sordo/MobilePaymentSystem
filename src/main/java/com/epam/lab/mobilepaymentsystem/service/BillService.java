package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.BillsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private static final boolean PAID = true;
    private static final boolean UNPAID = false;

    private final BillsRepository billsRepository;

    @Autowired
    public BillService(BillsRepository billsRepository) {
        this.billsRepository = billsRepository;
    }

    public void save(Bill bill) {
        billsRepository.save(bill);
    }

    public void createAndSaveBill(User user, ServiceUnit serviceUnit) {
//        Bill bill = billsRepository.findBillByUser_IdAndServiceUnit_id(user.getId(), serviceUnit.getId());
//        if (bill == null)
        Bill bill = new Bill();
        bill.setUser(user);
        bill.setServiceUnit(serviceUnit);
        bill.setPaidFor(false);
        billsRepository.save(bill);
    }

    public void deleteUnpaidBill(long userId, long serviceId) {
        billsRepository.deleteByUser_IdAndServiceUnit_IdAndPaidFor(userId, serviceId, UNPAID);
    }

    public Iterable<Bill> listAllPaidBillsOfUser(long userId) {
        return billsRepository.findAllByUser_IdAndPaidFor(userId, PAID);
    }

    public Iterable<Bill> listAllUnpaidBillsOfUser(long userId) {
        return billsRepository.findAllByUser_IdAndPaidFor(userId, UNPAID);
    }

    public long numberOfPaidBills() {
        return billsRepository.count();
    }

    public long numberOfUnpaidBills() {
        return billsRepository.count();
    }

    public int countTotalSum(Iterable<Bill> bills) {
        int total = 0;

        for (Bill bill: bills) {
            total += bill.getServiceUnit().getCost();
        }
        return total;
    }
}

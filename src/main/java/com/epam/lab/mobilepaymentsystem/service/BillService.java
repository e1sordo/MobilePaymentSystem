package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.BillsRepository;
import com.epam.lab.mobilepaymentsystem.model.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private final BillsRepository billsRepository;

    @Autowired
    public BillService(BillsRepository billsRepository) {
        this.billsRepository = billsRepository;
    }

    public void save(Bill bill) {
        billsRepository.save(bill);
    }
}

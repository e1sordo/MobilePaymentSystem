package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillController {

    private final BillService billService;
    private final UserService userService;

    @Autowired
    public BillController(BillService billService, UserService userService) {
        this.billService = billService;
        this.userService = userService;
    }

    @GetMapping("/bills")
    public String getBills(Model model) {
        long id = userService.getCurrentUserId();
        Iterable<Bill> oldBills = billService.listAllPaidBillsOfUser(id);
        Iterable<Bill> unpaidBills = billService.listAllUnpaidBillsOfUser(id);
        model.addAttribute("paidBills", oldBills);
        model.addAttribute("unpaidBills", unpaidBills);
        return "bill/bill_list";
    }

    @GetMapping("/testbillpay")
    public String test(Model model) {
        long id = userService.getCurrentUserId();
        billService.withdrawCashToPayForBills(id);
        return "bill/bill_list";
    }
}

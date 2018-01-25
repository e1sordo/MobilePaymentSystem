package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public String getBills(Model model) {
        Iterable<Bill> oldBills = billService.listAllPaidBillsOfUser();
        Iterable<Bill> unpaidBills = billService.listAllUnpaidBillsOfUser();
        model.addAttribute("paidBills", oldBills);
        model.addAttribute("unpaidBills", unpaidBills);
        return "bill/bill_list";
    }

//    @GetMapping("/bills/payment")
//    public String listAllUnpaidBills(Model model) {
//        Iterable<Bill> unpaidBills = billService.listAllUnpaidBillsOfUser(USER_DEFAULT_ID);
//        int total = billService.countTotalSum(unpaidBills);
//        model.addAttribute("unpaidBills", unpaidBills);
//        model.addAttribute("total", total);
//        return "bill/payment";
//    }
}

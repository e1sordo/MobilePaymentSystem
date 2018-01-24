package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillController {

    private static final long USER_DEFAULT_ID = 1L;

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

//    Manual bill creating for tests purposes
//    @GetMapping("/bill/add")
//    public String billForm(Model model) {
//        model.addAttribute("bill", new Bill());
//        return "bill";
//    }
//
//    @PostMapping("/bill/add")
//    public String billSubmit(@ModelAttribute("bill") Bill bill) {
//        billService.save(bill);
//        return "redirect:/bill/add";
//    }

    @GetMapping("/bills/payment")
    public String listAllUnpaidBills(Model model) {
        Iterable<Bill> unpaidBills = billService.listAllUnpaidBillsOfUser(USER_DEFAULT_ID); // TODO: get user id
        int total = billService.countTotalSum(unpaidBills);
        model.addAttribute("unpaidBills", unpaidBills);
        model.addAttribute("total", total);
        return "bill/payment";
    }

    @GetMapping("/bills/old")
    public String listAllOldBills(Model model) {
        Iterable<Bill> oldBills = billService.listAllPaidBillsOfUser(USER_DEFAULT_ID);
        model.addAttribute("oldBills", oldBills);
        return "bill/old";
    }
}

package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public String getBills(Model model) {
        model.addAttribute("allBills", null);
        model.addAttribute("unpaidBills", null);
        return "bill/bill_list";
    }

    @GetMapping("/bills/add")
    public String billForm(Model model) {
        model.addAttribute("bill", new Bill());
        return "bill";
    }

    @PostMapping("/bills/add")
    public String billSubmit(@ModelAttribute("bill") Bill bill) {
        billService.save(bill);
        return "redirect:/bill/add";
    }
}

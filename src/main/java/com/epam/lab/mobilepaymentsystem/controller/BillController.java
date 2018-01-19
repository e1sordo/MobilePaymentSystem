package com.epam.lab.mobilepaymentsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BillController {

    @RequestMapping("/newbill")
    public String billForm() {
        return "bill";
    }

//    @PostMapping("/newbill")
//    public String billSubmit(@ModelAttribute Bill bill) {
//        return "index";
//    }
}

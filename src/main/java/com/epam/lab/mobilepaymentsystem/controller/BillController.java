package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Bill;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BillController {

    private final BillService billService;
    private final ServiceUnitService serviceUnitService;
    private final UserService userService;

    @Autowired
    public BillController(BillService billService, ServiceUnitService serviceUnitService, UserService userService) {
        this.billService = billService;
        this.serviceUnitService = serviceUnitService;
        this.userService = userService;
    }

    @GetMapping("/bills")
    public String getBills(Model model) {
        List<Bill> oldBills = billService.getAllNonExpiredPaidBillsOfAllUsersOrderedById();
        List<Bill> unpaidBills = billService.getAllUnpaidBillsOfAllUsersOrderedById();
        model.addAttribute("paidBills", oldBills);
        model.addAttribute("unpaidBills", unpaidBills);
        return "bill/bill_list";
    }

    @GetMapping("/users/{uid}/bills")
    public String getBillListByUser(@PathVariable final long uid, Model model) {
        List<Bill> userBills = billService.getAllUnpaidBillsOfUserByUserId(uid);
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        return "bill/bill_user_list";
    }

    @GetMapping("/users/{uid}/services")
    public String getServiceListByUser(@PathVariable final long uid, Model model) {
        List<Bill> userBills = billService.getAllPaidBillsOfUserByUserId(uid);
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        return "service/service_user_list";
    }

    @GetMapping("/profile/bills")
    public String getBillListByCurrentUser(Model model) {
        final long id = userService.getCurrentUserId();
        List<Bill> userBills = billService.getAllUnpaidBillsOfUserByUserId(id);
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        return "bill/bill_user_list";
    }

    @GetMapping("/profile/services")
    public String getServiceListByCurrentUser(Model model) {
        final long id = userService.getCurrentUserId();
        List<Bill> userBills = billService.getAllPaidBillsOfUserByUserId(id);
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        return "service/service_user_list";
    }

    @GetMapping("/users/{uid}/bills/{bid}")
    public String getBillUnit(@PathVariable("uid") final long uid,
                              @PathVariable("bid") final long bid,
                              Model model) {
        Bill currentBill = billService.getById(bid);
        model.addAttribute("bill", currentBill);
        model.addAttribute("service", currentBill.getServiceUnit());
        model.addAttribute("userId", uid);
        model.addAttribute("unsubscribeAviable", billService.isNewBill(currentBill));
        // model.addAttribute("unpaidBills", billService.getAllNonExpiredUnpaidBillsOfUser(userService.getCurrentUserId()));
        return "bill/bill_item";
    }

    @PostMapping("/users/{uid}/bills/{bid}/unsub")
    public String unsubscribeFromUnpaidBill(@PathVariable("uid") final long uid,
                                            @PathVariable("bid") final long bid) {
        serviceUnitService.unsubscribeUserFromServiceByUserAndServiceId(
                uid, billService.getById(bid).getServiceUnit().getId());
        return "redirect:/users/" + uid + "/bills";
    }

    @PostMapping("/users/{uid}/bills/{bid}/pay")
    public String payUnpaidBill(@PathVariable("uid") final long uid,
                                @PathVariable("bid") final long bid) {
        billService.withdrawCashToPayForOneBill(billService.getById(bid),
                userService.getUserById(uid));
        return "redirect:/users/" + uid + "/bills";
    }
}

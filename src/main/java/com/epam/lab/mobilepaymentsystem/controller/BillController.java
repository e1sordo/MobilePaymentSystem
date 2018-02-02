package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.Bill;
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
        List<Bill> oldBills = billService.getAllPaidBillsOfAllUsersOrderedById(); // todo: check
        List<Bill> unpaidBills = billService.getAllUnpaidBillsOfAllUsersOrderedById();
        model.addAttribute("paidBills", oldBills);
        model.addAttribute("unpaidBills", unpaidBills);
        return "bill/bill_list";
    }

    @GetMapping("/users/{uid}/bills")
    public String getBillListByUser(@PathVariable final long uid, Model model) {
        List<Bill> userBills = billService.getAllUnpaidBillsOfUserByUserId(uid);
        model.addAttribute("currentUserId", userService.getCurrentUserId());
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        return "bill/bill_user_list";
    }

    @GetMapping("/users/{uid}/services")
    public String getServiceListByUser(@PathVariable final long uid, Model model) {
        List<Bill> userBills = billService.getAllNonExpiredActivePaidServiceOfUserByUserId(uid);
        List<Bill> userInactiveBills = billService.getAllExpiredActiveServicesOfUserByUserId(uid);
        model.addAttribute("currentUserId", userService.getCurrentUserId());
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        model.addAttribute("oldUserServices", userInactiveBills);
        model.addAttribute("empty2", userInactiveBills.isEmpty());
        return "service/service_user_list";
    }

    @GetMapping("/profile/bills")
    public String getBillListByCurrentUser(Model model) {
        final long id = userService.getCurrentUserId();
        List<Bill> userBills = billService.getAllUnpaidBillsOfUserByUserId(id);
        model.addAttribute("currentUserId", userService.getCurrentUserId());
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        return "bill/bill_user_list";
    }

    @GetMapping("/profile/services")
    public String getServiceListByCurrentUser(Model model) {
        final long id = userService.getCurrentUserId();
        List<Bill> userBills = billService.getAllNonExpiredActivePaidServiceOfUserByUserId(id);
        List<Bill> userInactiveBills = billService.getAllExpiredActiveServicesOfUserByUserId(id);
        model.addAttribute("currentUserId", userService.getCurrentUserId());
        model.addAttribute("empty", userBills.isEmpty());
        model.addAttribute("userBills", userBills);
        model.addAttribute("oldUserServices", userInactiveBills);
        model.addAttribute("empty2", userInactiveBills.isEmpty());
        return "service/service_user_list";
    }

    @GetMapping("/profile/bills/{bid}")
    public String getBillUnit(@PathVariable("bid") final long bid,
                              Model model) {
        Bill currentBill = billService.getById(bid);
        model.addAttribute("bill", currentBill);
        model.addAttribute("service", currentBill.getServiceUnit());
        model.addAttribute("userId", userService.getCurrentUserId());
        model.addAttribute("unsubscribeAviable", billService.isNewBill(currentBill));
        return "bill/bill_item";
    }

    @GetMapping("/users/{uid}/bills/{bid}")
    public String getBillUnit(@PathVariable("uid") final long uid,
                              @PathVariable("bid") final long bid,
                              Model model) {
        Bill currentBill = billService.getById(bid);
        model.addAttribute("bill", currentBill);
        model.addAttribute("service", currentBill.getServiceUnit());
        model.addAttribute("userId", uid);
        model.addAttribute("currentUserId", userService.getCurrentUserId());
        model.addAttribute("unsubscribeAviable", billService.isNewBill(currentBill));
        return "bill/bill_item";
    }

    @PostMapping("/profile/bills/{bid}/unsub")
    public String unsubscribeFromUnpaidBill(@PathVariable("bid") final long bid) {
        long uid = userService.getCurrentUserId();
        serviceUnitService.unsubscribeUserFromServiceByBillAndUserId(billService.getById(bid), uid);
        return "redirect:/profile/bills";
    }

    @PostMapping("/users/{uid}/bills/{bid}/unsub")
    public String unsubscribeFromUnpaidBill(@PathVariable("uid") final long uid,
                                            @PathVariable("bid") final long bid) {
        serviceUnitService.unsubscribeUserFromServiceByBillAndUserId(billService.getById(bid), uid);
        return "redirect:/users/" + uid + "/bills";
    }

    @PostMapping("/profile/bills/{bid}/pay")
    public String payUnpaidBill(@PathVariable("bid") final long bid) {
        long uid = userService.getCurrentUserId();
        billService.withdrawCashToPayForOneBill(billService.getById(bid),
                userService.getUserById(uid));
        return "redirect:/profile/bills";
    }

    @PostMapping("/users/{uid}/bills/{bid}/pay")
    public String payUnpaidBill(@PathVariable("uid") final long uid,
                                @PathVariable("bid") final long bid) {
        billService.withdrawCashToPayForOneBill(billService.getById(bid),
                userService.getUserById(uid));
        return "redirect:/users/" + uid + "/bills";
    }
}

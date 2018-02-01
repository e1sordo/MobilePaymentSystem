package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.BillService;
import com.epam.lab.mobilepaymentsystem.service.SecurityService;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import com.epam.lab.mobilepaymentsystem.wrapper.IntegerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final BillService billService;
    private final ServiceUnitService serviceUnitService;

    @Autowired
    public UserController(UserService userService,
                          SecurityService securityService,
                          BillService billService,
                          ServiceUnitService serviceUnitService) {
        this.userService = userService;
        this.securityService = securityService;
        this.billService = billService;
        this.serviceUnitService = serviceUnitService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("userForm") User userForm,
                               BindingResult bindingResult, Model model) {
        return userService.validateNewUserAndRegister(userForm, bindingResult, model, securityService);
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }

        return "user/login";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userService", userService);
        return "user/user_list";
    }

    @GetMapping("/profile")
    public String showMyProfile(Model model) {
        long id = userService.getCurrentUserId();
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("myProfile", true);
        addModelAttributes(model, id);
        return "user/user_item";
    }

    @PostMapping("/profile")
    public String showButtonTopUpBalance(@Valid @ModelAttribute("howMuchToIncrease") IntegerWrapper howMuch,
                                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            long id = userService.getCurrentUserId();
            addModelAttributes(model, id);
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("error", true);
            return "user/user_item";
        }
        userService.topUpBalance(userService.getCurrentUserId(), howMuch.getTranche());
        return "redirect:/profile";
    }

    @GetMapping("/users/{id}")
    public String showUserProfile(@PathVariable final Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("myProfile", false);
        addModelAttributes(model, id);
        return "user/user_item";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable final long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable final long id) {
        userService.blockUserById(id, false);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable final long id) {
        userService.blockUserById(id, true);
        return "redirect:/users";
    }

    private void addModelAttributes(Model model, long userId) {
        model.addAttribute("currentUserId", userId);
        model.addAttribute("numberOfServices",
                serviceUnitService.numberOfActiveServicesOfUserByUserId(userId));
        model.addAttribute("numberOfBills",
                billService.numberOfUnpaidBillsOfUserByUserId(userId));
        model.addAttribute("howMuchToIncrease", new IntegerWrapper());
    }
}

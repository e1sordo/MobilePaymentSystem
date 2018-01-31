package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.SecurityService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import com.epam.lab.mobilepaymentsystem.wrapper.IntegerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService,
                          SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
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
        return "user/user_list";
    }

    @GetMapping("/profile")
    public String showMyProfile(Principal principal, Model model) {
        Long id = userService.getByUsername(principal.getName()).getId();
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("howMuchToIncrease", new IntegerWrapper());
        return "user/user_item";
    }

    @GetMapping("/users/{id}")
    public String showUserProfile(@PathVariable final Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("howMuchToIncrease", new IntegerWrapper());
        return "user/user_item";
    }

    @PostMapping("/users/{id}/refill")
    public String showButtonTopUpBalance(@PathVariable final Long id,
                                         @Valid @ModelAttribute("howMuchToIncrease") IntegerWrapper howMuch) {
        userService.topUpBalance(id, howMuch.getTranche());
        return "redirect:/profile";
    }

    @GetMapping("/profile/services")
    public String showMyServices(Principal principal) {
        Long id = userService.getByUsername(principal.getName()).getId();
        return "redirect:/users/" + id + "/services";
    }

    @DeleteMapping("users/{id}/delete")
    public String deleteUser(@PathVariable final Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }
}

package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.SecurityService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import com.epam.lab.mobilepaymentsystem.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService,
                          SecurityService securityService,
                          UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "user/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") @Validated User userForm,
                               BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/index";
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

    @GetMapping("/admin")
    public String adminPage(Model model) {
        return "user/admin";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/index";
    }
//
//    @GetMapping("/{id}")
//    public String getUser(@PathVariable long id, Model model) {
//        model.addAttribute("users", userService.getUserById(id));
//        return "user/index";
//    }

//    @GetMapping
//    public String showAddUserPage(@ModelAttribute("user") User user, Model model) {
//        model.addAttribute("user", user);
//        return "adduser";
//    }

//    @PostMapping
//    public String createUser(@RequestBody User user) throws BindException {
//        userService.saveUser(user);
//        return "user/index";
//    }
}

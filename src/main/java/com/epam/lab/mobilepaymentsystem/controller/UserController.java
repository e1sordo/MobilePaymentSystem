package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.SecurityService;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
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
    public String registration(@Valid @ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {

        if(userService.getByUsername(userForm.getUsername()) != null) {
            bindingResult.reject("username");
            model.addAttribute("userWithSameUserName", "There is already a user registered with the username provided");
            return "user/registration";
        }
        if(!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            bindingResult.reject("password");
            model.addAttribute("passwordsNotSame", "Passwords don't match");
            return "user/registration";
        }
        if(bindingResult.hasErrors()) {
            return "user/registration";
        }

        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/";
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
    public String showMyProfile(Principal principal) {
        Long id = userService.getByUsername(principal.getName()).getId();
        return "redirect:/users/" + id;
    }

    @GetMapping("/profile/services")
    public String showMyServices(Principal principal) {
        Long id = userService.getByUsername(principal.getName()).getId();
        return "redirect:/users/" + id + "/services";
    }

    @GetMapping("/users/{id}")
    public String showUserProfile(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user/user_item";
    }

    @GetMapping("/users/{id}/services")
    public String showUserServices(@PathVariable long id, Model model) {
        // todo ДОДЕЛАТЬ ВЫВОД УСЛУГ
        model.addAttribute("userServices", null);
        return "service/service_list";
    }

    @DeleteMapping("users/{id}/delete")
    public String deleteUser(@PathVariable long id) {
        // todo ПЕРЕНОС В ГРУППУ АРХИВ
        return "redirect:/users";
    }
}

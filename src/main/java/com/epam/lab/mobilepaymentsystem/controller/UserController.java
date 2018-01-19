package com.epam.lab.mobilepaymentsystem.controller;

import com.epam.lab.mobilepaymentsystem.dao.UsersRepository;
import com.epam.lab.mobilepaymentsystem.form.UserForm;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UsersRepository usersRepository;

    @Value("${error.message}")
    private String errorMessage;

    @Autowired
    public UserController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", usersRepository.findAll());
        return "user";
    }

    @GetMapping("/user/add")
    public String showAddUserPage(Model model) {
        UserForm userForm = new UserForm();

        model.addAttribute("userForm", userForm);
        return "adduser";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("userForm") UserForm userForm) {
        long id             = userForm.getId();
        String login        = userForm.getLogin();
        String password     = userForm.getPassword();
        int personalAccount = userForm.getPersonalAccount();
        boolean isAdmin     = true;
        boolean isLocked    = false;

        User user = new User(login, password, personalAccount, isAdmin, isLocked);
        usersRepository.save(user);
        return "redirect:/user";
    }
}

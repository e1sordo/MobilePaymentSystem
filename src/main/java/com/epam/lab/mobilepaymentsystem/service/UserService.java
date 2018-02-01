package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.UserRepository;
import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void addUser(User user) {
        user.setBankAccount(0);
        user.setRole(Role.ROLE_USER.getDisplayName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUserById(long id) {
        User user = userRepository.findUserById(id);
        user.setRole(Role.ROLE_DELETED.getDisplayName());
        userRepository.save(user);
    }

    public void blockUserById(long id, boolean isAlreadyBlocked) {
        User user = userRepository.findUserById(id);
        String role = Role.ROLE_LOCKED.getDisplayName();
        if (isAlreadyBlocked) {
            role = Role.ROLE_SUBSCRIBER.getDisplayName();
        }
        user.setRole(role);
        userRepository.save(user);
    }

    public void topUpBalance(Long id, Integer tranche) {
        User user = getUserById(id);
        if (user.getRole().equals(Role.ROLE_USER.getDisplayName())) {
            user.setRole(Role.ROLE_SUBSCRIBER.getDisplayName());
        }

        user.setBankAccount(user.getBankAccount() + tranche);
        updateUser(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAllByRoleNot(
                Role.ROLE_DELETED.getDisplayName());
    }

    public long numberOfUsers() {
        return userRepository.count();
    }

    public long numberOfUsersByRole(String role) {
        return userRepository.countAllByRole(role);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public Long getCurrentUserId() {
        org.springframework.security.core.userdetails.User userSecurity =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userSecurity.getUsername());
        return user.getId();
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User userSecurity =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userSecurity.getUsername());
    }

    public List<ServiceUnit> getActiveServicesByUserId() {
        User user = getCurrentUser();
        return new ArrayList<>(user.getServiceUnits());
    }

    // TODO: not sure about working with model in backend
    // On the other hand validation logic being in frontend is bad
    // And maybe it is better to autowire SecurityService in UserService instead of passing it as param
    public String validateNewUserAndRegister(User user, BindingResult bindingResult, Model model, SecurityService securityService) {

        if((getByUsername(user.getUsername()) != null) && (!user.getPassword().equals(user.getConfirmPassword()))) {
            bindingResult.reject("username");
            bindingResult.reject("password");
            model.addAttribute("userWithSameUserName", "There is already a user registered with the username provided");
            model.addAttribute("passwordsNotSame", "Passwords don't match");
            return "user/registration";
        }

        if(getByUsername(user.getUsername()) != null) {
            bindingResult.reject("username");
            model.addAttribute(
                    "userWithSameUserName",
                    "There is already a user registered with the username provided");
            return "user/registration";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.reject("password");
            model.addAttribute(
                    "passwordsNotSame",
                    "Passwords don't match");
            return "user/registration";
        }

        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        addUser(user);
        securityService.autoLogin(user.getUsername(), user.getConfirmPassword());

        return "redirect:/";
    }
}

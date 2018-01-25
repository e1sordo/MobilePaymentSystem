package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.UserRepository;
import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        user.setRole(Role.ROLE_SUBSCRIBER.getDisplayName());
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        // todo
        userRepository.delete(id);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long count() {
        return userRepository.count();
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

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public List<ServiceUnit> getActiveServicesByUserId() {
        User user = getCurrentUser();
        return new ArrayList<>(user.getServiceUnits());
    }
}

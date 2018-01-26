package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.UserRepository;
import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void addUser(User user) {
        user.setRole(Role.ROLE_SUBSCRIBER.getDisplayName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findUserById(id);
        user.setRole(Role.ROLE_DELETED.getDisplayName());
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long numberOfUsers() {
        return userRepository.count();
    }

    public Long getCurrentUserId() {
        org.springframework.security.core.userdetails.User userSecurity =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userSecurity.getUsername());
        return user.getId();
    }

    public void straightSave(User user) {
        // todo change name "update"
        userRepository.save(user);
    }

    public List<ServiceUnit> getActiveServicesByUserId(long userId) {
        User user = userRepository.findUserById(userId);
        return new ArrayList<>(user.getServiceUnits());
    }
}

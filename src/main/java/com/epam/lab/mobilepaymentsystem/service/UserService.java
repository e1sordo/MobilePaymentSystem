package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.RoleRepository;
import com.epam.lab.mobilepaymentsystem.dao.UserRepository;
import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }

    public void save(User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findOne(1L));
        user.setRoles(roles);
        userRepository.save(user);
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
}

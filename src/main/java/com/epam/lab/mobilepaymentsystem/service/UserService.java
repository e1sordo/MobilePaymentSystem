package com.epam.lab.mobilepaymentsystem.service;

import com.epam.lab.mobilepaymentsystem.dao.UserRepository;
import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final String ROLE_REGISTERED = "REGISTERED";

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public void save(User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(ROLE_REGISTERED));
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

    public void straightSave(User user) {
        userRepository.save(user);
    }

    public List<ServiceUnit> getActiveServicesByUserId(long userId) {
        User user = userRepository.findUserById(userId);
        return new ArrayList<>(user.getServiceUnits());
    }
}

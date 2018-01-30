package com.epam.lab.mobilepaymentsystem;


import com.epam.lab.mobilepaymentsystem.model.Role;
import com.epam.lab.mobilepaymentsystem.model.User;
import com.epam.lab.mobilepaymentsystem.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MobilePaymentSystemApplication.class})
public class UserTests {

    @Autowired
    private UserService userService;

    private static int count = 0;
    // TODO: use @After and @Before
    private User createTestUser() {
        ++count;
        User user = new User();
        user.setUsername("login test" + count);
        user.setFullname("full name");
        user.setPassword("password");
        user.setBankAccount(100);
        userService.updateUser(user);
        return user;
    }

    @Test
    public void addUser() {
        User user = createTestUser();

        String userName = user.getUsername();
        assertEquals(userName,
                userService.getByUsername(userName).getUsername());
    }

    @Test
    public void updateUser() {
        User user = createTestUser();
        String newName = "new name";
        String newFullName = "new full name";

        user.setUsername(newName);
        user.setFullname(newFullName);
        userService.updateUser(user);
        assertEquals(newFullName,
                userService.getByUsername(newName).getFullname());
    }

    @Test
    public void changeRole() {
        User user = createTestUser();
        String role = user.getRole();

        assertEquals(role,
                userService.getByUsername(user.getUsername()).getRole());

        role = Role.ROLE_LOCKED.getDisplayName();
        user.setRole(role);
        userService.updateUser(user);
        assertEquals(role,
                userService.getByUsername(user.getUsername()).getRole());
    }
}

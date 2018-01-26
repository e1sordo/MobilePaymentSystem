package com.epam.lab.mobilepaymentsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        String pass1 = "admin";
//        String pass2 = "123456";
//        String pass3 = "qwerty";
//        System.out.println(passwordEncoder.encode(pass1));
//        System.out.println(passwordEncoder.encode(pass2));
//        System.out.println(passwordEncoder.encode(pass3));
    }
}
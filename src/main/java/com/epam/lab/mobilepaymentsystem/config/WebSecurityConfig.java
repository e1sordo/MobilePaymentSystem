package com.epam.lab.mobilepaymentsystem.config;

import com.epam.lab.mobilepaymentsystem.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailService userService;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    public WebSecurityConfig(UserDetailService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(
                        "/h2/**",
                        "/registration",
                        "/profile",
                        "/bills/**",
                        "/services/**",
                        "/h2/**",
                        "/test/**",
                        "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                        "/data/**", "/dist/**", "/js/**", "/vendor/**").permitAll()
                .antMatchers(
                        "/users").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
            .logout()
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
            .httpBasic()
                .and()
            .csrf().disable()
            .headers().frameOptions().sameOrigin();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.userDetailsService(userService);
    }
}

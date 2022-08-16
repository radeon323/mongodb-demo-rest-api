package com.luxoft.olshevchenko.mongodbdemo.testutils;

import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.ADMIN;
import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.USER;

/**
 * @author Oleksandr Shevchenko
 */
@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        ApiUser admin = new ApiUser("admin", "pass", ADMIN.getGrantedAuthorities(), true,true,true,true);
        ApiUser user = new ApiUser("user", "pass", USER.getGrantedAuthorities(), true,true,true,true);
        return new InMemoryUserDetailsManager(Arrays.asList(
                admin, user
        ));
    }
}

package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public interface ApiUserService extends UserDetailsService {
    List<ApiUser> findAll();
    ApiUser getById(String id);
    ApiUser getByName(String name);
    String delete(String id);
    ApiUser saveUser(ApiUser user);

    ApiUser saveAdmin(ApiUser user);
}


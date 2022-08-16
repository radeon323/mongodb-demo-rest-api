package com.luxoft.olshevchenko.mongodbdemo.controller;

import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import com.luxoft.olshevchenko.mongodbdemo.security.UserRole;
import com.luxoft.olshevchenko.mongodbdemo.service.DefaultApiUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @author Oleksandr Shevchenko
 */
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class ApiUserController {

    private final DefaultApiUserService apiUserService;
    Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApiUser> fetchUsers(@RequestParam(value="id", required = false) String id,
                                    @RequestParam(value="name", required = false) String name) {
        List<ApiUser> users = null;

        if (id != null) {
            ApiUser user = apiUserService.getById(id);
            if (user != null) {
                users = List.of(user);
            }
        } else if (name != null) {
            ApiUser user = apiUserService.getByName(name);
            if (user != null) {
                users = List.of(user);
            }
        } else {
            users = apiUserService.findAll();
        }
        return users;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiUser> saveApiUserAdmin(@RequestBody @Valid ApiUser user, @RequestParam(value="role", required = false) UserRole role) {
        if (role != null) {
            if (role.getGrantedAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                apiUserService.saveAdmin(user);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            apiUserService.saveUser(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiUser updateApiUser(@RequestBody @Valid ApiUser user, @RequestParam(value="id") String id) {
        user.setUserId(id);
        if (user.getGrantedAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            apiUserService.saveAdmin(user);
        } else {
            apiUserService.saveUser(user);
        }
        return user;
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteApiUserForAdmin(@RequestParam(value="id") String id) {
        apiUserService.delete(id);
        return id;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiUser saveUser(@RequestBody @Valid ApiUser user) {
        apiUserService.saveUser(user);
        return user;
    }

    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiUser updateCurrentUser(@RequestBody @Valid ApiUser user, @AuthenticationPrincipal ApiUser currentUser) {
        user.setUserId(currentUser.getUserId());
        if (currentUser.getGrantedAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.info("Update user with ADMIN role");
            apiUserService.saveAdmin(user);
        } else {
            logger.info("Update user with USER role");
            apiUserService.saveUser(user);
        }
        return user;
    }

    @DeleteMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteCurrentUser(@AuthenticationPrincipal ApiUser currentUser) {
        String id = currentUser.getUserId();
        apiUserService.delete(id);
        return id;
    }


}

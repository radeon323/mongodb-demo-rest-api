package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import com.luxoft.olshevchenko.mongodbdemo.repository.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.ADMIN;
import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.USER;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class DefaultApiUserService implements ApiUserService {
    private final ApiUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<ApiUser> findAll() {
        return repository.findAll();
    }

    @Override
    public ApiUser getById(String id) {
        Optional<ApiUser> optionalUser = repository.findById(id);
        ApiUser user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            logger.info("User with id {} not found", id);
        }
        return user;
    }

    @Override
    public ApiUser getByName(String name) {
        Optional<ApiUser> optionalUser = repository.findApiUserByUsername(name);
        ApiUser user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            logger.info("User with name {} not found", name);
        }
        return user;
    }

    @Override
    public ApiUser saveUser(ApiUser user) {
        return save(user, USER.getGrantedAuthorities());
    }

    @Override
    public ApiUser saveAdmin(ApiUser user) {
        return save(user, ADMIN.getGrantedAuthorities());
    }

    @Override
    public String delete(String id) {
        repository.deleteById(id);
        logger.info("Order with id {} has been deleted!", id);
        return id;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("In DefaultApiUserService loadUserByUsername()");
        return repository
                .findApiUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    public synchronized ApiUser save(ApiUser user, Set<SimpleGrantedAuthority> authorities) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setGrantedAuthorities(authorities);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        repository.save(user);
        return user;
    }


}

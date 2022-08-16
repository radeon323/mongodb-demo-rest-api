package com.luxoft.olshevchenko.mongodbdemo.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserPermission.*;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {
    GUEST(Set.of(ORDER_READ)),
    USER(Set.of(ORDER_READ, ORDER_WRITE)),
    ADMIN(Set.of(ORDER_READ, ORDER_WRITE, ORDER_DELETE, USER_READ, USER_WRITE));

    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }


}

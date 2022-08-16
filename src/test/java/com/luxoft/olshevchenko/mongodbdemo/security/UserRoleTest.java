package com.luxoft.olshevchenko.mongodbdemo.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class UserRoleTest {

    private final Set<SimpleGrantedAuthority> adminAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
            new SimpleGrantedAuthority("orders:read"),
            new SimpleGrantedAuthority("orders:write"),
            new SimpleGrantedAuthority("orders:delete"),
            new SimpleGrantedAuthority("users:read"),
            new SimpleGrantedAuthority("users:write"));
    private final Set<SimpleGrantedAuthority> userAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("orders:read"),
            new SimpleGrantedAuthority("orders:write"));

    @Test
    void testGetGrantedAuthoritiesForAdmin() {
        Set<SimpleGrantedAuthority> grantedAuthorities = UserRole.ADMIN.getGrantedAuthorities();
        assertEquals(adminAuthorities, grantedAuthorities);
        assertNotEquals(userAuthorities, grantedAuthorities);
    }

    @Test
    void testGetGrantedAuthoritiesForUser() {
        Set<SimpleGrantedAuthority> grantedAuthorities = UserRole.USER.getGrantedAuthorities();
        assertEquals(userAuthorities, grantedAuthorities);
        assertNotEquals(adminAuthorities, grantedAuthorities);
    }


}
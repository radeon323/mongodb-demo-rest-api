package com.luxoft.olshevchenko.mongodbdemo.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@RequiredArgsConstructor
public enum UserPermission {
    ORDER_READ("orders:read"),
    ORDER_WRITE("orders:write"),
    ORDER_DELETE("orders:delete"),
    USER_READ("users:read"),
    USER_WRITE("users:write");

    private final String permission;

}

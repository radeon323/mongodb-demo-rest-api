package com.luxoft.olshevchenko.mongodbdemo.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@NoArgsConstructor
public class UsernameAndPasswordAuthenticationRequest {
    private String username;
    private String password;
}

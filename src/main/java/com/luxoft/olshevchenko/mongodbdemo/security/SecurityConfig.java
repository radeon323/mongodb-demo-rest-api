package com.luxoft.olshevchenko.mongodbdemo.security;

import com.luxoft.olshevchenko.mongodbdemo.service.DefaultApiUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserPermission.*;


/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final DefaultApiUserService apiUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()

                .antMatchers(HttpMethod.GET,"/api/v1/store-orders/**").hasAuthority(ORDER_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/v1/store-orders/**").hasAuthority(ORDER_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/api/v1/store-orders/**").hasAuthority(ORDER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/api/v1/store-orders/**").hasAuthority(ORDER_DELETE.getPermission())

                .antMatchers(HttpMethod.POST,"/api/v1/users/register").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/v1/users/edit").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/v1/users/edit").permitAll()

                .antMatchers(HttpMethod.GET,"/api/v1/users/**").hasAuthority(USER_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/v1/users/**").hasAuthority(USER_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/api/v1/users/**").hasAuthority(USER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/api/v1/users/**").hasAuthority(USER_WRITE.getPermission())

                .anyRequest()
                .authenticated()
                .and()

                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(apiUserService);
        return provider;
    }


}

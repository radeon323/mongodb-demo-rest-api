package com.luxoft.olshevchenko.mongodbdemo.security;

import com.luxoft.olshevchenko.mongodbdemo.jwt.JwtConfig;
import com.luxoft.olshevchenko.mongodbdemo.jwt.JwtTokenVerifier;
import com.luxoft.olshevchenko.mongodbdemo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.luxoft.olshevchenko.mongodbdemo.service.DefaultApiUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserPermission.*;


/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final DefaultApiUserService apiUserService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)

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
                .authenticated();
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

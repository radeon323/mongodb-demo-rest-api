package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import com.luxoft.olshevchenko.mongodbdemo.repository.ApiUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.ADMIN;
import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class DefaultApiUserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApiUserRepository apiUserRepository;

    private DefaultApiUserService apiUserService;

    private final ApiUser admin = new ApiUser("admin", "pass", ADMIN.getGrantedAuthorities(), true,true,true,true);
    private final ApiUser user = new ApiUser("user", "pass", USER.getGrantedAuthorities(), true,true,true,true);
    private final List<ApiUser> usersList = List.of(admin, user);
    private final Set<SimpleGrantedAuthority> adminAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                                                                        new SimpleGrantedAuthority("orders:read"),
                                                                        new SimpleGrantedAuthority("orders:write"),
                                                                        new SimpleGrantedAuthority("orders:delete"),
                                                                        new SimpleGrantedAuthority("users:read"),
                                                                        new SimpleGrantedAuthority("users:write"));
    private final Set<SimpleGrantedAuthority> userAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"),
                                                                        new SimpleGrantedAuthority("orders:read"),
                                                                        new SimpleGrantedAuthority("orders:write"));


    @BeforeEach
    public void before() {
        apiUserService = new DefaultApiUserService(apiUserRepository, passwordEncoder);
        admin.setUserId("62fa602bbed1775efd45dde3");
        user.setUserId("62fa5948d753383e0882fbea");
    }

    @Test
    void testFindAll() {
        Mockito.when(apiUserRepository.findAll()).thenReturn(usersList);
        List<ApiUser> actualUsers = apiUserService.findAll();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals("admin", actualUsers.get(0).getUsername());
        assertEquals("user", actualUsers.get(1).getUsername());
        assertEquals(adminAuthorities, actualUsers.get(0).getGrantedAuthorities());
        assertEquals(userAuthorities, actualUsers.get(1).getGrantedAuthorities());
        assertEquals(admin, actualUsers.get(0));
        assertEquals(user, actualUsers.get(1));
    }

    @Test
    void testGetById() {
        Mockito.when(apiUserRepository.findById("62fa602bbed1775efd45dde3")).thenReturn(Optional.of(admin));
        ApiUser actualUser = apiUserService.getById("62fa602bbed1775efd45dde3");
        assertEquals(admin, actualUser);
    }

    @Test
    void testGetByName() {
        Mockito.when(apiUserRepository.findApiUserByUsername("admin")).thenReturn(Optional.of(admin));
        ApiUser actualUser = apiUserService.getByName("admin");
        assertEquals(admin, actualUser);
    }

    @Test
    void testReturnUserWhenSaveOrUpdateRequest() {
        ApiUser actualUser = apiUserService.saveUser(user);
        assertEquals(userAuthorities, actualUser.getGrantedAuthorities());
        assertNotEquals(adminAuthorities, actualUser.getGrantedAuthorities());
        assertEquals(user, actualUser);
    }

    @Test
    void testReturnUserAdminWhenSaveOrUpdateRequest() {
        ApiUser actualUser = apiUserService.saveAdmin(admin);
        assertEquals(adminAuthorities, actualUser.getGrantedAuthorities());
        assertNotEquals(userAuthorities, actualUser.getGrantedAuthorities());
        assertEquals(admin, actualUser);
    }

    @Test
    void testLoadUserByUsername() {
        Mockito.when(apiUserRepository.findApiUserByUsername("admin")).thenReturn(Optional.of(admin));
        UserDetails userDetails = apiUserService.loadUserByUsername("admin");
        assertEquals(admin, userDetails);
    }

    @Test
    void testLoadUserByUsernameIfUserNotFound() {
        Mockito.when(apiUserRepository.findApiUserByUsername("ergaegaer")).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> apiUserService.loadUserByUsername("ergaegaer"));
    }


}
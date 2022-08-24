package com.luxoft.olshevchenko.mongodbdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import com.luxoft.olshevchenko.mongodbdemo.testutils.SpringSecurityWebAuxTestConfig;
import com.luxoft.olshevchenko.mongodbdemo.service.DefaultApiUserService;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.ADMIN;
import static com.luxoft.olshevchenko.mongodbdemo.security.UserRole.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc
@WithUserDetails("admin")
class ApiUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefaultApiUserService apiUserService;

    private final ApiUser admin = new ApiUser("admin", "pass", ADMIN.getGrantedAuthorities(), true,true,true,true);
    private final ApiUser user = new ApiUser("user", "pass", USER.getGrantedAuthorities(), true,true,true,true);
    private final List<ApiUser> usersList = List.of(admin, user);
    JSONArray adminAuthoritiesJson;
    JSONArray userAuthoritiesJson;

    @BeforeEach
    public void before() throws JsonProcessingException, ParseException {
        admin.setUserId("62fa602bbed1775efd45dde3");
        user.setUserId("62fa5948d753383e0882fbea");

        Set<SimpleGrantedAuthority> adminAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("orders:read"),
                new SimpleGrantedAuthority("orders:write"),
                new SimpleGrantedAuthority("orders:delete"),
                new SimpleGrantedAuthority("users:read"),
                new SimpleGrantedAuthority("users:write"));
        Set<SimpleGrantedAuthority> userAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("orders:read"),
                new SimpleGrantedAuthority("orders:write"));

        JSONParser jsonParser= new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        adminAuthoritiesJson = (JSONArray) jsonParser.parse(objectMapper.writeValueAsString(adminAuthorities));
        userAuthoritiesJson = (JSONArray) jsonParser.parse(objectMapper.writeValueAsString(userAuthorities));
    }

    @Test
    void testFetchUsers() throws Exception {
        when(apiUserService.findAll()).thenReturn(usersList);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].password").value("pass"))
                .andExpect(jsonPath("$[0].grantedAuthorities").value(Matchers.containsInAnyOrder(adminAuthoritiesJson.toArray())))
                .andExpect(jsonPath("$[1].username").value("user"))
                .andExpect(jsonPath("$[1].password").value("pass"))
                .andExpect(jsonPath("$[1].grantedAuthorities").value(Matchers.containsInAnyOrder(userAuthoritiesJson.toArray())));
        verify(apiUserService, times(1)).findAll();
    }

    @Test
    void testFetchUsersGetUserById() throws Exception {
        when(apiUserService.getById("62fa602bbed1775efd45dde3")).thenReturn(admin);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/?id={id}", "62fa602bbed1775efd45dde3")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].password").value("pass"))
                .andExpect(jsonPath("$[0].grantedAuthorities").value(Matchers.containsInAnyOrder(adminAuthoritiesJson.toArray())));
        verify(apiUserService, times(1)).getById("62fa602bbed1775efd45dde3");
    }

    @Test
    void testFetchUsersGetUserByName() throws Exception {
        when(apiUserService.getByName("admin")).thenReturn(admin);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/?name={name}", "admin")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].password").value("pass"))
                .andExpect(jsonPath("$[0].grantedAuthorities").value(Matchers.containsInAnyOrder(adminAuthoritiesJson.toArray())));
        verify(apiUserService, times(1)).getByName("admin");
    }


}
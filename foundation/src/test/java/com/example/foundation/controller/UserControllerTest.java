package com.example.foundation.controller;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /api/users/register - success")
    void register_success() throws Exception {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("alice");
        req.setPassword("secret");
        req.setUserRole("RESIDENT");

        given(userService.registerUser(any())).willReturn("Registered");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Registered")));
    }

    @Test
    @DisplayName("POST /api/users/login - success")
    void login_success() throws Exception {
        Resident r = new Resident("bob", "pwd", "addr", "0123");
        r.setId("res-1");

        given(userService.login(anyString(), anyString())).willReturn(r);

        Resident body = new Resident("bob", "pwd", "", "");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("res-1")));
    }

    @Test
    @DisplayName("GET /api/users/all - returns residents")
    void getAllUsers_returnsList() throws Exception {
        Resident r = new Resident("carl", "pwd", "addr", "0123");
        r.setId("res-2");

        given(userService.getAllResidents()).willReturn(List.of(r));

        mockMvc.perform(get("/api/users/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("res-2")));
    }
}
